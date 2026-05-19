package com.trung.identityservice.service;

import com.trung.identityservice.dto.FormRegister;
import com.trung.identityservice.entity.Users;
import com.trung.identityservice.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(FormRegister request){
        Users users = new Users();
        users.setUsername(request.getUsername());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setRole(request.getRole());
        authRepository.save(users);
        return "User registered successfully";
    }
}
