package com.aireviewer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubService {

    @Qualifier("githubWebClient")
    private final WebClient githubWebClient;

    /**
     * Fetches the raw git diff for a Pull Request.
     *
     * @param repoFullName  e.g. "sandeepreddythippareddy/my-project"
     * @param prNumber      e.g. 42
     * @return raw diff string
     */
    public String fetchPullRequestDiff(String repoFullName, Integer prNumber) {
        log.info("Fetching diff for PR #{} in {}", prNumber, repoFullName);

        try {
            // GitHub serves the diff when Accept header is set to application/vnd.github.diff
            String diff = githubWebClient
                    .get()
                    .uri("/repos/{repo}/pulls/{pr}", repoFullName, prNumber)
                    .header("Accept", "application/vnd.github.diff")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (diff == null || diff.isBlank()) {
                log.warn("Empty diff received for PR #{}", prNumber);
                return "";
            }

            log.info("Diff fetched successfully ({} characters)", diff.length());
            return diff;

        } catch (Exception e) {
            log.error("Failed to fetch PR diff: {}", e.getMessage());
            throw new RuntimeException("Could not fetch PR diff from GitHub: " + e.getMessage());
        }
    }

    /**
     * Posts a comment on a Pull Request with the AI review.
     *
     * @param repoFullName  e.g. "sandeepreddythippareddy/my-project"
     * @param prNumber      the PR number
     * @param comment       the review text (supports Markdown)
     */
    public void postReviewComment(String repoFullName, Integer prNumber, String comment) {
        log.info("Posting AI review comment on PR #{} in {}", prNumber, repoFullName);

        // Wrap the comment with a bot header so it's clearly from the bot
        String formattedComment = """
                ## 🤖 AI Code Review

                %s

                ---
                *Reviewed by AI Code Review Bot powered by Claude AI*
                """.formatted(comment);

        try {
            githubWebClient
                    .post()
                    .uri("/repos/{repo}/issues/{pr}/comments", repoFullName, prNumber)
                    .bodyValue(Map.of("body", formattedComment))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Review comment posted successfully on PR #{}", prNumber);

        } catch (Exception e) {
            log.error("Failed to post review comment: {}", e.getMessage());
            throw new RuntimeException("Could not post review comment to GitHub: " + e.getMessage());
        }
    }
}
