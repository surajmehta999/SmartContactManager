package com.scm.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scm.repositories.UserRepo;
import com.scm.entities.User;

@Service
public class SecurityCustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //apne user ko load krana hai
      User user = userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

      if (!user.isEmailVerified()) { // Assuming `isEmailVerified` is the method for the `emailVerified` field
          throw new UsernameNotFoundException("Email not verified for user: " + username);
      }

      return user; // User should implement UserDetails interface
     
        // return userRepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not found with email : "+username));
    }

}
