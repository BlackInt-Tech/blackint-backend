package com.blackint.config;

import com.blackint.security.JwtAuthFilter;
import com.blackint.security.RateLimitFilter;

import lombok.RequiredArgsConstructor;

import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


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

                 .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                 .csrf(csrf -> csrf.disable())
                // Authorization Rules
                .authorizeHttpRequests(auth -> auth

                        // ================= AUTH =================
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/", "/health").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
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

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "https://blackint-frontend.vercel.app",
                "https://blackint-frontend-git-develop-blackints-projects.vercel.app"
        ));

        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
        }
}
