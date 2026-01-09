package com.supermarket.supermarket_api.exception;

public class SaleItemNotFoundException extends DomainException {
    public SaleItemNotFoundException(Long itemId) {
        super("Item not found with id: " + itemId);
    }
}
