package com.supermarket.supermarket_api.dto.sale;

import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductResponse;

import java.math.BigDecimal;
import java.util.List;

public record SaleResponse(
        Long id,
        Long branchId,
        List<AddProductResponse> items,
        BigDecimal total)
{ }
