package com.quartey.jobapplicationtracker.repository;

import com.quartey.jobapplicationtracker.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCompanyNameContainingIgnoreCase(String companyName);
    List<Job> findByJobTitleContainingIgnoreCase(String jobTitle);
    List<Job> findByLocationContainingIgnoreCase(String location);
    List<Job> findByMinSalaryGreaterThanEqual(BigDecimal minSalary);
    List<Job> findByMaxSalaryLessThanEqual(BigDecimal maxSalary);
}
