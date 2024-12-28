package com.school.controllers;

import com.school.dto.AuthenticationRequestDto;
import com.school.entities.User;
import com.school.enums.UserRole;
import com.school.repositories.UserRepositories;
import com.school.util.AppConstant;
import com.school.util.JWTHelper;
import org.json.JSONObject;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/school/security")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private UserRepositories userRepositories;



    @PostMapping("/authenticate")
    public  ResponseEntity<Map<String, Object>> createAuthenticationToken(@RequestBody AuthenticationRequestDto authentication, HttpServletResponse response) throws IOException {

        try {
       authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authentication.getEmail(),authentication.getPassword()));
   }catch (BadCredentialsException e){
   throw new BadCredentialsException("Invalid email or password");
}catch (DisabledException disabledException){
       response.sendError(HttpServletResponse.SC_FOUND,"User is not created!!");
   }
   final UserDetails userDetails=userDetailsService.loadUserByUsername(authentication.getEmail());
        Optional<User>optionalUser=this.userRepositories.findByEmail(userDetails.getUsername());

        final String token= jwtHelper.generateToken(userDetails.getUsername(),UserRole.ADMIN);

        if (optionalUser.isPresent()) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("UserId", optionalUser.get().getId());
            responseBody.put("role", optionalUser.get().getUserRole());
            responseBody.put("token", token);
            return ResponseEntity.ok(responseBody);
        }
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "User not found");
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(responseBody);
    }

   }
