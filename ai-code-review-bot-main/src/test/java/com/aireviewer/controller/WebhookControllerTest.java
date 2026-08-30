package com.aireviewer.controller;

import com.aireviewer.security.WebhookSignatureValidator;
import com.aireviewer.service.CodeReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebhookController.class)
class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private CodeReviewService codeReviewService;
    @MockBean private WebhookSignatureValidator signatureValidator;

    private static final String PR_OPENED_PAYLOAD = """
            {
              "action": "opened",
              "pull_request": {
                "number": 42,
                "title": "Add user authentication",
                "state": "open",
                "diff_url": "https://github.com/owner/repo/pull/42.diff",
                "html_url": "https://github.com/owner/repo/pull/42",
                "user": { "login": "sandeepreddythippareddy" }
              },
              "repository": {
                "full_name": "sandeepreddythippareddy/test-repo",
                "name": "test-repo"
              }
            }
            """;

    @Test
    void healthEndpoint_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/webhook/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void webhook_validPrOpened_shouldAccept() throws Exception {
        when(signatureValidator.isValid(any(), any())).thenReturn(true);

        mockMvc.perform(post("/api/webhook/github")
                        .header("X-GitHub-Event", "pull_request")
                        .header("X-Hub-Signature-256", "sha256=test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PR_OPENED_PAYLOAD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("accepted"));

        verify(codeReviewService, times(1)).processPullRequest(any());
    }

    @Test
    void webhook_nonPrEvent_shouldBeIgnored() throws Exception {
        when(signatureValidator.isValid(any(), any())).thenReturn(true);

        mockMvc.perform(post("/api/webhook/github")
                        .header("X-GitHub-Event", "push")
                        .header("X-Hub-Signature-256", "sha256=test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ignored"));

        verify(codeReviewService, never()).processPullRequest(any());
    }

    @Test
    void webhook_closedPr_shouldBeIgnored() throws Exception {
        when(signatureValidator.isValid(any(), any())).thenReturn(true);

        String closedPayload = PR_OPENED_PAYLOAD.replace("\"opened\"", "\"closed\"");

        mockMvc.perform(post("/api/webhook/github")
                        .header("X-GitHub-Event", "pull_request")
                        .header("X-Hub-Signature-256", "sha256=test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(closedPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ignored"));
    }

    @Test
    void webhook_invalidSignature_shouldReturn403() throws Exception {
        when(signatureValidator.isValid(any(), any())).thenReturn(false);

        mockMvc.perform(post("/api/webhook/github")
                        .header("X-GitHub-Event", "pull_request")
                        .header("X-Hub-Signature-256", "sha256=wrong")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PR_OPENED_PAYLOAD))
                .andExpect(status().isForbidden());
    }
}
