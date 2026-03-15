package com.blackint.security;

import com.blackint.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 10;
    private static final long TIME_WINDOW = 60_000; // 1 minute

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ Skip Swagger and docs
        if (path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")) {

            filterChain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Apply rate limit to all POST api
        String method = request.getMethod();

        if (!method.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = request.getRemoteAddr();

        requestCounts.putIfAbsent(ip, new RequestCounter());

        RequestCounter counter = requestCounts.get(ip);

        synchronized (counter) {

            long currentTime = System.currentTimeMillis();

            if (currentTime - counter.timestamp > TIME_WINDOW) {
                counter.timestamp = currentTime;
                counter.count.set(0);
            }

            int requestCount = counter.count.incrementAndGet();

            if (requestCount > MAX_REQUESTS) {

                log.warn("Rate limit exceeded: ip={}, path={}", ip, path);
                response.setStatus(429);
                response.setContentType("application/json");

                ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                        .success(false)
                        .message("Too many requests. Please try again later.")
                        .status(429)
                        .timestamp(LocalDateTime.now())
                        .build();

                response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private static class RequestCounter {

        java.util.concurrent.atomic.AtomicInteger count =
                new java.util.concurrent.atomic.AtomicInteger(0);

        long timestamp = System.currentTimeMillis();
    }
}
