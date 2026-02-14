package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.user.CreateUserRequest;
import com.supermarket.supermarket_api.dto.user.ChangePasswordRequest;
import com.supermarket.supermarket_api.dto.user.UserResponse;

public interface IUserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse findById(Long userId);
    void changePassword(Long userId, ChangePasswordRequest request);
    void enable(Long userId);
    void disable(Long userId);
}
