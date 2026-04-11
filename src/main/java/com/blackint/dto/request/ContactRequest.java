package com.blackint.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

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


    @NotBlank(message = "Offering type is required")
    private String offeringType;

    @NotBlank(message = "Offering name is required")
    private String offeringName;

    @NotBlank(message = "Offering price is required")
    private String offeringPrice;

    @NotBlank(message = "Project idea is required")
    @Size(min = 10, max = 3000)
    private String projectIdea;

}