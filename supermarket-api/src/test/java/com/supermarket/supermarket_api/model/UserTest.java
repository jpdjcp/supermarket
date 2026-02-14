package com.supermarket.supermarket_api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        user = new User(username, password, role);

        assertThat(user.getId()).isNull();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getRole()).isEqualTo(role);
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void blankUsername_shouldThrow() {
        String blankUserName = "";

        assertThatThrownBy(()-> new User(blankUserName, password, role))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullUsername_shouldThrow() {
        assertThatThrownBy(()-> new User(null, password, role))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shortUsername_shouldThrow() {
        String shortUsername = "J";

        assertThatThrownBy(()-> new User(shortUsername, password, role))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void blankPassword_shouldThrow() {
        String blankPassword = "";

        assertThatThrownBy(()-> new User(username, blankPassword, role))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullPassword_shouldThrow() {
        assertThatThrownBy(()-> new User(username, null, role))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shortPassword_shouldThrow() {
        String shortPassword = "abcd123";

        assertThatThrownBy(()-> new User(username, shortPassword, role))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullRole_shouldThrow() {
        assertThatThrownBy(()-> new User(username, password, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /* Change Password */

    @Test
    void changePassword_shouldChangePassword() {
        String newPassword = "newPassword";
        user = new User(username, password, role);

        user.changePassword(newPassword);

        assertThat(user.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void changePassword_withShortPassword_shouldThrow() {
        String shortPassword = "abcd123";
        user = new User(username, password, role);

        assertThatThrownBy(()-> user.changePassword(shortPassword))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changePassword_withBlankPassword_shouldThrow() {
        String blankPassword = "";
        user = new User(username, password, role);

        assertThatThrownBy(()-> user.changePassword(blankPassword))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changePassword_withNullPassword_shouldThrow() {
        user = new User(username, password, role);

        assertThatThrownBy(()-> user.changePassword(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /* Enable & Disable */

    @Test
    void disable_shouldDisableUser() {
        user = new User(username, password, role);

        user.disable();

        assertThat(user.isEnabled()).isFalse();
    }
    @Test
    void enable_shouldEnableUser() {
        user = new User(username, password, role);
        user.disable();

        user.enable();

        assertThat(user.isEnabled()).isTrue();
    }

}
