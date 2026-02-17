package com.blackint.exception;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {

    private boolean success;
    private String message;
    private String error;
    private int status;

    private String path;
    private LocalDateTime timestamp;
}
