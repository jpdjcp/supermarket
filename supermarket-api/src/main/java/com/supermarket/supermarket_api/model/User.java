package com.supermarket.supermarket_api.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private static final int USERNAME_MIN = 2;
    private static final int USERNAME_MAX = 50;
    private static final int PASSWORD_MIN = 8;
    private static final int PASSWORD_MAX = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private boolean enabled = true;
    private Instant lastLogin;

    public User(String username, String password, UserRole role) {
        onCreate(username, password, role);
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void changePassword(String password) {
        validatePassword(password);
        this.password = password;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public void setLastLogin(Instant lastLogin) {
        require(lastLogin != null, "Last login cannot be null");
        this.lastLogin = lastLogin;
    }

    @PrePersist
    private void setCreatedAt() {
        this.createdAt = Instant.now();
    }

    private void onCreate(String username, String password, UserRole role) {
        validateUser(username);
        validatePassword(password);
        require(role != null, "Role cannot be null");
    }

    /* Validations */
    private static void validateUser(String username) {
        require(username != null, "Username cannot be null");
        require(!username.isBlank(), "Username cannot be blank");
        require(username.length() >= USERNAME_MIN, "Username too short");
        require(username.length() <= USERNAME_MAX, "Username too long");
    }

    private static void validatePassword(String password) {
        require(password != null, "Password cannot be null");
        require(!password.isBlank(), "Password cannot be blank");
        require(password.length() >= PASSWORD_MIN, "Password too short");
        require(password.length() <= PASSWORD_MAX, "Password too long");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }
}
