package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.user.CreateUserRequest;
import com.supermarket.supermarket_api.dto.user.ChangePasswordRequest;
import com.supermarket.supermarket_api.dto.user.UserResponse;
import com.supermarket.supermarket_api.model.User;

import java.time.Instant;
import java.util.List;

public interface IUserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse findById(Long userId);
    User findRequiredById(Long userId);
    List<UserResponse> findInactiveSince(Instant threshold);
    List<UserResponse> findByLastLoginBetween(Instant from, Instant to);
    void updateLastLogin(Long userId);
    void changePassword(Long userId, ChangePasswordRequest request);
    void enable(Long userId);
    void disable(Long userId);
    void ensureExists(Long userId);
}
