package com.supermarket.supermarket_api.exception;

public class ProductNotFoundException extends DomainException {

    public ProductNotFoundException(Long idProduct) {
        super("Product not found with id: " + idProduct);
    }
}
