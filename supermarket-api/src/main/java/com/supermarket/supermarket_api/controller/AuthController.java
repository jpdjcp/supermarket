package com.supermarket.supermarket_api.controller;

import com.supermarket.supermarket_api.dto.auth.AuthResponse;
import com.supermarket.supermarket_api.dto.auth.LoginRequest;
import com.supermarket.supermarket_api.dto.auth.SignupRequest;
import com.supermarket.supermarket_api.dto.user.UserResponse;
import com.supermarket.supermarket_api.security.jwt.JwtService;
import com.supermarket.supermarket_api.security.model.CustomUserDetails;
import com.supermarket.supermarket_api.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest login) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.username(),
                        login.password()
        ));

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        AuthResponse response = new AuthResponse(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody @Valid SignupRequest signup) {
        UserResponse response = userService.createUser(signup);
        return ResponseEntity.created(
                URI.create("/api/v1/users/" + response.id()))
                .body(response);
    }
}
