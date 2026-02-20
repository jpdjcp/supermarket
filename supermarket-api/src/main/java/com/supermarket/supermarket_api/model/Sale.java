package com.supermarket.supermarket_api.model;

import com.supermarket.supermarket_api.exception.InvalidSaleStateException;
import com.supermarket.supermarket_api.exception.SaleItemNotFoundException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant closedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private SaleStatus status = SaleStatus.OPEN;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SaleItem> saleItems = new ArrayList<>();

    public Sale(Branch branch, User user) {
        this.branch = branch;
        this.user = user;
    }

    public SaleItem addProduct(Product product) {
        if (product == null)
            throw new IllegalArgumentException("Product cannot be null");

        return this.saleItems.stream()
                .filter(i -> i.getProduct().equals(product))
                .findFirst()
                .map(i -> {
                    i.increaseQuantity();
                    return i;
                })
                .orElseGet(() -> {
                    SaleItem item = new SaleItem(this, product);
                    this.saleItems.add(item);
                    return item;
                });
    }

    public void removeProduct(Product product) {
        SaleItem item = findItem(product);
        this.saleItems.remove(item);
    }

    public void increaseQuantity(Product product) {
        SaleItem item = findItem(product);
        item.increaseQuantity();
    }

    public void decreaseQuantity(Product product) {
        SaleItem item = findItem(product);
        if (item.getQuantity() == 1) {
            removeProduct(product);
        } else {
            item.decreaseQuantity();
        }
    }

    public void finish() {
        if (this.status != SaleStatus.OPEN)
            throw new InvalidSaleStateException("Only OPEN sales can be finished");

        this.status = SaleStatus.FINISHED;
        this.closedAt = Instant.now();
    }

    public void cancel() {
        if (this.status != SaleStatus.OPEN)
            throw new InvalidSaleStateException("Only OPEN sales can be cancelled");

        this.status = SaleStatus.CANCELLED;
        this.closedAt = Instant.now();
    }

    @Transient
    public BigDecimal getTotal() {
        return saleItems.stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public SaleItem findItem(Product product) {
        return saleItems.stream()
                .filter(i -> i.getProduct().equals(product))
                .findFirst()
                .orElseThrow(() -> new SaleItemNotFoundException(product.getId()));
    }

    public boolean containsProduct(Long productId) {
        return saleItems.stream()
                .anyMatch(i -> i.getProduct().getId()
                        .equals(productId));

    }

    @PrePersist
    private void setCreatedAt() {
        this.createdAt = Instant.now();
    }
}
