package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.AddItemRequest;
import com.supermarket.supermarket_api.dto.SaleResponse;
import com.supermarket.supermarket_api.dto.SaleItemResponse;

import java.util.List;

public interface ISaleService {
    List<SaleResponse> findAll();
    SaleResponse createSale(Long branchId);
    SaleResponse getById(Long id);
    List<SaleResponse> getSalesByBranch(Long branchId);
    SaleItemResponse addItem(Long saleId, AddItemRequest request);
    List<SaleItemResponse> getItemsBySale(Long saleId);
}
