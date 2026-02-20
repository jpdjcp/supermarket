package com.supermarket.supermarket_api.dto.sale;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleCreateRequest(

        @NotNull(message = "Branch ID cannot be null")
        @Positive(message = "Branch ID must be positive")
        Long branchId,

        @NotNull(message = "User ID cannot be null")
        @Positive(message = "User ID must be positive")
        Long userId
) { }
