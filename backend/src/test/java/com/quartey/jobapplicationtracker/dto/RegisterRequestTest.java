package com.quartey.jobapplicationtracker.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    @Test
    void validRequestShouldPassValidation() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "test@example.com",
                "password123"
        );

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void blankUsernameShouldFailValidation() {
        RegisterRequest request = new RegisterRequest(
                "",
                "test@example.com",
                "password123"
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shortUsernameShouldFailValidation() {
        RegisterRequest request = new RegisterRequest(
                "ab",
                "test@example.com",
                "password123"
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void invalidEmailShouldFailValidation() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "not-an-email",
                "password123"
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shortPasswordShouldFailValidation() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "test@example.com",
                "short"
        );

        assertFalse(validator.validate(request).isEmpty());
    }
}