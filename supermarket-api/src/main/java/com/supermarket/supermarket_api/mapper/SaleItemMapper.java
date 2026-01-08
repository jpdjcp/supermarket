package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.dto.SaleItemResponse;
import com.supermarket.supermarket_api.model.SaleItem;
import org.springframework.stereotype.Component;

@Component
public class SaleItemMapper {

    public SaleItemResponse toResponse(SaleItem saleItem) {
        return new SaleItemResponse(
                saleItem.getId(),
                saleItem.getSale().getId(),
                saleItem.getProduct().getId(),
                saleItem.getQuantity(),
                saleItem.getSubtotal());
    }
}