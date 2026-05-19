package com.trung.identityservice.service;

import com.trung.identityservice.config.JwtUtil;
import com.trung.identityservice.dto.FormLogin;
import com.trung.identityservice.dto.FormRegister;
import com.trung.identityservice.dto.JwtResponse;
import com.trung.identityservice.entity.Users;
import com.trung.identityservice.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration}")
    private long expirationTime;
    public String register(FormRegister request){
        Users users = new Users();
        users.setUsername(request.getUsername());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setRole(request.getRole());
        authRepository.save(users);
        return "User registered successfully";
    }

    public String testToken(String username){
        Users users = authRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return "Token: " + jwtUtil.generateToken(users);
    }

    public JwtResponse login(FormLogin request){
        Users users = authRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), users.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtUtil.generateToken(users);
        return JwtResponse.builder()
                .token(token)
                .type("Bearer")
                .expiration(expirationTime)
                .build();
    }
}
