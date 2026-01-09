package com.supermarket.supermarket_api.mapper;

import com.supermarket.supermarket_api.dto.product.ProductCreateRequest;
import com.supermarket.supermarket_api.dto.product.ProductResponse;
import com.supermarket.supermarket_api.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }

    public Product toProduct(ProductCreateRequest dto) {
        return new Product(
                dto.name(),
                dto.price()
        );
    }
}
