package com.supermarket.supermarket_api.dto.sale;

import com.supermarket.supermarket_api.model.SaleStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record SaleSummary(
        Long id,
        Long branchId,
        Long userId,
        Instant createdAt,
        Instant closedAt,
        SaleStatus status,
        BigDecimal total
) {
}
