package com.adriangarciao.jobmatch.integration;

import com.adriangarciao.jobmatch.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies the security error semantics end-to-end with the real filter chain enabled:
 * a missing token yields 401 (not an empty 403), a valid token with an insufficient
 * role yields 403, and both carry a JSON body. Also confirms the generated OpenAPI spec
 * exposes the bearer scheme and a servers list.
 */
@SpringBootTest
@AutoConfigureMockMvc // filters ENABLED (unlike the addFilters=false controller slice tests)
class SecurityErrorResponseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Test
    void protectedRoute_withNoToken_returns401WithJsonBody() throws Exception {
        mockMvc.perform(get("/api/resumes"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value("/api/resumes"));
    }

    @Test
    void protectedRoute_withInvalidToken_returns401WithJsonBody() throws Exception {
        mockMvc.perform(get("/api/resumes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer not-a-real-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"));
    }

    @Test
    void adminRoute_withValidUserRoleToken_returns403WithJsonBody() throws Exception {
        // A normal USER token: authenticates fine, but lacks ADMIN for /api/admin/ping.
        String userToken = jwtService.generateToken(42L, "demo.user@example.com", "USER");

        mockMvc.perform(get("/api/admin/ping")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Forbidden"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void openApiSpec_exposesBearerSchemeAndServers() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.type").value("http"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme").value("bearer"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.bearerFormat").value("JWT"))
                .andExpect(jsonPath("$.servers").isArray())
                .andExpect(jsonPath("$.servers").isNotEmpty());
    }
}
