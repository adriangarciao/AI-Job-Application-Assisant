package com.adriangarciao.jobmatch.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Returns 403 Forbidden with a JSON body when an authenticated user lacks the role
 * required for a URL-level rule. Method-level {@code @PreAuthorize} denials are handled
 * by {@code GlobalExceptionHandler} with the same shape. Wired into {@code SecurityConfig}.
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public RestAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        SecurityErrorResponder.write(request, response, HttpStatus.FORBIDDEN,
                "Access denied. Your account does not have the required role for this resource.",
                objectMapper);
    }
}
