package com.adriangarciao.jobmatch.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Writes a small JSON error body for security-filter-level failures (missing token,
 * insufficient role) so the response shape matches {@code GlobalExceptionHandler.ApiError}
 * (status, error, message, timestamp) plus the request path. No stack traces or
 * internal details are leaked.
 */
final class SecurityErrorResponder {

    private SecurityErrorResponder() {}

    static void write(HttpServletRequest request,
                      HttpServletResponse response,
                      HttpStatus status,
                      String message,
                      ObjectMapper objectMapper) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getRequestURI());
        body.put("timestamp", Instant.now().toString());

        objectMapper.writeValue(response.getWriter(), body);
    }
}
