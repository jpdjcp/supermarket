package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.sale.SaleResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.SaleItemResponse;

import java.util.List;

public interface ISaleService {
    SaleResponse createSale(Long branchId);
    SaleResponse findById(Long id);
    AddProductResponse addProduct(Long id, AddProductRequest request);
    List<SaleItemResponse> getProducts(Long saleId);
    void removeProduct(Long id, Long productId);
}
