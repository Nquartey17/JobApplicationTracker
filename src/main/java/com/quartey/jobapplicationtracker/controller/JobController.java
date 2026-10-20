package com.quartey.jobapplicationtracker.controller;

import com.quartey.jobapplicationtracker.dto.CreateJobRequest;
import com.quartey.jobapplicationtracker.dto.JobResponse;
import com.quartey.jobapplicationtracker.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse createJob(@Valid @RequestBody CreateJobRequest request,
                                 JwtAuthenticationToken authenticationToken) {

        return jobService.createJob(request, authenticationToken);
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getJobs(JwtAuthenticationToken authentication) {

        List<JobResponse> jobs = jobService.getJobs(authentication);
        return ResponseEntity.ok(jobs);
    }
}
