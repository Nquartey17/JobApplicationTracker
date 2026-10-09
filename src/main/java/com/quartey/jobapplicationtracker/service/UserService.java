package com.quartey.jobapplicationtracker.service;

import com.quartey.jobapplicationtracker.dto.RegisterRequest;
import com.quartey.jobapplicationtracker.dto.UserResponse;
import com.quartey.jobapplicationtracker.entity.User;
import com.quartey.jobapplicationtracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional // Tell Spring db operations running transactions
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerUser(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            //Change to DuplicateResourceException later
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        //Password hash
        user.setPasswordHash(
                passwordEncoder.encode(request.password())
        );

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public Optional<User> getUserByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
