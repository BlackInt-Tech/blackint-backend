package com.blackint.service;

import com.blackint.entity.AdminUser;
import com.blackint.entity.RefreshToken;
import com.blackint.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshToken createRefreshToken(AdminUser user, String token) {

        repository.deleteByAdminUser_Id(user.getId()); // one active token per user

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .adminUser(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();

        return repository.save(refreshToken);
    }

    public RefreshToken validateToken(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getRevoked() ||
            refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired or revoked");
        }

        return refreshToken;
    }

    public void revoke(String token) {
        repository.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            repository.save(rt);
        });
    }
}
