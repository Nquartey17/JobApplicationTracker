package com.quartey.jobapplicationtracker.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record JobResponse(Long id, String companyName,
                          String jobTitle,
                          String location,
                          String jobUrl,
                          String description,
                          BigDecimal minSalary,
                          BigDecimal maxSalary,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt) {
}
