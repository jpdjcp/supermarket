package com.supermarket.supermarket_api.model;

import com.supermarket.supermarket_api.exception.InvalidSaleStateException;
import com.supermarket.supermarket_api.exception.SaleItemNotFoundException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private SaleStatus status = SaleStatus.OPEN;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SaleItem> saleItems = new ArrayList<>();

    public Sale(Branch branch) {
        this.branch = branch;
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
        item.decreaseQuantity();
    }

    public void finish() {
        if (this.status != SaleStatus.OPEN)
            throw new InvalidSaleStateException("Only OPEN sales can be finished");

        this.status = SaleStatus.FINISHED;
    }

    public void cancel() {
        if (this.status != SaleStatus.OPEN)
            throw new InvalidSaleStateException("Only OPEN sales can be cancelled");

        this.status = SaleStatus.CANCELLED;
    }

    @Transient
    public BigDecimal getTotal() {
        return saleItems.stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private SaleItem findItem(Product product) {
        return saleItems.stream()
                .filter(i -> i.getProduct().equals(product))
                .findFirst()
                .orElseThrow(() -> new SaleItemNotFoundException(product.getId()));
    }

}
