package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.AddItemRequest;
import com.supermarket.supermarket_api.dto.SaleDTO;
import com.supermarket.supermarket_api.dto.SaleItemDTO;

import java.util.List;

public interface ISaleService {
    List<SaleDTO> findAll();
    SaleDTO createSale(Long branchId);
    SaleDTO getById(Long id);
    List<SaleDTO> getSalesByBranch(Long branchId);
    SaleItemDTO addItem(Long saleId, AddItemRequest request);
    List<SaleItemDTO> getItemsBySale(Long saleId);
}
