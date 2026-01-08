package com.supermarket.supermarket_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RemoveItemRequest(

        @NotNull(message = "Sale ID is required")
        @Positive(message = "Sale ID must positive")
        Long saleId,

        @NotNull(message = "Product ID is required")
        @Positive(message = "Product ID must be positive")
        Long productId
) {
}
