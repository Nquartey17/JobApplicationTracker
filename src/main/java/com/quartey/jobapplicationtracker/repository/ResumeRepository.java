package com.quartey.jobapplicationtracker.repository;

import com.quartey.jobapplicationtracker.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByNameContainingIgnoreCase(String name);
    List<Resume> findByFilePath(String filePath);
    List<Resume> findByVersion(Integer version);
}
