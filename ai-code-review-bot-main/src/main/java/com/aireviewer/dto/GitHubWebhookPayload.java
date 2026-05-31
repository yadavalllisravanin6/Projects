package com.aireviewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents the JSON payload GitHub sends to your webhook
 * when a Pull Request is opened or updated.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubWebhookPayload {

    private String action;           // "opened", "synchronize", "closed"

    @JsonProperty("pull_request")
    private PullRequestInfo pullRequest;

    private RepositoryInfo repository;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PullRequestInfo {
        private Integer number;
        private String title;
        private String state;

        @JsonProperty("diff_url")
        private String diffUrl;

        @JsonProperty("html_url")
        private String htmlUrl;

        private UserInfo user;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class UserInfo {
            private String login;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RepositoryInfo {
        @JsonProperty("full_name")
        private String fullName;      // "owner/repo-name"

        private String name;
    }
}
