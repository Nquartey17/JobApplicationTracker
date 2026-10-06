package com.quartey.jobapplicationtracker.repository;

import com.quartey.jobapplicationtracker.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    //Using Optional for single value return
    Optional<Skill> findByName(String name);
}
