package com.blackint.controller;

import com.blackint.common.ApiResponse;
import com.blackint.dto.request.AuthRequest;
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

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ApiResponse.success(service.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(
            @Valid @RequestBody AuthRequest request) {

        return ApiResponse.success(service.login(request));
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(
            @RequestParam String refreshToken) {

        return ApiResponse.success(service.refresh(refreshToken));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @RequestParam String refreshToken) {

        service.logout(refreshToken);
        return ApiResponse.successMessage("Logged out successfully");
    }

    @DeleteMapping("/delete")
    public ApiResponse<Void> delete(
            @RequestParam String refreshToken){

        service.delete(refreshToken);
        return ApiResponse.successMessage("Deleted successfully");
    }

}