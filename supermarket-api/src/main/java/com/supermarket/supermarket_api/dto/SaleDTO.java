package com.supermarket.supermarket_api.dto;

import java.math.BigDecimal;
import java.util.List;

public record SaleDTO(
        Long id,
        Long branchId,
        List<SaleItemDTO> items,
        BigDecimal total)
{ }
