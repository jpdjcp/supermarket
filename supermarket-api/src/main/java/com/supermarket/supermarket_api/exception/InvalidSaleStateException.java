package com.supermarket.supermarket_api.exception;

public class InvalidSaleStateException extends DomainException {
    public InvalidSaleStateException(String message) {
        super(message);
    }
}
