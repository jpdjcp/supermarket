package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.SaleItemResponse;
import com.supermarket.supermarket_api.model.SaleItem;
import org.springframework.stereotype.Component;

@Component
public class SaleItemMapper {

    public AddProductResponse toResponse(SaleItem saleItem) {
        return new AddProductResponse(
                saleItem.getSale().getId(),
                saleItem.getProduct().getId(),
                saleItem.getQuantity(),
                saleItem.getSubtotal());
    }

    public SaleItemResponse toSaleItemResponse(SaleItem item) {
        return new SaleItemResponse(
                item.getSale().getId(),
                item.getProduct().getId(),
                item.getQuantity(),
                item.getSubtotal());
    }
}