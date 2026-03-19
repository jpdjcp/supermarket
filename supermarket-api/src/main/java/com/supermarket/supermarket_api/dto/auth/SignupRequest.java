package com.supermarket.supermarket_api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank
        @Size(min = 2, max = 50)
        String username,

        @NotBlank
        @Size(min = 8, max = 100)
        String password
) {
}
