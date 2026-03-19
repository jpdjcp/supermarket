package com.supermarket.supermarket_api.exception;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException(Long userId) {
        super("User not found with ID: " + userId);
    }

    public UserNotFoundException(String username) {
        super("User not found with username: " + username);
    }
}
