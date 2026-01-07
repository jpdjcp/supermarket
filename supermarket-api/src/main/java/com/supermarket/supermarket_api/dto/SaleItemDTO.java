package com.supermarket.supermarket_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SaleItemDTO(
        @NotNull(message = "Sale item ID cannot be null")
        @Positive(message = "Sale item ID must be positive")
        Long id,

        @NotNull(message = "Sale ID cannot be null")
        @Positive(message = "Sale ID must be positive")
        Long saleId,

        @NotNull(message = "Product ID cannot be null")
        @Positive(message = "Product ID must be positive")
        Long productId,

        @NotNull(message = "Quantity cannot be null")
        @Positive(message = "Quantity must be positive")
        @Max(value = 100, message = "Quantity cannot exceed 100")
        Integer quantity,

        @NotNull(message = "Subtotal cannot be null")
        @Positive(message = "Subtotal must be positive")
        BigDecimal subtotal
) { }
