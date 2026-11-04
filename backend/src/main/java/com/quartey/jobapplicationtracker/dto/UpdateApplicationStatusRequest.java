package com.quartey.jobapplicationtracker.dto;

import com.quartey.jobapplicationtracker.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateApplicationStatusRequest(@NotNull ApplicationStatus status) {
}
