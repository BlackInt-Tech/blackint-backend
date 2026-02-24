package com.blackint.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class ContactRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email
    @Size(max = 150)
    private String email;

    @Pattern(
            regexp = "^[0-9+\\-()\\s]{7,20}$",
            message = "Invalid phone number format"
    )
    private String phone;

    @NotBlank(message = "Company is required")
    private String company;

    @NotEmpty(message = "Select at least one service")
    private List<String> services;

    @NotBlank(message = "Budget is required")
    private String budget;

    @NotBlank(message = "Project idea is required")
    @Size(min = 10, max = 3000)
    private String projectIdea;

    @NotBlank(message = "Message is required")
    @Size(min = 10, max = 2000)
    private String message;
}