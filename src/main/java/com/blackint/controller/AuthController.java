package com.blackint.controller;

import com.blackint.common.ApiResponse;
import com.blackint.dto.request.AuthRequest;
import com.blackint.dto.request.RefreshTokenRequest;
import com.blackint.dto.request.RegisterRequest;
import com.blackint.dto.response.AuthResponse;
import com.blackint.dto.response.RegisterResponse;
import com.blackint.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    // REGISTER
    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return service.register(request);
    }

    // LOGIN
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(
            @Valid @RequestBody AuthRequest request) {

        return service.login(request);
    }

    // REFRESH ACCESS TOKEN
    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(
            @RequestParam RefreshTokenRequest request) {

        return service.refresh(request.getRefreshToken());
    }

    // LOGOUT
    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @RequestParam RefreshTokenRequest request) {

        return service.logout(request.getRefreshToken());
    }
}
