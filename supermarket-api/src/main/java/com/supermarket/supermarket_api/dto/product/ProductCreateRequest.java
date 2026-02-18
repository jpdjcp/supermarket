package com.supermarket.supermarket_api.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateRequest(

        @NotBlank
        @Pattern(regexp = "^[A-Z0-9-]{6,20}$", message = "SKU must be 6-20 chars, uppercase, numbers or hyphen")
        String sku,

        @NotBlank(message = "Product name cannot be blank")
        @Size(max = 50, message = "Product name must be at most 50 characters long")
        String name,

        @NotNull(message = "Product price is required")
        @Digits(integer = 10, fraction = 2, message = "Price must have up to 2 decimal places")
        @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
        BigDecimal price
) { }
