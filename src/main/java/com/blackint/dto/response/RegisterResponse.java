package com.blackint.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private String id;
    private String fullName;
    private String email;
    private String role;
}
