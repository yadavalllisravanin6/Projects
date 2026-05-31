package com.aireviewer.controller;

import com.aireviewer.model.PullReview;
import com.aireviewer.repository.PullReviewRepository;
import com.aireviewer.service.CodeReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for the React dashboard frontend.
 * All endpoints return JSON.
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {

    private final CodeReviewService codeReviewService;
    private final PullReviewRepository reviewRepository;

    /**
     * GET /api/reviews/recent
     * Returns the 10 most recent AI reviews across all repos.
     */
    @GetMapping("/recent")
    public ResponseEntity<List<PullReview>> getRecentReviews() {
        return ResponseEntity.ok(codeReviewService.getRecentReviews());
    }

    /**
     * GET /api/reviews/repo?name=owner/repo-name
     * Returns all reviews for one specific repository.
     */
    @GetMapping("/repo")
    public ResponseEntity<List<PullReview>> getReviewsByRepo(@RequestParam String name) {
        return ResponseEntity.ok(codeReviewService.getReviewsByRepo(name));
    }

    /**
     * GET /api/reviews/{id}
     * Returns a single review by its database ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PullReview> getReviewById(@PathVariable Long id) {
        return reviewRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/reviews/{id}
     * Deletes a review record (useful for cleanup during dev).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        if (!reviewRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reviewRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
