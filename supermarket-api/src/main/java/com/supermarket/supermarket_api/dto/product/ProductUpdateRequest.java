package com.supermarket.supermarket_api.dto.product;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductUpdateRequest(

        @NotNull(message = "Product price is required")
        @Positive(message = "Product price must be positive")
        @Digits(integer = 10, fraction = 2, message = "Price must have up to 2 decimal places")
        BigDecimal price
) {
}
