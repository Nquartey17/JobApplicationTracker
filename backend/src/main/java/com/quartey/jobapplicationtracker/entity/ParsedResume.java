package com.quartey.jobapplicationtracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "parsed_resume")
public class ParsedResume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto ID
    private Long id;

    @OneToOne
    @JoinColumn(name = "resume_id", nullable = false, unique = true)
    private Resume resume;

    // JsonNode stores flexible JSON structures without needing to create seperate java classes for every nested field
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode skills;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode education;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "work_experience", columnDefinition = "jsonb")
    private JsonNode workExperience;

    @Column(name = "parsed_at", nullable = false)
    private LocalDateTime parsedAt;

    @PrePersist
    protected void onCreate() {
        parsedAt = LocalDateTime.now();
    }

}
