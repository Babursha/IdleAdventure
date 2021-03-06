package com.IdleAdventure.adventuregame.security;

import com.IdleAdventure.adventuregame.model.GameUser;
import com.IdleAdventure.adventuregame.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username){
        GameUser gameUser = userRepository.findByUsername(username);
        if(gameUser == null){
            throw new UsernameNotFoundException(username);
        }
        return new User(gameUser.getUsername(),gameUser.getPassword(),emptyList());

    }

}
