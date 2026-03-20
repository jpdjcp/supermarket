package com.supermarket.supermarket_api.exception;

public class SaleNotOpenException extends DomainException {


    public SaleNotOpenException() {
        super("Sale is not open");
    }
}
