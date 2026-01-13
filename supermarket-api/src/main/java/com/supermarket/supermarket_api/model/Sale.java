package com.supermarket.supermarket_api.model;

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

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SaleItem> saleItems = new ArrayList<>();

    public Sale(Branch branch) {
        this.branch = branch;
    }

    public SaleItem addProduct(Product product, int quantity) {
        validate(product, quantity);
        SaleItem item = new SaleItem(this, product);
        this.saleItems.add(item);
        return item;
    }

    public void removeProduct(Product product) {
        SaleItem item = findItem(product.getId());
        this.saleItems.remove(item);
    }

    public void increaseQuantity(Product product) {
        SaleItem item = findItem(product.getId());
        item.increaseQuantity();
    }

    public void decreaseQuantity(Product product) {
        SaleItem item = findItem(product.getId());
        item.decreaseQuantity();
    }

    @Transient
    public BigDecimal getTotal() {
        return saleItems.stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static void validate(Product product, int quantity) {
        if (quantity < 0)
            throw new IllegalArgumentException("Quantity must be at least 1");
        if (product == null)
            throw new IllegalArgumentException("Product cannot be null");
    }

    private SaleItem findItem(Long productId) {
        return saleItems.stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new SaleItemNotFoundException(productId));
    }
}
