package com.supermarket.supermarket_api.dto.sale;

import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductResponse;
import com.supermarket.supermarket_api.model.SaleStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Sale representation including lifecycle state")
public record SaleResponse(
        @Schema(example = "42")
        Long id,

        @Schema(description = "Current lifecycle status of the sale")
        Long branchId,

        SaleStatus status,
        List<AddProductResponse> items,
        BigDecimal total)
{ }
