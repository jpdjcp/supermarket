package com.supermarket.supermarket_api.dto.sale.saleItem;

import java.math.BigDecimal;

public record AddProductResponse(
        Long saleId,
        Long productId,
        Integer quantity,
        BigDecimal subtotal
) { }
