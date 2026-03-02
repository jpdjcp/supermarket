package com.supermarket.integration;

import com.supermarket.supermarket_api.model.Product;
import com.supermarket.supermarket_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

// import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

        assertNotNull(product.getId());

        found = productRepository.findById(product.getId());
        assertTrue(found.isPresent());
        assertEquals(found.get().getSku(), sku);
        assertEquals(found.get().getName(), name);
        assertEquals(found.get().getPrice(), price);

    }
}
