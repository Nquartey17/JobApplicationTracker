
package com.quartey.jobapplicationtracker.service;

import com.quartey.jobapplicationtracker.dto.CreateJobRequest;
import com.quartey.jobapplicationtracker.dto.JobResponse;
import com.quartey.jobapplicationtracker.entity.Job;
import com.quartey.jobapplicationtracker.entity.User;
import com.quartey.jobapplicationtracker.repository.JobRepository;
import com.quartey.jobapplicationtracker.repository.UserRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(
            JobRepository jobRepository,
            UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public JobResponse createJob(CreateJobRequest request, JwtAuthenticationToken authentication) {

        String username = authentication.getToken().getSubject();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        Job job = new Job();
        job.setUser(user);
        job.setCompanyName(request.companyName());
        job.setJobTitle(request.jobTitle());
        job.setLocation(request.location());
        job.setJobUrl(request.jobUrl());
        job.setDescription(request.description());
        job.setMinSalary(request.minSalary());
        job.setMaxSalary(request.maxSalary());

        Job savedJob = jobRepository.save(job);

        return new JobResponse(
                savedJob.getId(),
                savedJob.getCompanyName(),
                savedJob.getJobTitle(),
                savedJob.getLocation(),
                savedJob.getJobUrl(),
                savedJob.getDescription(),
                savedJob.getMinSalary(),
                savedJob.getMaxSalary(),
                savedJob.getCreatedAt(),
                savedJob.getUpdatedAt()
        );
    }


    public List<JobResponse> getJobs(
            JwtAuthenticationToken authentication) {

        String username = authentication.getToken().getSubject();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        List<Job> jobs = jobRepository.findByUser_Id(user.getId());

        return jobs.stream()
                .map(job -> new JobResponse(
                        job.getId(),
                        job.getCompanyName(),
                        job.getJobTitle(),
                        job.getLocation(),
                        job.getJobUrl(),
                        job.getDescription(),
                        job.getMinSalary(),
                        job.getMaxSalary(),
                        job.getCreatedAt(),
                        job.getUpdatedAt()
                ))
                .toList();
    }
}
