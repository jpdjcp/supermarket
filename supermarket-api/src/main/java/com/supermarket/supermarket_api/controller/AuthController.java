package com.supermarket.supermarket_api.controller;

import com.supermarket.supermarket_api.dto.auth.AuthResponse;
import com.supermarket.supermarket_api.dto.auth.LoginRequest;
import com.supermarket.supermarket_api.security.jwt.JwtService;
import com.supermarket.supermarket_api.security.model.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public AuthResponse login(LoginRequest login) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.username(),
                        login.password()
        ));

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token);
    }
}
