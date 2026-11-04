package com.quartey.jobapplicationtracker.dto;

import java.time.LocalDateTime;

public record ResumeResponse(Long id,
                             String name,
                             Integer version,
                             LocalDateTime createdAt,
                             LocalDateTime updatedAt) {
}
