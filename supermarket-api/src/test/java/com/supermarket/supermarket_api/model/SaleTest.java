package com.supermarket.supermarket_api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

public class SaleTest {

    private Branch branch;
    private Product product;

    @BeforeEach
    void setUp() {
        branch = new Branch("Branch Address");
        product = new Product("Product name", new BigDecimal("1000"));
    }

    @Test
    void shouldAddProductToSale() {
        Sale sale = new Sale(branch);
        sale.addProduct(product);
        assertThat(sale.getSaleItems()).hasSize(1);
        assertThat(sale.getSaleItems().getFirst().getQuantity()).isEqualTo(1);
    }

    @Test
    void shouldIncreaseQuantityWhenAddingSameProductAgain() {
        Sale sale = new Sale(branch);

        sale.addProduct(product);
        sale.addProduct(product);

        assertThat(sale.getSaleItems()).hasSize(1);
        assertThat(sale.getSaleItems().getFirst().getQuantity()).isEqualTo(2);
    }

    @Test
    void shouldNotAllowQuantityBelowOne() {
        Sale sale = new Sale(branch);
        sale.addProduct(product);

        assertThatThrownBy(() ->
                sale.decreaseQuantity(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldCalculateTotal() {
        Sale sale = new Sale(branch);

        sale.addProduct(product);
        sale.increaseQuantity(product);
        assertThat(sale.getTotal()).isEqualTo(new BigDecimal("2000"));

        sale.decreaseQuantity(product);
        assertThat(sale.getTotal()).isEqualTo(new BigDecimal("1000"));
    }


}
