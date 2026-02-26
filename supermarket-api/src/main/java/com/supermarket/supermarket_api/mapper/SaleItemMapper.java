package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.dto.sale.saleItem.ItemResponse;
import com.supermarket.supermarket_api.model.SaleItem;
import org.springframework.stereotype.Component;

@Component
public class SaleItemMapper {

    public ItemResponse toResponse(SaleItem saleItem) {
        return new ItemResponse(
                saleItem.getSale().getId(),
                saleItem.getProduct().getId(),
                saleItem.getQuantity(),
                saleItem.getSubtotal());
    }

    public ItemResponse toSaleItemResponse(SaleItem item) {
        return new ItemResponse(
                item.getSale().getId(),
                item.getProduct().getId(),
                item.getQuantity(),
                item.getSubtotal());
    }
}