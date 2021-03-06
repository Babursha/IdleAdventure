package com.IdleAdventure.adventuregame.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;

import com.IdleAdventure.adventuregame.model.*;
import com.IdleAdventure.adventuregame.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
public class AccountController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private StatsRepository statsRepository;
    @Autowired
    private EquippedRepository equippedRepository;
    @Autowired
    private AchievementRepository achievementRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;


    @RequestMapping("/register")
    public ResponseEntity<?> register(@RequestBody @NotEmpty GameUser user) throws Exception {
        if (this.userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists.");
        }
        if (this.userRepository.findByEmail(user.getEmail()) != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already registered.");
        }
        else {
            this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRole("basic");
            user.setGold(1000.00);
            this.userRepository.save(user);
            this.setNewStats(user);
            this.setNewInventory(user);
            this.setNewAchievements(user);
        }
        return ResponseEntity.ok(HttpEntity.EMPTY);
    }

    private void setNewStats(@NotEmpty GameUser user){
        Stats userStats = new Stats(user.getId(),1,2,2,20,0,0,0,100,0);
        this.statsRepository.save(userStats);
        this.equippedRepository.initalizeEquipped(user.getId());
    }

    private void setNewInventory(@NotEmpty GameUser user) {
        List<Inventory> starterInv = new ArrayList();
        starterInv.add(new Inventory(user.getId(), 1, 1));
        starterInv.add(new Inventory(user.getId(), 2, 1));
        starterInv.add(new Inventory(user.getId(), 3, 1));
        starterInv.add(new Inventory(user.getId(),4,50));
        this.inventoryRepository.saveAll(starterInv);
    }

    private void setNewAchievements(@NotEmpty GameUser user){
        Achievement userBadges = new Achievement(user.getId(),0,0,0,0,0);
        this.achievementRepository.save(userBadges);
    }

    @RequestMapping("/api/home")
    public UserHomeDetails homepage() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        int id = this.userRepository.findByUsername(username).getId();
        return this.userRepository.getUserDetails(id);
    }

    @RequestMapping("/api/account/changePassword")
    public void changePassword(@RequestBody GameUser user) throws Exception {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String newPassword = this.bCryptPasswordEncoder.encode(user.getPassword());
        this.userRepository.changePassword(user.getUsername(), newPassword);
    }

}