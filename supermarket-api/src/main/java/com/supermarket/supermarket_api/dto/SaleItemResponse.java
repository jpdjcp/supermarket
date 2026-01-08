package com.supermarket.supermarket_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SaleItemResponse(
        Long id,
        Long saleId,
        Long productId,
        Integer quantity,
        BigDecimal subtotal
) { }
