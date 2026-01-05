package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.dto.ItemDTO;
import com.supermarket.supermarket_api.model.ItemItem;
import com.supermarket.supermarket_api.model.Product;

public class ItemMapper {

    public static ItemDTO mapToDTO(ItemItem saleItem) {
        return new ItemDTO(
                saleItem.getId(),
                saleItem.getSale().getId(),
                saleItem.getProduct().getId(),
                saleItem.getQuantity(),
                saleItem.getSubtotal());
    }

    // dto -> model
    public static ItemItem mapToItem(ItemDTO dto, Product product) {
        return new ItemItem(
                dto.id(),
                null,
                product,
                dto.quantity(),
                dto.subtotal()
        );
    }
}
