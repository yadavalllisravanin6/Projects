package com.aireviewer.repository;

import com.aireviewer.model.PullReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PullReviewRepository extends JpaRepository<PullReview, Long> {

    // Find all reviews for a specific repo
    List<PullReview> findByRepoFullNameOrderByCreatedAtDesc(String repoFullName);

    // Find all reviews for a specific PR
    List<PullReview> findByRepoFullNameAndPrNumber(String repoFullName, Integer prNumber);

    // Find recent reviews (for dashboard)
    List<PullReview> findTop10ByOrderByCreatedAtDesc();
}
