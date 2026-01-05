package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.dto.SaleItemDTO;
import com.supermarket.supermarket_api.model.SaleItem;
import com.supermarket.supermarket_api.model.Product;

public class ItemMapper {

    public static SaleItemDTO mapToDTO(SaleItem saleItem) {
        return new SaleItemDTO(
                saleItem.getId(),
                saleItem.getSale().getId(),
                saleItem.getProduct().getId(),
                saleItem.getQuantity(),
                saleItem.getSubtotal());
    }

    // dto -> model
    public static SaleItem mapToItem(SaleItemDTO dto, Product product) {
        return new SaleItem(
                dto.id(),
                null,
                product,
                dto.quantity(),
                dto.subtotal()
        );
    }
}
