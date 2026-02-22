package com.blackint.service;

import com.blackint.dto.request.AuthRequest;
import com.blackint.dto.request.RegisterRequest;
import com.blackint.dto.response.AuthResponse;
import com.blackint.dto.response.RegisterResponse;
import com.blackint.entity.AdminUser;
import com.blackint.entity.RefreshToken;
import com.blackint.repository.AdminUserRepository;
import com.blackint.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminUserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse login(AuthRequest request) {

        AdminUser user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        refreshTokenService.createRefreshToken(user, refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public RegisterResponse register(RegisterRequest request) {

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

        return RegisterResponse.builder()
                .id(admin.getId().toString())
                .fullName(admin.getFullName())
                .email(admin.getEmail())
                .role(admin.getRole())
                .build();
    }

    public AuthResponse refresh(String refreshToken) {

        RefreshToken storedToken = refreshTokenService.validateToken(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(
                storedToken.getAdminUser().getEmail()
        );

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(String refreshToken) {
        refreshTokenService.revoke(refreshToken);
    }

    public void delete(String refreshToken) {
        
        
    }
}