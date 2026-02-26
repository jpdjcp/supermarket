package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.sale.SaleResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.ItemResponse;
import com.supermarket.supermarket_api.exception.SaleNotFoundException;
import com.supermarket.supermarket_api.mapper.SaleItemMapper;
import com.supermarket.supermarket_api.mapper.SaleMapper;
import com.supermarket.supermarket_api.model.*;
import com.supermarket.supermarket_api.pricing.DiscountResolver;
import com.supermarket.supermarket_api.pricing.discount.DiscountStrategy;
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
    private final DiscountResolver discountResolver;

    @Override
    @Transactional
    public SaleResponse createSale(Long branchId, Long userId) {
        require(branchId != null, "Branch ID cannot be null");
        require(userId != null, "User ID cannot be null");

        Branch branch = branchService.findRequiredById(branchId);
        User user = userService.findRequiredById(userId);
        Sale sale = new Sale(branch, user);
        Sale saved = repository.save(sale);
        DiscountStrategy strategy = discountResolver.resolve(saved);
        return saleMapper.toResponse(saved, strategy);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse findById(Long saleId) {
        require(saleId != null, "Sale ID cannot be null");

        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        DiscountStrategy strategy = discountResolver.resolve(sale);
        return saleMapper.toResponse(sale, strategy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> findByUserId(Long userId) {
        require(userId != null, "User ID cannot be null");
        userService.ensureExists(userId);

        return  repository.findByUser_Id(userId)
                .stream()
                .map(sale -> saleMapper.toResponse(
                        sale,
                        discountResolver.resolve(sale)
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> findByBranchId(Long branchId) {
        require(branchId != null, "Branch ID cannot be null");

        return  repository.findByBranch_Id(branchId)
                .stream()
                .map(sale -> saleMapper.toResponse(
                        sale,
                        discountResolver.resolve(sale)
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> findByCreatedAt(Instant from, Instant to) {
        require(from != null, "Parameter 'from' instant cannot be null");
        require(to != null, "Parameter 'to' instant cannot be null");
        require(from.isBefore(to), "Parameter 'from' must be before than 'to'");

        return repository.findByCreatedAtBetween(from, to)
                .stream()
                .map(sale -> saleMapper.toResponse(
                        sale,
                        discountResolver.resolve(sale)))
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
                .map(sale -> saleMapper.toResponse(
                        sale,
                        discountResolver.resolve(sale)))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getItems(Long saleId) {
        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

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
    public ItemResponse addProduct(Long saleId, @Nonnull AddProductRequest request) {
        require(saleId != null, "Sale ID cannot be null");

        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        validateSaleOpen(sale, "Sale must be OPEN to add a product");

        Product product = productService.findRequiredById(request.productId());
        SaleItem item = sale.addProduct(product);

        return itemMapper.toResponse(item);
    }

    @Override
    @Transactional
    public void removeProduct(Long saleId, Long productId) {
        require(saleId != null, "Sale ID cannot be null");
        require(productId != null, "Product ID cannot be null");

        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        validateSaleOpen(sale, "Sale must be OPEN to remove a product");
        Product product = productService.findRequiredById(productId);
        sale.removeProduct(product);
    }

    @Override
    @Transactional
    public void increaseQuantity(Long saleId, Long productId) {
        require(saleId != null, "Sale ID cannot be null");
        require(productId != null, "Product ID cannot be null");

        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        validateSaleOpen(sale,"Sale must be OPEN to increase quantity");

        Product product = productService.findRequiredById(productId);
        sale.increaseQuantity(product);
    }


    @Override
    @Transactional
    public void decreaseQuantity(Long saleId, Long productId) {
        require(saleId != null, "Sale ID cannot be null");
        require(productId != null, "Product ID cannot be null");

        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        validateSaleOpen(sale, "Sale must be OPEN to decrease quantity");
        Product product = productService.findRequiredById(productId);
        sale.decreaseQuantity(product);
    }

    @Override
    @Transactional
    public SaleResponse finishSale(Long saleId) {
        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        validateSaleOpen(sale, "Sale must be OPEN to finish it");
        sale.finish();
        return saleMapper.toResponse(
                sale,
                discountResolver.resolve(sale)
        );
    }

    @Override
    @Transactional
    public SaleResponse cancelSale(Long saleId) {
        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        validateSaleOpen(sale, "Sale must be OPEN to cancel it");

        sale.cancel();
        return saleMapper.toResponse(
                sale,
                discountResolver.resolve(sale)
        );
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
