package com.blackint.config;

import com.blackint.security.JwtAuthFilter;
import com.blackint.security.RateLimitFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final RateLimitFilter rateLimitFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF for REST APIs
                .csrf(csrf -> csrf.disable())

                // Enable CORS (for frontend apps)
                .cors(Customizer.withDefaults())

                // Stateless session (JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Authorization Rules
                .authorizeHttpRequests(auth -> auth

                        // ================= AUTH =================
                        .requestMatchers("/api/auth/**").permitAll()

                        // ================= SWAGGER =================
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // ================= PUBLIC CONTACT =================
                        .requestMatchers(HttpMethod.POST, "/api/contact").permitAll()

                        // ================= PUBLIC PROJECTS =================
                        .requestMatchers(HttpMethod.GET, "/api/projects/published").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/projects/*").permitAll()

                        // ================= PUBLIC SERVICES =================
                        .requestMatchers(HttpMethod.GET, "/api/services/published").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/services/*").permitAll()

                        // ================= ANY OTHER =================
                        .anyRequest().authenticated()
                )

                // Rate Limit FIRST
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)

                // JWT Filter AFTER rate limit
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
