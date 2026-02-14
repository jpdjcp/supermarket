package com.supermarket.supermarket_api.dto.user;

import com.supermarket.supermarket_api.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank
        @Size(min = 2, max = 50)
        String username,

        @NotBlank
        @Size(min = 8, max = 100)
        String password,

        @NotNull
        UserRole role
) {
}
