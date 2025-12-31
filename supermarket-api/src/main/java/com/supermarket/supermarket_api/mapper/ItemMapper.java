package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.dto.ItemDTO;
import com.supermarket.supermarket_api.model.Item;
import com.supermarket.supermarket_api.model.Product;

public class ItemMapper {

    public static ItemDTO mapToDTO(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getSale().getId(),
                item.getProduct().getId(),
                item.getQuantity(),
                item.getSubtotal());
    }

    // dto -> model
    public static Item mapToItem(ItemDTO dto, Product product) {
        return new Item(
                dto.id(),
                null,
                product,
                dto.quantity(),
                dto.subtotal()
        );
    }
}
