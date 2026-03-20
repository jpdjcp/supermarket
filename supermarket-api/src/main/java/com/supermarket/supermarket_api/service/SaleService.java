package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.sale.SaleDetail;
import com.supermarket.supermarket_api.dto.sale.SaleSummary;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

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
    public SaleDetail createSale(Long branchId) {
        require(branchId != null, "Branch ID cannot be null");

        User currentUser = userService.getCurrentUser();
        Branch branch = branchService.findRequiredById(branchId);

        Sale sale = new Sale(branch, currentUser);
        Sale saved = repository.save(sale);
        DiscountStrategy strategy = discountResolver.resolve(saved);
        return saleMapper.toDetail(saved, strategy);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleDetail findById(Long saleId) {
        require(saleId != null, "Sale ID cannot be null");

        Sale sale = getSaleOwnedByCurrentUser(saleId);

        DiscountStrategy strategy = discountResolver.resolve(sale);
        return saleMapper.toDetail(sale, strategy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleSummary> findByUserId(Long userId) {
        require(userId != null, "User ID cannot be null");
        userService.ensureExists(userId);

        return  repository.findByUser_Id(userId)
                .stream()
                .map(sale -> saleMapper.toSummary(
                        sale,
                        discountResolver.resolve(sale)
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleSummary> findByBranchId(Long branchId) {
        require(branchId != null, "Branch ID cannot be null");

        return  repository.findByBranch_Id(branchId)
                .stream()
                .map(sale -> saleMapper.toSummary(
                        sale,
                        discountResolver.resolve(sale)
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleSummary> findByCreatedAt(Instant from, Instant to) {
        require(from != null, "Parameter 'from' instant cannot be null");
        require(to != null, "Parameter 'to' instant cannot be null");
        require(from.isBefore(to), "Parameter 'from' must be before than 'to'");

        return repository.findByCreatedAtBetween(from, to)
                .stream()
                .map(sale -> saleMapper.toSummary(
                        sale,
                        discountResolver.resolve(sale)))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleSummary> findByClosedAt(Instant from, Instant to) {
        require(from != null, "Parameter 'from' instant cannot be null");
        require(to != null, "Parameter 'to' instant cannot be null");
        require(from.isBefore(to), "Parameter 'from' must be before than 'to'");

        return repository.findByClosedAtBetween(from, to)
                .stream()
                .map(sale -> saleMapper.toSummary(
                        sale,
                        discountResolver.resolve(sale)))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getItems(Long saleId) {
        Sale sale = getSaleOwnedByCurrentUser(saleId);

        return sale.getSaleItems().stream()
                .map(itemMapper::toSaleItemResponse)
                .toList();
    }

    @Override
    public SaleItem getItem(Long saleId, Long productId) {
        Sale sale = getSaleOwnedByCurrentUser(saleId);

        Product product = productService.findRequiredById(productId);
        return sale.findItem(product);
    }

    @Override
    public boolean containsProduct(Long saleId, Long productId) {
        return getSaleOwnedByCurrentUser(saleId)
                .containsProduct(productId);
    }

    @Override
    @Transactional
    public ItemResponse addProduct(Long saleId, @Nonnull AddProductRequest request) {
        require(saleId != null, "Sale ID cannot be null");

        Sale sale = getSaleOwnedByCurrentUser(saleId);

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

        Sale sale = getSaleOwnedByCurrentUser(saleId);

        validateSaleOpen(sale, "Sale must be OPEN to remove a product");
        Product product = productService.findRequiredById(productId);
        sale.removeProduct(product);
    }

    @Override
    @Transactional
    public void increaseQuantity(Long saleId, Long productId) {
        require(saleId != null, "Sale ID cannot be null");
        require(productId != null, "Product ID cannot be null");

        Sale sale = getSaleOwnedByCurrentUser(saleId);

        validateSaleOpen(sale,"Sale must be OPEN to increase quantity");

        Product product = productService.findRequiredById(productId);
        sale.increaseQuantity(product);
    }

    @Override
    @Transactional
    public void decreaseQuantity(Long saleId, Long productId) {
        require(saleId != null, "Sale ID cannot be null");
        require(productId != null, "Product ID cannot be null");

        Sale sale = getSaleOwnedByCurrentUser(saleId);

        validateSaleOpen(sale, "Sale must be OPEN to decrease quantity");
        Product product = productService.findRequiredById(productId);
        sale.decreaseQuantity(product);
    }

    @Override
    @Transactional
    public SaleDetail finishSale(Long saleId) {
        Sale sale = getSaleOwnedByCurrentUser(saleId);

        validateSaleOpen(sale, "Sale must be OPEN to finish it");
        sale.finish();
        return saleMapper.toDetail(
                sale,
                discountResolver.resolve(sale)
        );
    }

    @Override
    @Transactional
    public SaleDetail cancelSale(Long saleId) {
        Sale sale = getSaleOwnedByCurrentUser(saleId);

        sale.ensureOpen();

        sale.cancel();
        return saleMapper.toDetail(
                sale,
                discountResolver.resolve(sale)
        );
    }

    private void require(boolean condition, String message) {
        if (!condition)
            throw new IllegalArgumentException(message);
    }

    @Transactional(readOnly = true)
    private Sale getSaleOwnedByCurrentUser(Long saleId) {
        Sale sale = repository.findById(saleId)
                .orElseThrow(()-> new SaleNotFoundException(saleId));

        User currentUser = userService.getCurrentUser();

        if (!Objects.equals(sale.getUser().getId(), currentUser.getId()))
            throw new AccessDeniedException("Forbidden: this sale doesn't belongs to this user");

        return sale;
    }
}
