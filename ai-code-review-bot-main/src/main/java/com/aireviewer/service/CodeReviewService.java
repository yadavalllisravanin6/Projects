package com.aireviewer.service;

import com.aireviewer.dto.GitHubWebhookPayload;
import com.aireviewer.model.PullReview;
import com.aireviewer.repository.PullReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeReviewService {

    private final GitHubService gitHubService;
    private final ClaudeAiService claudeAiService;
    private final PullReviewRepository reviewRepository;

    /**
     * Main entry point — triggered when GitHub sends a webhook event.
     * Runs asynchronously so the webhook endpoint responds immediately (GitHub requires < 10s response).
     *
     * @param payload The parsed GitHub webhook payload
     */
    @Async
    public void processPullRequest(GitHubWebhookPayload payload) {
        String repoFullName = payload.getRepository().getFullName();
        Integer prNumber    = payload.getPullRequest().getNumber();
        String prTitle      = payload.getPullRequest().getTitle();
        String prAuthor     = payload.getPullRequest().getUser().getLogin();

        log.info("Processing PR #{} '{}' in {}", prNumber, prTitle, repoFullName);

        // Save initial record to DB
        PullReview review = PullReview.builder()
                .repoFullName(repoFullName)
                .prNumber(prNumber)
                .prTitle(prTitle)
                .prAuthor(prAuthor)
                .status(PullReview.ReviewStatus.PENDING)
                .build();
        review = reviewRepository.save(review);

        try {
            // Step 1: Fetch the diff from GitHub
            String diff = gitHubService.fetchPullRequestDiff(repoFullName, prNumber);

            if (diff.isBlank()) {
                log.warn("Empty diff for PR #{}, skipping review.", prNumber);
                updateStatus(review, PullReview.ReviewStatus.FAILED, "Empty diff", "");
                return;
            }

            review.setDiffContent(diff);

            // Step 2: Send diff to Claude for review
            String aiReview = claudeAiService.reviewCode(prTitle, diff);

            // Step 3: Post the review back to GitHub as a PR comment
            gitHubService.postReviewComment(repoFullName, prNumber, aiReview);

            // Step 4: Save completed review to DB
            updateStatus(review, PullReview.ReviewStatus.COMPLETED, diff, aiReview);
            log.info("✅ Review completed for PR #{} in {}", prNumber, repoFullName);

        } catch (Exception e) {
            log.error("❌ Review failed for PR #{}: {}", prNumber, e.getMessage());
            updateStatus(review, PullReview.ReviewStatus.FAILED, review.getDiffContent(), "Error: " + e.getMessage());
        }
    }

    private void updateStatus(PullReview review, PullReview.ReviewStatus status, String diff, String comment) {
        review.setStatus(status);
        review.setDiffContent(diff);
        review.setReviewComment(comment);
        reviewRepository.save(review);
    }

    // ---- Methods used by the dashboard API ----

    public List<PullReview> getRecentReviews() {
        return reviewRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public List<PullReview> getReviewsByRepo(String repoFullName) {
        return reviewRepository.findByRepoFullNameOrderByCreatedAtDesc(repoFullName);
    }
}
