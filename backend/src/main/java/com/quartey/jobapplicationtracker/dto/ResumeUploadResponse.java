package com.quartey.jobapplicationtracker.dto;

public record ResumeUploadResponse(
        ResumeResponse resume,
        ResumeParserResponse parsedData
) {}
