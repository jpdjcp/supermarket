package com.supermarket.supermarket_api.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class ProductTest {

    private final String SKU = "ABCD-1234";
    private final String name = "Milk";
    private final BigDecimal price = BigDecimal.valueOf(10);

    @Test
    void validProduct_shouldBeCreated() {
        Product product = new Product(SKU, name, price);

        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualByComparingTo("10");
    }

    @Test
    void createProduct_withNullSKU_shouldThrow() {
        assertThatThrownBy(()-> new Product(null, name, price))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void createProduct_withBlankSKU_shouldThrow() {
        assertThatThrownBy(()-> new Product("", name, price))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createProduct_withSKUInvalidPattern_shouldThrow() {
        assertThatThrownBy(()-> new Product("abcd-1234", name, price))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_nullName_shouldThrow() {
        assertThatThrownBy(() -> new Product(SKU, null, price))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void blankName_shouldThrow() {
        assertThatThrownBy(() -> new Product(SKU, "", price))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void priceEqualsZero_shouldThrow() {
        assertThatThrownBy(() -> new Product(SKU, name, BigDecimal.valueOf(0)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void negativePrice_shouldThrow() {
        assertThatThrownBy(() -> new Product(SKU, name, BigDecimal.valueOf(-10)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void illegalPriceChange_shouldThrow() {
        Product product = new Product(SKU, name, price);
        assertThatThrownBy(() -> product.changePrice(BigDecimal.valueOf(-10)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
