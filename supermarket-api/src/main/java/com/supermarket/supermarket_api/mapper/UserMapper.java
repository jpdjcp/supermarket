package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.dto.user.UserResponse;
import com.supermarket.supermarket_api.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt() != null ? user.getCreatedAt().toInstant() : null
        );
    }
}
