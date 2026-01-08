package com.supermarket.supermarket_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int quantity;

    public SaleItem(Sale sale, Product product, int quantity) {
        if (sale == null)
            throw new IllegalArgumentException("Sale cannot be null");
        if (product == null)
            throw new IllegalArgumentException("Product cannot be null");
        if (quantity < 0)
            throw new IllegalArgumentException("Quantity must be at least 1");

        this.sale = sale;
        this.product = product;
        this.quantity = quantity;
    }

    public void changeQuantity(int quantity) {
        if (quantity < 0)
            throw new IllegalArgumentException("Quantity must bbe at least 1");

        this.quantity = quantity;
    }

    @Transient
    public BigDecimal getSubtotal() {
        return product.getPrice()
                .multiply(BigDecimal.valueOf(quantity));
    }
}
