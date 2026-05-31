package com.aireviewer.controller;

import com.aireviewer.dto.GitHubWebhookPayload;
import com.aireviewer.security.WebhookSignatureValidator;
import com.aireviewer.service.CodeReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Receives GitHub webhook events for Pull Requests.
 *
 * SETUP IN GITHUB:
 * 1. Repo → Settings → Webhooks → Add webhook
 * 2. Payload URL: https://your-app.railway.app/api/webhook/github
 * 3. Content type: application/json
 * 4. Secret: same value as github.webhook.secret in application.properties
 * 5. Events: select "Pull requests"
 */
@Slf4j
@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final CodeReviewService codeReviewService;
    private final WebhookSignatureValidator signatureValidator;

    /**
     * Main webhook endpoint. GitHub POSTs here when a PR event happens.
     * We validate the signature, then trigger async review processing.
     * Always responds within milliseconds — review runs in background.
     */
    @PostMapping("/github")
    public ResponseEntity<Map<String, String>> handleGitHubWebhook(
            @RequestHeader(value = "X-GitHub-Event",        defaultValue = "unknown") String eventType,
            @RequestHeader(value = "X-Hub-Signature-256",   defaultValue = "")        String signature,
            @RequestBody String rawBody) {

        log.info("Received GitHub event: '{}' ", eventType);

        // Security: verify the request actually came from GitHub
        if (!signature.isEmpty() && !signatureValidator.isValid(rawBody, signature)) {
            log.warn("Rejected webhook: invalid signature");
            return ResponseEntity.status(403)
                    .body(Map.of("error", "Invalid webhook signature"));
        }

        // Only process pull_request events
        if (!"pull_request".equals(eventType)) {
            return ResponseEntity.ok(Map.of("status", "ignored", "event", eventType));
        }

        // Parse the payload manually
        GitHubWebhookPayload payload;
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            payload = mapper.readValue(rawBody, GitHubWebhookPayload.class);
        } catch (Exception e) {
            log.error("Failed to parse webhook payload", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid payload"));
        }

        String action = payload.getAction();

        if ("opened".equals(action) || "synchronize".equals(action)) {
            log.info("Triggering AI review for PR #{} '{}' in {}",
                    payload.getPullRequest().getNumber(),
                    payload.getPullRequest().getTitle(),
                    payload.getRepository().getFullName());

            codeReviewService.processPullRequest(payload);

            return ResponseEntity.ok(Map.of(
                    "status",  "accepted",
                    "message", "AI review started for PR #" + payload.getPullRequest().getNumber()
            ));
        }

        return ResponseEntity.ok(Map.of("status", "ignored", "action", action));
    }

    /**
     * Health check — Railway/Render uses this to verify the app is alive.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status",  "UP",
                "service", "AI Code Review Bot"
        ));
    }
}
