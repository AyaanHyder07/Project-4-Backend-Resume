package com.resume.dashboard.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final Set<String> EXCLUDED_PATHS = new HashSet<>(Arrays.asList(
        "/health", "/metrics", "/actuator", "/favicon.ico"
    ));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();

        // Skip excluded paths
        if (EXCLUDED_PATHS.stream().anyMatch(path::contains)) {
            chain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);

        long startTime = System.currentTimeMillis();
        String method = httpRequest.getMethod();
        String queryString = httpRequest.getQueryString();
        String userId = extractUserId(httpRequest);

        try {
            // Log incoming request
            logIncomingRequest(method, path, queryString, wrappedRequest, userId);

            // Process the request
            chain.doFilter(wrappedRequest, wrappedResponse);

            // Log response
            long duration = System.currentTimeMillis() - startTime;
            logOutgoingResponse(method, path, httpResponse.getStatus(), duration, wrappedResponse, userId);

        } finally {
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logIncomingRequest(String method, String path, String queryString,
                                    ContentCachingRequestWrapper request, String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== INCOMING REQUEST ==========\n");
        sb.append("Timestamp: ").append(Instant.now()).append("\n");
        sb.append("Method: ").append(method).append("\n");
        sb.append("Path: ").append(path).append("\n");
        if (queryString != null && !queryString.isEmpty()) {
            sb.append("Query: ").append(queryString).append("\n");
        }
        sb.append("User ID: ").append(userId != null ? userId : "Anonymous").append("\n");

        // Log headers
        Enumeration<String> headers = request.getHeaderNames();
        if (headers.hasMoreElements()) {
            sb.append("Headers: ");
            while (headers.hasMoreElements()) {
                String headerName = headers.nextElement();
                if (!headerName.toLowerCase().contains("password") && !headerName.toLowerCase().contains("authorization")) {
                    sb.append("[").append(headerName).append(": ").append(request.getHeader(headerName)).append("] ");
                }
            }
            sb.append("\n");
        }

        // Log request body if present
        byte[] body = request.getContentAsByteArray();
        if (body.length > 0) {
            String bodyStr = new String(body, StandardCharsets.UTF_8);
            if (bodyStr.length() > 500) {
                sb.append("Body: ").append(bodyStr.substring(0, 500)).append("...[truncated]\n");
            } else {
                sb.append("Body: ").append(bodyStr).append("\n");
            }
        }

        sb.append("======================================\n");
        log.info(sb.toString());
    }

    private void logOutgoingResponse(String method, String path, int status, long duration,
                                     ContentCachingResponseWrapper response, String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== OUTGOING RESPONSE ==========\n");
        sb.append("Timestamp: ").append(Instant.now()).append("\n");
        sb.append("Method: ").append(method).append("\n");
        sb.append("Path: ").append(path).append("\n");
        sb.append("Status Code: ").append(status).append(" ").append(getStatusText(status)).append("\n");
        sb.append("Duration: ").append(duration).append("ms\n");
        sb.append("User ID: ").append(userId != null ? userId : "Anonymous").append("\n");

        // Log response body if present (only for small payloads)
        byte[] body = response.getContentAsByteArray();
        if (body.length > 0 && body.length < 1000) {
            String bodyStr = new String(body, StandardCharsets.UTF_8);
            if (!bodyStr.isEmpty()) {
                sb.append("Response Body: ").append(bodyStr).append("\n");
            }
        }

        if (status >= 400) {
            log.error(sb.toString());
        } else {
            log.info(sb.toString());
        }
        sb.append("======================================\n");
    }

    private String extractUserId(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return "JWT-User"; // In production, decode JWT to get actual user ID
            }
            Object userObj = request.getAttribute("userId");
            return userObj != null ? userObj.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getStatusText(int status) {
        return switch (status) {
            case 200 -> "OK";
            case 201 -> "CREATED";
            case 204 -> "NO CONTENT";
            case 400 -> "BAD REQUEST";
            case 401 -> "UNAUTHORIZED";
            case 403 -> "FORBIDDEN";
            case 404 -> "NOT FOUND";
            case 500 -> "INTERNAL SERVER ERROR";
            case 502 -> "BAD GATEWAY";
            case 503 -> "SERVICE UNAVAILABLE";
            default -> "UNKNOWN";
        };
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
