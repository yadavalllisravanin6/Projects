package com.aireviewer.service;

import com.aireviewer.dto.ClaudeRequest;
import com.aireviewer.dto.ClaudeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaudeAiServiceTest {

    @Mock private WebClient claudeWebClient;
    @Mock private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock private WebClient.RequestBodySpec requestBodySpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private ClaudeAiService claudeAiService;

    @BeforeEach
    void setUp() {
        // Inject @Value fields manually in tests
        ReflectionTestUtils.setField(claudeAiService, "model",     "claude-sonnet-4-20250514");
        ReflectionTestUtils.setField(claudeAiService, "maxTokens", 1000);
    }

    @Test
    void reviewCode_shouldReturnClaudeResponse() {
        // Arrange - mock the whole WebClient chain
        ClaudeResponse mockResponse = new ClaudeResponse();
        ClaudeResponse.ContentBlock block = new ClaudeResponse.ContentBlock();
        block.setText("## Code Review\n\nLooks good! No major issues found.");
        mockResponse.setContent(List.of(block));

        when(claudeWebClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(ClaudeRequest.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ClaudeResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act
        String result = claudeAiService.reviewCode("Fix NPE in UserService", "- String x = null;\n+ String x = \"\";");

        // Assert
        assertThat(result).contains("Code Review");
        assertThat(result).contains("Looks good");
    }

    @Test
    void reviewCode_shouldHandleNullResponse() {
        when(claudeWebClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ClaudeResponse.class)).thenReturn(Mono.empty());

        String result = claudeAiService.reviewCode("Test PR", "some diff");

        assertThat(result).contains("failed");
    }

    @Test
    void reviewCode_shouldTruncateLongDiffs() {
        // Build a diff longer than 8000 chars
        String longDiff = "x".repeat(10000);

        when(claudeWebClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(ClaudeRequest.class))).thenAnswer(invocation -> {
            ClaudeRequest req = invocation.getArgument(0);
            // Verify the content was truncated (contains truncation note)
            assertThat(req.getMessages().get(0).getContent()).contains("truncated");
            return requestBodySpec;
        });
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ClaudeResponse.class)).thenReturn(Mono.empty());

        claudeAiService.reviewCode("Big PR", longDiff);
        verify(claudeWebClient).post();
    }
}
