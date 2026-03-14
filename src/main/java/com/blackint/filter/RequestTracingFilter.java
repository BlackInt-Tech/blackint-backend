package com.blackint.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestTracingFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(RequestTracingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        request.setAttribute("requestId", requestId);

        log.info("Request started | id={} | method={} | uri={}",
                requestId,
                request.getMethod(),
                request.getRequestURI());

        filterChain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;

        log.info("Request completed | id={} | status={} | duration={} ms",
                requestId,
                response.getStatus(),
                duration);
    }
}