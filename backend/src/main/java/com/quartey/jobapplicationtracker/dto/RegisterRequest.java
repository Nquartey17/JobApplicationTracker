package com.quartey.jobapplicationtracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank(message = "Username required")
                          @Size(min = 3, max = 50, message = "Username must be between 3-50 characters")
                          String username,

                              @NotBlank(message = "Email required")
                          @Email(message = "Please provide a valid email address")
                          @Size(max = 255, message = "Email must not exceed 255 characters")
                          String email,

                              @NotBlank(message = "Password required")
                          @Size(min = 8, message = "Password must be at least 8 characters")
                          String password
                          ) {
}
