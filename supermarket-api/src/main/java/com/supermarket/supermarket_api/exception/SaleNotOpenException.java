package com.supermarket.supermarket_api.exception;

public class SaleNotOpenException extends DomainException {


    public SaleNotOpenException(String message) {
        super(message);
    }
}
