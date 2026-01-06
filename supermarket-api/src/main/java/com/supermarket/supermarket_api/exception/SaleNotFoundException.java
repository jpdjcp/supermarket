package com.supermarket.supermarket_api.exception;

public class SaleNotFoundException extends DomainException {

    public SaleNotFoundException(Long saleId) {
        super("Sale not found with id: " + saleId);
    }
}
