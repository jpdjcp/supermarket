package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.AddItemRequest;
import com.supermarket.supermarket_api.dto.SaleResponse;
import com.supermarket.supermarket_api.dto.SaleItemDTO;

import java.util.List;

public interface ISaleService {
    List<SaleResponse> findAll();
    SaleResponse createSale(Long branchId);
    SaleResponse getById(Long id);
    List<SaleResponse> getSalesByBranch(Long branchId);
    SaleItemDTO addItem(Long saleId, AddItemRequest request);
    List<SaleItemDTO> getItemsBySale(Long saleId);
}
