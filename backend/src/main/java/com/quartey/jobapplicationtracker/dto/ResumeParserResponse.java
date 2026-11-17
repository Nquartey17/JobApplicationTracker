package com.quartey.jobapplicationtracker.dto;

import java.util.List;

public record ResumeParserResponse(String filename, ExtractedData extracted_data) {
    public record ExtractedData(List<String> skills, Education education, WorkExperience work_experience) {}
    public record Education(String education_section, List<String> degrees, List<String> graduation_years) {}
    public record WorkExperience(String experience_section) {}
}
