package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.sale.SaleResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.SaleItemResponse;
import com.supermarket.supermarket_api.model.Sale;
import com.supermarket.supermarket_api.model.SaleItem;

import java.time.Instant;
import java.util.List;

public interface ISaleService {
    SaleResponse createSale(Long branchId);
    SaleResponse findById(Long id);
    List<SaleResponse> findByCreatedAtBetween(Instant from, Instant to);
    AddProductResponse addProduct(Long id, AddProductRequest request);
    List<SaleItemResponse> getItems(Long saleId);
    SaleItem getItem(Long id, Long productId);
    boolean containsProduct(Long id, Long productId);
    void removeProduct(Long id, Long productId);
    void increaseQuantity(Long id, Long productId);
    void decreaseQuantity(Long id, Long productId);
    SaleResponse finishSale(Long saleId);
    SaleResponse cancelSale(Long saleId);
}
