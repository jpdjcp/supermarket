package com.supermarket.supermarket_api.dto;

public record SaleItemDTO(
        Long id,
        Long saleId,
        Long productId,
        Integer quantity,
        Double subtotal
) { }
