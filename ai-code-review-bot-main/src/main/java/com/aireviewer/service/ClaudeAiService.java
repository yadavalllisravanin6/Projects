package com.aireviewer.service;

import com.aireviewer.dto.ClaudeRequest;
import com.aireviewer.dto.ClaudeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaudeAiService {

    @Qualifier("claudeWebClient")
    private final WebClient claudeWebClient;

    @Value("${claude.api.model}")
    private String model;

    @Value("${claude.api.max-tokens}")
    private Integer maxTokens;

    /**
     * Sends the code diff to Claude and gets a review back.
     *
     * @param prTitle   Title of the Pull Request
     * @param diffContent  The raw git diff from the PR
     * @return Claude's review as a formatted string
     */
    public String reviewCode(String prTitle, String diffContent) {
        log.info("Sending code diff to Claude AI for review...");

        String prompt = buildPrompt(prTitle, diffContent);

        ClaudeRequest request = ClaudeRequest.builder()
                .model(model)
                .maxTokens(maxTokens)
                .messages(List.of(
                        ClaudeRequest.Message.builder()
                                .role("user")
                                .content(prompt)
                                .build()
                ))
                .build();

        try {
            ClaudeResponse response = claudeWebClient
                    .post()
                    .uri("/v1/messages")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ClaudeResponse.class)
                    .block();

            if (response == null) {
                log.error("Received null response from Claude API");
                return "❌ AI review failed: no response received.";
            }

            log.info("Claude review received successfully.");
            return response.getReviewText();

        } catch (Exception e) {
            log.error("Error calling Claude API: {}", e.getMessage());
            return "❌ AI review failed: " + e.getMessage();
        }
    }

    /**
     * Builds the prompt sent to Claude.
     * This is the most important part — the better your prompt, the better the review.
     */
    private String buildPrompt(String prTitle, String diffContent) {
        // Limit diff size so we don't exceed Claude's context window
        String trimmedDiff = diffContent.length() > 8000
                ? diffContent.substring(0, 8000) + "\n\n[... diff truncated for length ...]"
                : diffContent;

        return """
                You are an expert Java code reviewer. Analyze the following Pull Request diff and provide a thorough code review.

                PR Title: %s

                Code Diff:
                ```diff
                %s
                ```

                Please review the code and provide feedback covering:

                1. **Code Quality** - Is the code clean, readable, and well-structured?
                2. **Bugs & Issues** - Are there any bugs, null pointer risks, or logic errors?
                3. **Performance** - Are there any performance concerns?
                4. **Security** - Are there any security vulnerabilities (SQL injection, hardcoded secrets, etc.)?
                5. **Best Practices** - Does it follow Java/Spring Boot best practices?
                6. **Suggestions** - What specific improvements would you recommend?

                Format your response in Markdown so it renders nicely in GitHub.
                Be specific, concise, and constructive. If the code looks good, say so!
                """.formatted(prTitle, trimmedDiff);
    }
}
