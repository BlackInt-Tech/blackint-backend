package com.blackint.common;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private int status;
    private LocalDateTime timestamp;

    // ================= SUCCESS =================
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Success")
                .data(data)
                .status(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ================= ERROR =================
    public static <T> ApiResponse<T> error(String message, int status) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ================= SUCCESS MESSAGE =================
    public static ApiResponse<Void> successMessage(String message) {
    return ApiResponse.<Void>builder()
            .success(true)
            .message(message)
            .data(null)
            .timestamp(LocalDateTime.now())
            .build();
}
}
