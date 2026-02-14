package com.supermarket.supermarket_api.dto.user;

import com.supermarket.supermarket_api.model.UserRole;

import java.time.Instant;

public record UserResponse(
        Long id,
        String username,
        UserRole role,
        boolean enabled,
        Instant createdAt
) {
}
