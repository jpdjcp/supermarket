package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.sale.SaleDetail;
import com.supermarket.supermarket_api.dto.sale.SaleSummary;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.ItemResponse;
import com.supermarket.supermarket_api.model.SaleItem;

import java.time.Instant;
import java.util.List;

public interface ISaleService {
    SaleDetail createSale(Long branchId, Long userId);
    SaleDetail findById(Long id);
    List<SaleSummary> findByUserId(Long userId);
    List<SaleSummary> findByBranchId(Long branchId);
    List<SaleSummary> findByCreatedAt(Instant from, Instant to);
    List<SaleSummary> findByClosedAt(Instant from, Instant to);
    ItemResponse addProduct(Long id, AddProductRequest request);
    List<ItemResponse> getItems(Long saleId);
    SaleItem getItem(Long id, Long productId);
    boolean containsProduct(Long id, Long productId);
    void removeProduct(Long id, Long productId);
    void increaseQuantity(Long id, Long productId);
    void decreaseQuantity(Long id, Long productId);
    SaleDetail finishSale(Long saleId);
    SaleDetail cancelSale(Long saleId);
}
