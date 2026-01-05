package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.AddItemRequest;
import com.supermarket.supermarket_api.dto.SaleDTO;
import com.supermarket.supermarket_api.dto.SaleItemDTO;

import java.util.List;

public interface ISaleService {
    List<SaleDTO> list();
    SaleDTO createSale(Long branchId);
    SaleDTO get(Long id);
    List<SaleDTO> getSalesByBranch(Long branchId);
    SaleItemDTO addItem(Long saleId, AddItemRequest request);
    List<SaleItemDTO> getItemsBySale(Long saleId);
}
