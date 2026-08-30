package com.aireviewer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pull_reviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PullReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // GitHub PR info
    @Column(nullable = false)
    private String repoFullName;      // e.g. "sandeepreddythippareddy/my-project"

    @Column(nullable = false)
    private Integer prNumber;          // e.g. 42

    @Column(nullable = false)
    private String prTitle;

    private String prAuthor;

    // The code diff that was reviewed
    @Column(columnDefinition = "TEXT")
    private String diffContent;

    // Claude's review response
    @Column(columnDefinition = "TEXT")
    private String reviewComment;

    // Status tracking
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = ReviewStatus.PENDING;
    }

    public enum ReviewStatus {
        PENDING, COMPLETED, FAILED
    }
}
