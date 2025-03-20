package com.creditapp.CreditManagementApp.controller;

import com.creditapp.CreditManagementApp.DTO.AuthRequest;
import com.creditapp.CreditManagementApp.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already registered");
        }

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){

        System.out.println("Login API hit!");

        User user = userRepository.findByUserName(authRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        String token = jwtUtil.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token",token);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/refresh-token")
//    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request){
//        if (jwtUtil.validateToken(request.refreshToken())){
//            String userNameFromToken = jwtUtil.getUserNameFromToken(request.refreshToken());
//            String accessToken = jwtUtil.generateToken(userNameFromToken, true);
//            String refreshToken = jwtUtil.generateToken(userNameFromToken, false);
//
//            User user = userRepository.findByEmail(userNameFromToken).get();
//            JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken, user);
//
//            return ResponseEntity.ok(jwtResponse);
//        }
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
//    }
}

