package com.supermarket.supermarket_api.integration;

import com.supermarket.supermarket_api.model.Product;
import com.supermarket.supermarket_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    private Optional<Product> found;
    private Product product;
    private String sku;
    private String name;
    private BigDecimal price;

    @BeforeEach
    void setUp() {
        sku = "ABC-1234";
        name = "Milk";
        price = BigDecimal.valueOf(10);
        product = new Product(sku, name, price);
    }

    @Test
    void shouldSaveAndRetrieveProduct() {
        product = productRepository.save(product);

        assertThat(product.getId()).isNotNull();

        found = productRepository.findById(product.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getSku()).isEqualTo(sku);
        assertThat(found.get().getName()).isEqualTo(name);
        assertThat(found.get().getPrice()).isEqualTo(price);
    }

    @Test
    void shouldEnforceUniqueSkuConstraint() {
        Product duplicatedProduct = new Product(sku, name, price);
        productRepository.save(product);

        assertThatThrownBy(()-> {
            productRepository.save(duplicatedProduct);
            productRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldNotAllowNullPrice() {
        Product nullPricedProduct = new Product(sku, name, null);

        assertThatThrownBy(()-> {
            productRepository.save(nullPricedProduct);
            productRepository.flush();
        }).isInstanceOf(NullPointerException.class);
    }
}
