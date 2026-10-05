package com.quartey.jobapplicationtracker.repository;

import com.quartey.jobapplicationtracker.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByStatusContainingIgnoreCase(String status);
}
