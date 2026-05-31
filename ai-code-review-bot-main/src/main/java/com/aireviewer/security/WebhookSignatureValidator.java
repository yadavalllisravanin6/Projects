package com.aireviewer.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Validates the HMAC-SHA256 signature that GitHub attaches to every webhook.
 *
 * GitHub signs every webhook with your secret (set in application.properties)
 * using HMAC-SHA256 and sends the signature in the X-Hub-Signature-256 header.
 *
 * This validator makes sure the request actually came from GitHub
 * and wasn't forged by someone who found your webhook URL.
 *
 * HOW TO USE in WebhookController:
 *
 *   @PostMapping("/github")
 *   public ResponseEntity<?> handleWebhook(
 *       @RequestHeader("X-Hub-Signature-256") String signature,
 *       @RequestBody String rawBody,
 *       ...) {
 *
 *       if (!signatureValidator.isValid(rawBody, signature)) {
 *           return ResponseEntity.status(403).body("Invalid signature");
 *       }
 *       ...
 *   }
 */
@Slf4j
@Component
public class WebhookSignatureValidator {

    @Value("${github.webhook.secret}")
    private String webhookSecret;

    /**
     * Returns true if the request body matches the GitHub signature.
     *
     * @param payload   raw request body (the JSON string)
     * @param signature value from X-Hub-Signature-256 header (format: "sha256=abc123...")
     */
    public boolean isValid(String payload, String signature) {
        if (signature == null || !signature.startsWith("sha256=")) {
            log.warn("Missing or malformed webhook signature");
            return false;
        }

        try {
            String expectedSig = "sha256=" + computeHmacSha256(payload, webhookSecret);
            boolean valid = constantTimeEquals(expectedSig, signature);
            if (!valid) {
                log.warn("Webhook signature mismatch. Request may be forged.");
            }
            return valid;
        } catch (Exception e) {
            log.error("Error validating webhook signature: {}", e.getMessage());
            return false;
        }
    }

    private String computeHmacSha256(String data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {

        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(
                key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(keySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Convert bytes to hex string
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    /**
     * Constant-time string comparison to prevent timing attacks.
     */
    private boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
