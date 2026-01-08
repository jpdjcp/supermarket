package com.supermarket.supermarket_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ChangeQuantityRequest(

        @NotNull(message = "Sale ID is required")
        @Positive(message = "Sale ID must positive")
        Long saleId,

        @NotNull(message = "Product ID is required")
        @Positive(message = "Product ID must be positive")
        Long productId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        @Max(value = 100, message = "Quantity cannot exceed 100")
        Integer quantity) {
}
