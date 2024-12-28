package com.school.util;

import com.school.enums.UserRole;
import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;

import java.security.Key;

import java.security.SecureRandom;
import java.util.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.function.Function;


@Component
public class JWTHelper {


        public String getUserNameFromToken(String token){
            return getClaimFromToken(token, Claims::getSubject);
        }

        public Date getExpirationDateFromToken(String token){
            return getClaimFromToken(token, Claims::getExpiration);
        }

        public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        }

        private Claims getAllClaimsFromToken(String token) {
            return Jwts.parser().setSigningKey(AppConstant.SECRET_KEY).parseClaimsJws(token).getBody();
        }

        private Boolean isTokenExpired(String token){
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        }

        public String generateToken(String username, UserRole role){
            Map<String, Object> claims = new HashMap<>();
            return doGenerateToken(claims, username, role);
        }

        public String doGenerateToken(Map<String, Object> claims, String subject, UserRole role) {
            claims.put("role", role.name());
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))  // 30 minutes expiration
                    .signWith(SignatureAlgorithm.HS256,AppConstant.SECRET_KEY)  // Using HS256 with the secret key
                    .compact();
        }

        public boolean validateToken(String token, UserDetails userDetails){
            final String username = getUserNameFromToken(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        }

        public String getRoleFromToken(String token) {
            return getClaimFromToken(token, claims -> claims.get("role", String.class));
        }

    }
