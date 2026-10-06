package com.quartey.jobapplicationtracker.service;

import com.quartey.jobapplicationtracker.entity.User;
import com.quartey.jobapplicationtracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional // Tell Spring db operations running transactions
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        //Add password hash here

        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
