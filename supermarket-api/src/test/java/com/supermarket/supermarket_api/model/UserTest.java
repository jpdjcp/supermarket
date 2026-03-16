package com.supermarket.supermarket_api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

public class UserTest {

    private User user;
    private String username;
    private String password;
    private UserRole role;

    @BeforeEach
    void setUp() {
        username = "John";
        password = "abcd1234";
        role = UserRole.ROLE_USER;
    }

    /* Constructor Tests */

    @Test
    void validUser_shouldBeCreated() {
        user = new User(username, password);

        assertThat(user.getId()).isNull();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getRole()).isEqualTo(role);
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void blankUsername_shouldThrow() {
        String blankUserName = "";

        assertThatThrownBy(()-> new User(blankUserName, password))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullUsername_shouldThrow() {
        assertThatThrownBy(()-> new User(null, password))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shortUsername_shouldThrow() {
        String shortUsername = "J";

        assertThatThrownBy(()-> new User(shortUsername, password))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void blankPassword_shouldThrow() {
        String blankPassword = "";

        assertThatThrownBy(()-> new User(username, blankPassword))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullPassword_shouldThrow() {
        assertThatThrownBy(()-> new User(username, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shortPassword_shouldThrow() {
        String shortPassword = "abcd123";

        assertThatThrownBy(()-> new User(username, shortPassword))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullRole_shouldThrow() {
        assertThatThrownBy(()-> new User(username, password))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /* Change Password */

    @Test
    void changePassword_shouldChangePassword() {
        String newPassword = "newPassword";
        user = new User(username, password);

        user.changePassword(newPassword);

        assertThat(user.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void changePassword_withShortPassword_shouldThrow() {
        String shortPassword = "abcd123";
        user = new User(username, password);

        assertThatThrownBy(()-> user.changePassword(shortPassword))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changePassword_withBlankPassword_shouldThrow() {
        String blankPassword = "";
        user = new User(username, password);

        assertThatThrownBy(()-> user.changePassword(blankPassword))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changePassword_withNullPassword_shouldThrow() {
        user = new User(username, password);

        assertThatThrownBy(()-> user.changePassword(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /* Enable & Disable */

    @Test
    void disable_shouldDisableUser() {
        user = new User(username, password);

        user.disable();

        assertThat(user.isEnabled()).isFalse();
    }
    @Test
    void enable_shouldEnableUser() {
        user = new User(username, password);
        user.disable();

        user.enable();

        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void setLastLogin_shouldSetLastLogin() {
        user = new User(username, password);
        Instant lastLogin = Instant.now();
        user.setLastLogin(lastLogin);

        assertThat(user.getLastLogin()).isEqualTo(lastLogin);
    }

    @Test
    void setLastLogin_withNullInstant_shouldThrow() {
        user = new User(username, password);

        assertThatThrownBy(()-> user.setLastLogin(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
