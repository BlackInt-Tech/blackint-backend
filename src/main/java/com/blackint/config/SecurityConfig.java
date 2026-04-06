package com.blackint.config;

import com.blackint.security.JwtAuthFilter;
import com.blackint.security.JwtAuthenticationEntryPoint;
import com.blackint.security.RateLimitFilter;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final RateLimitFilter rateLimitFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ================= SECURITY FILTER CHAIN =================
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

            // Stateless JWT API
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())

            .exceptionHandling(exception ->
                    exception.authenticationEntryPoint(authenticationEntryPoint)
            )

            // ================= AUTHORIZATION =================
            .authorizeHttpRequests(auth -> auth

                    // ===== AUTH APIs (IMPORTANT FIX) =====
                    .requestMatchers("/api/auth/**", "/api/v1/auth/**").permitAll()

                    // ===== HEALTH =====
                    .requestMatchers("/", "/health").permitAll()

                    // ===== SWAGGER =====
                    .requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html"
                    ).permitAll()

                    // ===== STATIC =====
                    .requestMatchers(
                            "/images/**",
                            "/css/**",
                            "/js/**"
                    ).permitAll()

                    // ===== CONTACT PUBLIC =====
                    .requestMatchers("/api/contacts/**", "/api/v1/contacts/**").permitAll()

                    // ===== BLOG PUBLIC =====
                    .requestMatchers(HttpMethod.GET, "/api/blogs/**", "/api/v1/blogs/**").permitAll()

                    // ===== PROJECT PUBLIC =====
                    .requestMatchers(HttpMethod.GET, "/api/projects/**", "/api/v1/projects/**").permitAll()

                    // ===== OFFERINGS PUBLIC =====
                    .requestMatchers(HttpMethod.GET, "/api/offerings/**", "/api/v1/offerings/**").permitAll()

                    // ===== HOMEPAGE PUBLIC =====
                    .requestMatchers(HttpMethod.GET, "/api/homepage-data", "/api/v1/homepage-data").permitAll()

                    // ===== BLOG ADMIN =====
                    .requestMatchers(HttpMethod.POST, "/api/blogs/**", "/api/v1/blogs/**").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/blogs/**", "/api/v1/blogs/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/blogs/**", "/api/v1/blogs/**").authenticated()

                    // ===== PROJECT ADMIN =====
                    .requestMatchers(HttpMethod.POST, "/api/projects/**", "/api/v1/projects/**").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/projects/**", "/api/v1/projects/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/projects/**", "/api/v1/projects/**").authenticated()

                    // ===== OFFERINGS ADMIN =====
                    .requestMatchers(HttpMethod.POST, "/api/offerings/**", "/api/v1/offerings/**").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/offerings/**", "/api/v1/offerings/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/offerings/**", "/api/v1/offerings/**").authenticated()

                    // ===== EVERYTHING ELSE =====
                    .anyRequest().authenticated()
            )

            // ===== FILTERS =====
            .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ================= GLOBAL CORS CONFIG =================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "https://*.vercel.app",
                "https://*.onrender.com",
                "https://blackint.in",
                "https://*.blackint.in"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS",
                "PATCH"
        ));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // ================= STATIC RESOURCE IGNORE =================
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/images/**",
                "/css/**",
                "/js/**"
        );
    }
}