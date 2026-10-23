
package com.quartey.jobapplicationtracker.controller;

import com.quartey.jobapplicationtracker.dto.ApplicationResponse;
import com.quartey.jobapplicationtracker.dto.CreateApplicationRequest;
import com.quartey.jobapplicationtracker.dto.UpdateApplicationStatusRequest;
import com.quartey.jobapplicationtracker.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    //Partially changing an existing resource which is why to use patch instead of put mapping (Change everything)
    @PatchMapping("/{id}/status")
    public ApplicationResponse updateApplicationStatusRequest(@PathVariable Long id,
                                                              @Valid @RequestBody UpdateApplicationStatusRequest request,
                                                              JwtAuthenticationToken authentication) {

        return applicationService.updateApplicationStatus(id, request, authentication);
    }


}
