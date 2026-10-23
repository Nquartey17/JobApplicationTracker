
package com.quartey.jobapplicationtracker.controller;

import com.quartey.jobapplicationtracker.dto.ApplicationResponse;
import com.quartey.jobapplicationtracker.dto.CreateApplicationRequest;
import com.quartey.jobapplicationtracker.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(
            ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse createApplication(
            @Valid @RequestBody CreateApplicationRequest request,
            JwtAuthenticationToken authentication) {

        return applicationService.createApplication(
                request, authentication);
    }

    @GetMapping
    public List<ApplicationResponse> getApplications(JwtAuthenticationToken authentication) {
        return applicationService.getApplications(authentication);

    }

}
