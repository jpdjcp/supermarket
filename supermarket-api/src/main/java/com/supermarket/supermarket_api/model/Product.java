package com.supermarket.supermarket_api.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@Entity
public class Product {

    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Z0-9-]{6,20}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NaturalId
    @Column(nullable = false, unique = true)
    private String sku;

    public Product(String sku, String name, BigDecimal price) {
        this.sku = Objects.requireNonNull(sku, "Product SKU cannot be null");
        this.name = Objects.requireNonNull(name, "Product's name cannot be null");
        this.price = Objects.requireNonNull(price, "Product's price cannot be null");

        require(!sku.isBlank(), "Product's SKU cannot be blank");
        require(isValidSku(sku), "SKU must be 6-20 chars, uppercase, numbers or hyphen");
        require(!name.isBlank(), "Product's name cannot be blank");
        require(price.signum() > 0, "Product's price must be over zero");
    }

    public void changePrice(BigDecimal newPrice) {
        validatePrice(newPrice);
        this.price = newPrice;
    }

    public static boolean isValidSku(String sku) {
        return sku != null && SKU_PATTERN.matcher(sku).matches();
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null || price.signum() <= 0)
            throw new IllegalArgumentException("Product price must be positive");
    }

    private void require(boolean condition, String message) {
        if (!condition)
            throw new IllegalArgumentException(message);
    }


}
