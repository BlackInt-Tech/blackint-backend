package com.blackint.common;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
}
