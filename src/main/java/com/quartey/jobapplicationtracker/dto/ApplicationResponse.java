package com.quartey.jobapplicationtracker.dto;

import java.time.LocalDateTime;

public record ApplicationResponse(Long applicationId,
                                  Long jobId,
                                  String companyName,
                                  String jobTitle,
                                  Long resumeId,
                                  String status,
                                  LocalDateTime appliedAt,
                                  LocalDateTime lastUpdatedAt,
                                  String notes) {

}
