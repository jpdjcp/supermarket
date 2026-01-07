package com.supermarket.supermarket_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddItemRequest(
        @NotNull(message = "Product ID is required")
        @Positive(message = "Product ID must be positive")
        Long productId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        @Max(value = 100, message = "Quantity cannot exceed 100")
        Integer quantity
) {
}
