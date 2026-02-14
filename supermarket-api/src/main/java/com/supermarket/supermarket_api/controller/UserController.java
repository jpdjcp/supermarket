package com.supermarket.supermarket_api.controller;

import com.supermarket.supermarket_api.dto.user.ChangePasswordRequest;
import com.supermarket.supermarket_api.dto.user.CreateUserRequest;
import com.supermarket.supermarket_api.dto.user.UserResponse;
import com.supermarket.supermarket_api.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse user = service.createUser(request);
        URI location = URI.create("/api/v1/users/" + user.id());
        return ResponseEntity.created(location).body(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> findById(@PathVariable @Positive Long userId) {
        return ResponseEntity.ok(service.findById(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long userId,
            @RequestBody @Valid ChangePasswordRequest request) {
        service.changePassword(userId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/enable")
    public ResponseEntity<Void> enable(@PathVariable @Positive Long userId) {
        service.enable(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/disable")
    public ResponseEntity<Void> disable(@PathVariable @Positive Long userId) {
        service.disable(userId);
        return ResponseEntity.noContent().build();
    }
}
