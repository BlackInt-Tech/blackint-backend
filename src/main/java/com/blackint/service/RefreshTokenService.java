package com.blackint.service;

import com.blackint.entity.AdminUser;
import com.blackint.entity.RefreshToken;
import com.blackint.repository.RefreshTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken createRefreshToken(AdminUser user, String token) {

        refreshTokenRepository.deleteByAdminUser_Id(user.getId());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .adminUser(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();

         return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken validateToken(String token) {

        RefreshToken stored = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (stored.getRevoked() || stored.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired or revoked");
        }

        return stored;
    }

    @Transactional
    public void revoke(String token) {
        RefreshToken stored = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);
    }
}
