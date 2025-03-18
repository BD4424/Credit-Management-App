package com.creditapp.CreditManagementApp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    private static final String SECRET_KEY = "lahjsdlhjdalkjsdakjdakshllakhdklashdalsjdjhasjdkajsdjanjasdhjkads";
    private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String username){

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser().setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token).getBody().getSubject();
    }

    //validate
    public boolean validateToken(String token, UserDetails userDetails){
        try {
            String username = getUserNameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
}
