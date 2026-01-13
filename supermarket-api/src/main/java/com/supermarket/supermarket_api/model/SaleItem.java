package com.supermarket.supermarket_api.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public SaleItem(Sale sale, Product product) {
        validate(sale, product);

        this.sale = sale;
        this.product = product;
        this.quantity = 1;
    }

    private static void validate(Sale sale, Product product) {
        if (sale == null)
            throw new IllegalArgumentException("Sale cannot be null");
        if (product == null)
            throw new IllegalArgumentException("Product cannot be null");
    }

    public void increaseQuantity() {
        this.quantity++;
    }

    public void decreaseQuantity() {
        if (this.quantity == 1)
            throw new IllegalArgumentException("Quantity must be at least 1");

        this.quantity--;
    }

    @Transient
    public BigDecimal getSubtotal() {
        return product.getPrice()
                .multiply(BigDecimal.valueOf(quantity));
    }
}
