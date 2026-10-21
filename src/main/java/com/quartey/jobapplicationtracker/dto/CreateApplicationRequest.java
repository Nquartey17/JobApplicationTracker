package com.quartey.jobapplicationtracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateApplicationRequest(@NotNull
                                       @Positive //Number must be greater than 0
                                       Long jobId,

                                       @NotNull
                                       @Positive
                                       Long resumeId,

                                       @Size(max = 5000)
                                       String notes) {
}
