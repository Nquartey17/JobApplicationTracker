
package com.quartey.jobapplicationtracker.service;

import com.quartey.jobapplicationtracker.dto.RegisterRequest;
import com.quartey.jobapplicationtracker.dto.UserResponse;
import com.quartey.jobapplicationtracker.entity.User;
import com.quartey.jobapplicationtracker.exception.DuplicateResourceException;
import com.quartey.jobapplicationtracker.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock //Mock repository to test without connecting to PostgreSQL
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder);
    }

    // 1. Successful registration
    @Test
    void registerUser_Success() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "test@example.com",
                "password123"
        );

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        UserResponse response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("testuser", response.username());
        assertEquals("test@example.com", response.email());

        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository).save(any(User.class));
    }

    // 2. Duplicate username is rejected
    @Test
    void registerUser_DuplicateUsername_ThrowsException() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "test@example.com",
                "password123"
        );

        when(userRepository.existsByUsername("testuser"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> userService.registerUser(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    // 3. Duplicate email is rejected
    @Test
    void registerUser_DuplicateEmail_ThrowsException() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "test@example.com",
                "password123"
        );

        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> userService.registerUser(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    // 4. Stored password is hashed and matches the original
    @Test
    void registerUser_PasswordIsHashed() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "test@example.com",
                "password123"
        );

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        userService.registerUser(request);

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        String storedHash = savedUser.getPasswordHash();

        assertNotNull(storedHash);
        assertNotEquals(request.password(), storedHash);

        assertTrue(
                passwordEncoder.matches(request.password(), storedHash),
                "Stored password hash should match the original password"
        );
    }

    // 5. Response exposes no password or password hash
    @Test
    void registerUser_ResponseDoesNotExposePasswordHash() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "test@example.com",
                "password123"
        );

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        UserResponse response = userService.registerUser(request);

        Set<String> responseFields = Arrays.stream(
                        UserResponse.class.getRecordComponents()
                )
                .map(component -> component.getName())
                .collect(Collectors.toSet());

        assertEquals(
                Set.of("id", "username", "email"),
                responseFields
        );

        assertFalse(response.toString().contains("password123"));
    }
}
