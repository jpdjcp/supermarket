package com.supermarket.supermarket_api.model;

import com.supermarket.supermarket_api.exception.InvalidSaleStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

public class SaleTest {
    private Branch branch;
    private User user;
    private Product product;
    private Sale sale;

    @BeforeEach
    void setUp() {
        final String SKU  = "ABCD-1234";
        branch = new Branch("Branch Address");
        user = new User("John", "Abcd-1234", UserRole.ROLE_USER);
        product = new Product(SKU, "Product name", new BigDecimal("1000"));
        sale = new Sale(branch, user);
    }

    @Test
    void createSale_shouldCreateIt() {
        assertThat(sale.getId()).isNull();
        assertThat(sale.getUser()).isSameAs(user);
        assertThat(sale.getCreatedAt()).isNull();
        assertThat(sale.getClosedAt()).isNull();
        assertThat(sale.getSaleItems()).isEmpty();
        assertThat(sale.getStatus()).isEqualTo(SaleStatus.OPEN);
        assertThat(sale.getBranch()).isSameAs(branch);
    }

    @Test
    void createSale_withNullBranch_shouldThrow() {
        assertThatThrownBy(()-> new Sale(null, user))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSale_withNullUser_shouldThrow() {
        assertThatThrownBy(()-> new Sale(branch, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void addProduct_shouldAddProductToSale() {
        sale.addProduct(product);
        assertThat(sale.getSaleItems()).hasSize(1);
        assertThat(sale.getSaleItems().getFirst().getQuantity()).isEqualTo(1);
    }

    @Test
    void addProduct_whenProductAlreadyAdded_shouldIncreaseQuantity() {
        sale.addProduct(product);
        sale.addProduct(product);

        assertThat(sale.getSaleItems()).hasSize(1);
        assertThat(sale.getSaleItems().getFirst().getQuantity()).isEqualTo(2);
    }

    @Test
        void decreaseQuantity_whenQuantityIsOne_shouldRemoveProduct() {
        sale.addProduct(product);
        sale.decreaseQuantity(product);

        assertThat(sale.containsProduct(product.getId())).isFalse();
    }

    @Test
    void getSubTotal_shouldCalculateSubTotal() {
        sale.addProduct(product);
        sale.increaseQuantity(product);
        assertThat(sale.calculateTotal()).isEqualTo(new BigDecimal("2000"));

        sale.decreaseQuantity(product);
        assertThat(sale.calculateTotal()).isEqualTo(new BigDecimal("1000"));
    }

    @Test
    void finishSale_whenOpen_shouldSetStatusAndCloseAt() {
        sale.finish();

        assertThat(sale.getStatus()).isEqualTo(SaleStatus.FINISHED);
        assertThat(sale.getClosedAt()).isNotNull();
    }

    @Test
    void cancelSale_whenOpen_shouldSetStatusAndClosedAt() {
        sale.cancel();

        assertThat(sale.getStatus()).isEqualTo(SaleStatus.CANCELLED);
        assertThat(sale.getClosedAt()).isNotNull();
    }

    @Test
    void finishSale_whenCancelled_shouldThrow() {
        sale.cancel();

        assertThatThrownBy(sale::finish).isInstanceOf(InvalidSaleStateException.class);
    }

    @Test
    void cancelSale_whenFinished_shouldThrow() {
        sale.finish();

        assertThatThrownBy(sale::cancel).isInstanceOf(InvalidSaleStateException.class);
    }

}
