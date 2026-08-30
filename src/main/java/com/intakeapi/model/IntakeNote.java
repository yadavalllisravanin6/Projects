package com.intakeapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Represents a single patient intake / symptom note.
 *
 * IMPORTANT: this project must only ever be tested with synthetic/fake data.
 * Never enter real patient information here - there is no HIPAA-grade
 * security on this app (no auth, no encryption at rest, no audit log).
 * That's fine for a portfolio project, as long as the data in it is fake.
 */
@Entity
@Table(name = "intake_notes")
public class IntakeNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "patientAge is required")
    @Min(value = 0, message = "patientAge must be >= 0")
    @Column(nullable = false)
    private Integer patientAge;

    // The free-text symptom description, e.g.
    // "45yo, chest tightness for 2 days, worse with exertion, mild shortness of breath"
    @NotBlank(message = "symptomText is required")
    @Column(nullable = false, length = 2000)
    private String symptomText;

    // Values used: SUBMITTED, TRIAGED, IN_REVIEW, CLOSED
    @Column(nullable = false)
    private String status = "SUBMITTED";

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- constructors ---

    public IntakeNote() {
        // required by JPA
    }

    public IntakeNote(Integer patientAge, String symptomText) {
        this.patientAge = patientAge;
        this.symptomText = symptomText;
    }

    // --- getters and setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public String getSymptomText() {
        return symptomText;
    }

    public void setSymptomText(String symptomText) {
        this.symptomText = symptomText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
