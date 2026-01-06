package com.supermarket.supermarket_api.exception;

public class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
