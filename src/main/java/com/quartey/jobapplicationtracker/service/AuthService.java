package com.quartey.jobapplicationtracker.service;

import com.quartey.jobapplicationtracker.dto.RegisterRequest;
import com.quartey.jobapplicationtracker.dto.UserResponse;
import com.quartey.jobapplicationtracker.entity.User;
import com.quartey.jobapplicationtracker.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail());
    }
}
