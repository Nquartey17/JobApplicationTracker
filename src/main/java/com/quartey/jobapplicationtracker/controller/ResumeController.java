package com.quartey.jobapplicationtracker.controller;

import com.quartey.jobapplicationtracker.dto.ResumeResponse;
import com.quartey.jobapplicationtracker.service.ResumeService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resumes")
@Validated // enable validation of the parameters passed into your controller methods
public class ResumeController {

    public final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED) // File successfully created
    public ResumeResponse uploadResume(
            @RequestParam("name") @NotBlank
            String name,

            @RequestParam(value = "version", required = false)
            Integer version,

            @RequestParam("file")
            MultipartFile file, // Files of different types

            JwtAuthenticationToken authentication) {
        return resumeService.uploadResume(name, version, file, authentication);

    }

}
