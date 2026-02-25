package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.product.ProductCreateRequest;
import com.supermarket.supermarket_api.dto.product.ProductResponse;
import com.supermarket.supermarket_api.dto.product.ProductUpdateRequest;
import com.supermarket.supermarket_api.model.Product;
import java.util.List;

public interface IProductService {
    ProductResponse create(ProductCreateRequest request);
    ProductResponse findById(Long productId);
    Product findRequiredById(Long productId);
    ProductResponse findBySku(String sku);
    List<ProductResponse> findAll();
    ProductResponse updatePrice(Long productId, ProductUpdateRequest request);
    void delete(Long productId);
}
