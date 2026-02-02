package com.supermarket.supermarket_api.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    public Product(String name, BigDecimal price) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Product name cannot be blank");
        validatePrice(price);

        this.name = name;
        this.price = price;
    }

    public void changePrice(BigDecimal newPrice) {
        validatePrice(newPrice);
        this.price = newPrice;
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null || price.signum() <= 0)
            throw new IllegalArgumentException("Product price must be positive");
    }
}
