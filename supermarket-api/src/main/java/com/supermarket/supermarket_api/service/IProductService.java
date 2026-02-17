package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.product.ProductCreateRequest;
import com.supermarket.supermarket_api.dto.product.ProductResponse;
import com.supermarket.supermarket_api.dto.product.ProductUpdateRequest;
import com.supermarket.supermarket_api.model.Product;

import java.util.List;

public interface IProductService {
    ProductResponse create(ProductCreateRequest request);
    ProductResponse findById(Long id);
    Product findRequiredById(Long id);
    ProductResponse findBySKU(String sku);
    List<ProductResponse> findAll();
    ProductResponse updatePrice(Long id, ProductUpdateRequest request);
    void delete(Long id);
}
