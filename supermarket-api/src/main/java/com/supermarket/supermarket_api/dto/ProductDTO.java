package com.supermarket.supermarket_api.dto;

import java.math.BigDecimal;

public record ProductDTO(
        Long id,
        String name,
        BigDecimal price
) { }
