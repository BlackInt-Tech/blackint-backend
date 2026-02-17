package com.blackint.service;

import com.blackint.dto.request.AuthRequest;
import com.blackint.dto.request.RegisterRequest;
import com.blackint.dto.response.ApiResponse;
import com.blackint.dto.response.AuthResponse;
import com.blackint.dto.response.RegisterResponse;
import com.blackint.entity.AdminUser;
import com.blackint.entity.RefreshToken;
import com.blackint.repository.AdminUserRepository;
import com.blackint.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminUserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    // LOGIN
    public ApiResponse<AuthResponse> login(AuthRequest request) {

        AdminUser user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        refreshTokenService.createRefreshToken(user, refreshToken);

        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // REGISTER
    public ApiResponse<RegisterResponse> register(RegisterRequest request) {

        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Admin already exists");
        }

        AdminUser admin = AdminUser.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(encoder.encode(request.getPassword()))
                .role("ROLE_ADMIN")
                .isActive(true)
                .build();

        repository.save(admin);

        RegisterResponse response = RegisterResponse.builder()
                .id(admin.getId().toString())
                .fullName(admin.getFullName())
                .email(admin.getEmail())
                .role(admin.getRole())
                .build();

        return ApiResponse.<RegisterResponse>builder()
                .success(true)
                .message("Admin registered successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // REFRESH TOKEN
    public ApiResponse<AuthResponse> refresh(String refreshToken) {

        RefreshToken storedToken = refreshTokenService.validateToken(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(
                storedToken.getAdminUser().getEmail()
        );

        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Access token refreshed")
                .data(AuthResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // LOGOUT
    public ApiResponse<Void> logout(String refreshToken) {

        refreshTokenService.revoke(refreshToken);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Logged out successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
