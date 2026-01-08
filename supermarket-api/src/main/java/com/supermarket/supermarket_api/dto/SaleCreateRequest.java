package com.supermarket.supermarket_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleCreateRequest(

        @NotNull(message = "Branch ID cannot be null")
        @Positive(message = "Branch ID must be positive")
        Long branchId
) { }
