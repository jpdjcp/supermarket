package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.sale.SaleResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.SaleItemResponse;
import com.supermarket.supermarket_api.exception.SaleNotFoundException;
import com.supermarket.supermarket_api.mapper.SaleItemMapper;
import com.supermarket.supermarket_api.mapper.SaleMapper;
import com.supermarket.supermarket_api.model.*;
import com.supermarket.supermarket_api.repository.SaleRepository;
import com.supermarket.supermarket_api.exception.SaleNotOpenException;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class SaleService implements ISaleService {

    private final SaleRepository repository;
    private final BranchService branchService;
    private final UserService userService;
    private final ProductService productService;
    private final SaleMapper saleMapper;
    private final SaleItemMapper itemMapper;

    @Override
    @Transactional
    public SaleResponse createSale(Long branchId, Long userId) {
        Branch branch = branchService.findRequiredById(branchId);
        User user = userService.findRequiredById(userId);
        Sale sale = new Sale(branch, user);
        return saleMapper.toResponse(repository.save(sale));
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse findById(Long id) {
        return repository.findById(id)
                .map(saleMapper::toResponse)
                .orElseThrow(() -> new SaleNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> findByCreatedAt(Instant from, Instant to) {
        require(from != null, "Parameter 'from' instant cannot be null");
        require(to != null, "Parameter 'to' instant cannot be null");
        require(from.isBefore(to), "Parameter 'from' must be before than 'to'");

        return repository.findByCreatedAtBetween(from, to)
                .stream()
                .map(saleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> findByClosedAt(Instant from, Instant to) {
        require(from != null, "Parameter 'from' instant cannot be null");
        require(to != null, "Parameter 'to' instant cannot be null");
        require(from.isBefore(to), "Parameter 'from' must be before than 'to'");

        return repository.findByClosedAtBetween(from, to)
                .stream()
                .map(saleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleItemResponse> getItems(Long id) {
        Sale sale = findRequiredSale(id);

        return sale.getSaleItems().stream()
                .map(itemMapper::toSaleItemResponse)
                .toList();
    }

    @Override
    public SaleItem getItem(Long saleId, Long productId) {
        Sale sale = repository.findById(saleId)
                .orElseThrow(()-> new SaleNotFoundException(saleId));
        Product product = productService.findRequiredById(productId);
        return sale.findItem(product);
    }

    @Override
    public boolean containsProduct(Long saleId, Long productId) {
        Sale sale = repository.findById(saleId)
                .orElseThrow(()-> new SaleNotFoundException(saleId));

        return sale.containsProduct(productId);
    }

    @Override
    @Transactional
    public AddProductResponse addProduct(Long id, @Nonnull AddProductRequest request) {
        Sale sale = findRequiredSale(id);
        validateSaleOpen(sale, "Sale must be OPEN to add a product");

        Product product = productService.findRequiredById(request.productId());
        SaleItem item = sale.addProduct(product);

        return itemMapper.toResponse(item);
    }

    @Override
    @Transactional
    public void removeProduct(Long id, Long productId) {
        Sale sale = findRequiredSale(id);
        validateSaleOpen(sale, "Sale must be OPEN to remove a product");
        Product product = productService.findRequiredById(productId);
        sale.removeProduct(product);
    }

    @Override
    @Transactional
    public void increaseQuantity(Long id, Long productId) {
        Sale sale = findRequiredSale(id);
        validateSaleOpen(sale,"Sale must be OPEN to increase quantity");

        Product product = productService.findRequiredById(productId);
        sale.increaseQuantity(product);
    }


    @Override
    @Transactional
    public void decreaseQuantity(Long id, Long productId) {
        Sale sale = findRequiredSale(id);
        validateSaleOpen(sale, "Sale must be OPEN to decrease quantity");
        Product product = productService.findRequiredById(productId);
        sale.decreaseQuantity(product);
    }

    @Override
    @Transactional
    public SaleResponse finishSale(Long saleId) {
        Sale sale = findRequiredSale(saleId);
        validateSaleOpen(sale, "Sale must be OPEN to finish it");

        sale.finish();
        return saleMapper.toResponse(sale);
    }

    @Override
    @Transactional
    public SaleResponse cancelSale(Long saleId) {
        Sale sale = findRequiredSale(saleId);
        validateSaleOpen(sale, "Sale must be OPEN to cancel it");

        sale.cancel();
        return saleMapper.toResponse(sale);
    }

    @Nonnull
    private Sale findRequiredSale(Long saleId) {
        return repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));
    }

    private static void validateSaleOpen(Sale sale, String message) {
        if (sale.getStatus() != SaleStatus.OPEN) {
            throw new SaleNotOpenException(message);
        }
    }

    private void require(boolean condition, String message) {
        if (!condition)
            throw new IllegalArgumentException(message);
    }
}
