package com.supermarket.supermarket_api.model;

import com.supermarket.supermarket_api.exception.InvalidSaleStateException;
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
        sale.decreaseQuantity(product);

        assertThat(sale.containsProduct(product.getId())).isFalse();
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

    @Test
    void finishSale_whenOpen_shouldMarkAsFinished() {
        Sale sale = new Sale(branch);
        sale.finish();
        assertThat(sale.getStatus()).isEqualTo(SaleStatus.FINISHED);
    }

    @Test
    void cancelSale_whenOpen_shouldMarkAsCancelled() {
        Sale sale = new Sale(branch);
        sale.cancel();
        assertThat(sale.getStatus()).isEqualTo(SaleStatus.CANCELLED);
    }

    @Test
    void finishSale_whenCancelled_shouldThrow() {
        Sale sale = new Sale(branch);
        sale.cancel();
        assertThatThrownBy(sale::finish).isInstanceOf(InvalidSaleStateException.class);
    }

    @Test
    void cancelSale_whenFinished_shouldThrow() {
        Sale sale = new Sale(branch);
        sale.finish();
        assertThatThrownBy(sale::cancel).isInstanceOf(InvalidSaleStateException.class);
    }

}
