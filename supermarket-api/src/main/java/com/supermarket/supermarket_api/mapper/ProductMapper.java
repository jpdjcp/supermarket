package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.model.Product;
import com.supermarket.supermarket_api.dto.ProductDTO;

public class ProductMapper {

    public static ProductDTO mapToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }

    public static Product mapToProduct(ProductDTO dto) {
        return new Product(
                dto.id(),
                dto.name(),
                dto.price()
        );
    }
}
