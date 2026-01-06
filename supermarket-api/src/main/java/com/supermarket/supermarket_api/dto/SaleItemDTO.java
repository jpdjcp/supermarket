package com.supermarket.supermarket_api.dto;

import java.math.BigDecimal;

public record SaleItemDTO(
        Long id,
        Long saleId,
        Long productId,
        Integer quantity,
        BigDecimal subtotal
) { }
