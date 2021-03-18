package com.IdleAdventure.adventuregame.api;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import com.IdleAdventure.adventuregame.model.Inventory;
import com.IdleAdventure.adventuregame.model.Stats;
import com.IdleAdventure.adventuregame.model.UserHomeDetails;
import com.IdleAdventure.adventuregame.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.IdleAdventure.adventuregame.model.GameUser;


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
            user.setGold(100.00);
            this.userRepository.save(user);
            this.setNewStats(user);
            this.setNewInventory(user);
        }
        return ResponseEntity.ok(HttpEntity.EMPTY);
    }

    private void setNewStats(@NotEmpty GameUser user){
        Stats userStats = new Stats(user.getId(),1,0,20,10,0,0,0,100,0);
        this.statsRepository.save(userStats);
        this.equippedRepository.initalizeEquipped(user.getId());
    }

    private void setNewInventory(@NotEmpty GameUser user) {
        List<Inventory> starterInv = new ArrayList();
        starterInv.add(new Inventory(user.getId(), 1, 1));
        starterInv.add(new Inventory(user.getId(), 2, 1));
        starterInv.add(new Inventory(user.getId(), 3, 1));
        this.inventoryRepository.saveAll(starterInv);
    }

    @RequestMapping("/api/home")
    public UserHomeDetails homepage() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        int id = this.userRepository.findByUsername(username).getId();
        return this.userRepository.getUserDetails(id);
    }

    @RequestMapping("api/account/changePassword")
    public void changePassword(@RequestBody GameUser user) throws Exception {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String newPassword = this.bCryptPasswordEncoder.encode(user.getPassword());
        this.userRepository.changePassword(user.getUsername(), newPassword);
    }
}