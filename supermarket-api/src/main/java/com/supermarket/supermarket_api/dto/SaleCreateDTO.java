package com.supermarket.supermarket_api.dto;

import jakarta.validation.constraints.NotNull;

public record SaleCreateDTO(
        @NotNull
        Long branchId
) {
}
