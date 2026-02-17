package com.supermarket.supermarket_api.dto.product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        BigDecimal price
) {
}
