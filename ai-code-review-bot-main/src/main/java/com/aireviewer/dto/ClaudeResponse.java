package com.aireviewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Response received from Claude API
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaudeResponse {

    private String id;
    private String type;
    private String model;

    private List<ContentBlock> content;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentBlock {
        private String type;   // "text"
        private String text;
    }

    /**
     * Helper method to extract the text from the first content block
     */
    public String getReviewText() {
        if (content != null && !content.isEmpty()) {
            return content.get(0).getText();
        }
        return "No review generated.";
    }
}
