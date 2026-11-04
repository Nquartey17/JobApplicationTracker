package com.quartey.jobapplicationtracker.repository;

import com.quartey.jobapplicationtracker.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStatusContainingIgnoreCase(String status);
    List<Application> findByUserId(Long userId);
    boolean existsByUser_IdAndJob_Id(Long userId, Long jobId); // check if user has already applied to specific job
}
