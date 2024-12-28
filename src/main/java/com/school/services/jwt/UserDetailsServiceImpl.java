package com.school.services.jwt;

import com.school.entities.User;
import com.school.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepositories userRepositories;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Write a logic to get user from DB
        Optional<User>getuserdetails=this.userRepositories.findByEmail(email);
        if (!getuserdetails.isPresent()){
            throw new UsernameNotFoundException("Email not found",null);
        }
        return new org.springframework.security.core.userdetails.User(getuserdetails.get().getEmail(),getuserdetails.get().getPassword(),new ArrayList<>());
    }
}
