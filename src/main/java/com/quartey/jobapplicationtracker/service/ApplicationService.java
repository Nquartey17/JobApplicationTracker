package com.quartey.jobapplicationtracker.service;

import com.quartey.jobapplicationtracker.dto.ApplicationResponse;
import com.quartey.jobapplicationtracker.dto.CreateApplicationRequest;
import com.quartey.jobapplicationtracker.entity.Application;
import com.quartey.jobapplicationtracker.entity.Job;
import com.quartey.jobapplicationtracker.entity.Resume;
import com.quartey.jobapplicationtracker.entity.User;
import com.quartey.jobapplicationtracker.repository.ApplicationRepository;
import com.quartey.jobapplicationtracker.repository.JobRepository;
import com.quartey.jobapplicationtracker.repository.ResumeRepository;
import com.quartey.jobapplicationtracker.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              JobRepository jobRepository,
                              ResumeRepository resumeRepository,
                              UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ApplicationResponse createApplication(CreateApplicationRequest request,
                                                 JwtAuthenticationToken authentication) {

        String username = authentication.getToken().getSubject();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        Job job = jobRepository.findById(request.jobId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Job not found"));

        // Make sure the job found belongs to the user
        if (!job.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Job not found");
        }

        Resume resume = resumeRepository.findById(request.resumeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Resume not found"));

        // Make sure resume belongs to the user
        if (!resume.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Resume not found");
        }

        // Check if user has already applied to a job
        if (applicationRepository.existsByUser_IdAndJob_Id(
                user.getId(), job.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "You already have an application for this job");
        }

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setResume(resume);
        application.setStatus("APPLIED");
        application.setNotes(request.notes());

        Application saved = applicationRepository.save(application);

        return new ApplicationResponse(
                saved.getId(),
                saved.getJob().getId(),
                saved.getJob().getCompanyName(),
                saved.getJob().getJobTitle(),
                saved.getResume().getId(),
                saved.getResume().getName(),
                saved.getStatus(),
                saved.getAppliedAt(),
                saved.getLastUpdatedAt(),
                saved.getNotes()
        );
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplications(JwtAuthenticationToken authentication) {

        String username = authentication.getToken().getSubject();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        List<Application> applications = applicationRepository.findByUserId(user.getId());

        return applications.stream()
                .map(application -> new ApplicationResponse(
                application.getId(),
                application.getJob().getId(),
                application.getJob().getCompanyName(),
                application.getJob().getJobTitle(),
                application.getResume().getId(),
                application.getResume().getName(),
                application.getStatus(),
                application.getAppliedAt(),
                application.getLastUpdatedAt(),
                application.getNotes()
                ))
                .toList();

    }


}
