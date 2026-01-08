package com.supermarket.supermarket_api.dto;

import java.math.BigDecimal;
import java.util.List;

public record SaleResponse(
        Long id,
        Long branchId,
        List<SaleItemResponse> items,
        BigDecimal total)
{ }
