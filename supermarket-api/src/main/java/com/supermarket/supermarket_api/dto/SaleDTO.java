package com.supermarket.supermarket_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record SaleDTO(
        Long id,

        @NotNull(message = "Branch ID cannot be null")
        @Positive(message = "Branch ID must be positive")
        Long branchId,

        @NotNull(message = "Sale's items are required")
        List<SaleItemDTO> items,

        @NotNull(message = "Total cannot be null")
        @Positive(message = "Total must be positive")
        BigDecimal total)
{ }
