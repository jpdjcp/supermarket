package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.user.CreateUserRequest;
import com.supermarket.supermarket_api.dto.user.ChangePasswordRequest;
import com.supermarket.supermarket_api.dto.user.UserResponse;
import com.supermarket.supermarket_api.exception.UserNotFoundException;
import com.supermarket.supermarket_api.mapper.UserMapper;
import com.supermarket.supermarket_api.model.User;
import com.supermarket.supermarket_api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        require(request != null, "Create User Request cannot be null");
        if (repository.existsByUsername(request.username()))
            throw new IllegalArgumentException("Username already exists");

        User user = new User(request.username(), request.password(), request.role());
        User saved = repository.save(user);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long userId) {
        require(userId != null, "User ID cannot be null");

        User user = repository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));
        return mapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findRequiredById(Long userId) {
        require(userId != null, "User ID cannot be null");

        return repository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findInactiveSince(Instant threshold) {
        require(threshold != null, "Threshold instant cannot be null");

        List<User> result = repository.findInactiveSince(threshold);
        return result.stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findByLastLoginBetween(Instant from, Instant to) {
        require(from != null, "From threshold cannot be null");
        require(to != null, "To threshold cannot be null");
        require(from.isBefore(to), "From must be before to");

        List<User> result = repository.findByLastLoginBetween(from, to);
        return result.stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public void updateLastLogin(Long userId) {
        require(userId != null, "User ID cannot be null");

        User user = repository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));
        user.setLastLogin(Instant.now());
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        require(userId != null, "User ID cannot be null");
        require(request != null, "Change Password Request cannot be null");

        User user = repository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));
        user.changePassword(request.password());
    }

    @Override
    @Transactional
    public void enable(Long userId) {
        require(userId != null, "User ID cannot be null");

        User user = repository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));
        user.enable();
    }

    @Override
    @Transactional
    public void disable(Long userId) {
        require(userId != null, "User ID cannot be null");

        User user = repository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));
        user.disable();
    }

    private void require(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }
}
