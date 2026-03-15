package com.supermarket.supermarket_api.controller;

import com.supermarket.supermarket_api.dto.auth.AuthResponse;
import com.supermarket.supermarket_api.dto.auth.LoginRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authManager;

    @PostMapping("/login")
    public AuthResponse login(LoginRequest login) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.username(),
                        login.password()
        ));

        return new AuthResponse("Authentication successful");
    }
}
