package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.AddItemRequest;
import com.supermarket.supermarket_api.dto.SaleResponse;
import com.supermarket.supermarket_api.dto.SaleItemResponse;

import java.util.List;

public interface ISaleService {
    SaleResponse createSale(Long branchId);
    SaleResponse findById(Long id);
    List<SaleResponse> getSalesByBranch(Long branchId);
    SaleItemResponse addProduct(Long id, AddItemRequest request);
    List<SaleItemResponse> getProducts(Long saleId);
    void deleteProduct(Long id, Long itemId);
}
