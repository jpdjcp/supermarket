package com.supermarket.supermarket_api.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class ProductTest {

    @Test
    void validProduct_shouldBeCreated() {
        Product product = new Product("Milk", BigDecimal.valueOf(10));

        assertThat(product.getName()).isEqualTo("Milk");
        assertThat(product.getPrice()).isEqualByComparingTo("10");
    }

    @Test
    void constructor_nullName_shouldThrow() {
        assertThatThrownBy(() -> new Product(null, BigDecimal.valueOf(10)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void blankName_shouldThrow() {
        assertThatThrownBy(() -> new Product("", BigDecimal.valueOf(10)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void priceEqualsZero_shouldThrow() {
        assertThatThrownBy(() -> new Product("Product name", BigDecimal.valueOf(0)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void negativePrice_shouldThrow() {
        assertThatThrownBy(() -> new Product("Product name", BigDecimal.valueOf(-10)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void illegalPriceChange_shouldThrow() {
        Product product = new Product("Products name", BigDecimal.valueOf(10));
        assertThatThrownBy(() -> product.changePrice(BigDecimal.valueOf(-10)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
