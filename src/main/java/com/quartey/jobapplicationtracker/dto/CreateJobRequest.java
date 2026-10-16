package com.quartey.jobapplicationtracker.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateJobRequest(@NotBlank
                               @Size(max = 255)
                               String companyName,

                               @NotBlank
                               @Size(max = 255)
                               String jobTitle,

                               @NotBlank
                               @Size(max = 255)
                               String location,

                               @Size(max = 2048)
                               String jobUrl,

                               @NotBlank
                               String description,

                               @DecimalMin(value = "0.0")
                               BigDecimal minSalary,

                               @DecimalMin(value = "0.0")
                               BigDecimal maxSalary) {
}
