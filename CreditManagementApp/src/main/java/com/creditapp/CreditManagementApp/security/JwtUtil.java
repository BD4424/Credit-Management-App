package com.creditapp.CreditManagementApp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    private static final String SECRET_KEY = "lahjsdlhjdalkjsdakjdakshllakhdklashdalsjdjhasjdkajsdjanjasdhjkads";
    private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getUserName())
                .claim("role","ROLE_"+user.getRole().name())
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

    public String getUserRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public String extractUserNameFromRequest(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return getUserNameFromToken(token);
        }
        throw new RuntimeException("JWT Token is missing or invalid");
    }
}
