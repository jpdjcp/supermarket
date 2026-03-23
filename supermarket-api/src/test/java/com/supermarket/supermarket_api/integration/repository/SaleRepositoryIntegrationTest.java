package com.supermarket.supermarket_api.integration.repository;

import com.supermarket.supermarket_api.integration.AbstractIntegrationTest;
import com.supermarket.supermarket_api.model.*;
import com.supermarket.supermarket_api.repository.BranchRepository;
import com.supermarket.supermarket_api.repository.ProductRepository;
import com.supermarket.supermarket_api.repository.SaleRepository;
import com.supermarket.supermarket_api.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

//@SpringBootTest
@Transactional
public class SaleRepositoryIntegrationTest extends AbstractIntegrationTest {
/*
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private String username;
    private String password;
    private UserRole role;
    private User user;
    private String address;
    private Branch branch;
    private String productName;
    private String sku;
    private BigDecimal price;
    private Product product;
    private Sale sale;
    private Sale saved;
    private Optional<Sale> found;
    private Optional<Sale> salesFound;
    private List<Sale> results;
    private Instant from;
    private Instant to;

    @BeforeEach
    void setup() {
        username = "John Jackson";
        password = "sd51v5211v5s";
        role = UserRole.ROLE_USER;
        user = new User(username, password);
        user.setRole(role);
        User savedUser = userRepository.save(user);

        address = "Av. Evergreen 1010, Springfield";
        branch = new Branch(address);
        Branch savedBranch = branchRepository.save(branch);

        sku = "ABC-1234";
        productName = "Milk";
        price = BigDecimal.valueOf(10);
        product = new Product(sku, productName, price);
        productRepository.save(product);

        entityManager.flush();
        entityManager.clear();

        sale = new Sale(savedBranch, savedUser);
    }

    @Test
    void shouldSaveAndRetrieveSale() {
        sale = saleRepository.save(sale);
        salesFound = saleRepository.findById(sale.getId());

        assertThat(salesFound).isPresent();
        assertThat(salesFound.get().getUser().getId()).isEqualTo(user.getId());
        assertThat(salesFound.get().getBranch().getId()).isEqualTo(branch.getId());
        assertThat(salesFound.get().getCreatedAt()).isNotNull();
        assertThat(salesFound.get().getClosedAt()).isNull();
        assertThat(salesFound.get().getSaleItems()).isEmpty();
        assertThat(salesFound.get().getStatus()).isEqualTo(SaleStatus.OPEN);
    }

    @Test
    void shouldFindByUserId() {
        saved = saleRepository.save(sale);
        entityManager.flush();
        entityManager.clear();

        results = saleRepository.findByUser_Id(user.getId());

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getId()).isEqualTo(saved.getId());
    }

    @Test
    void shouldFindByBranchId() {
        saved = saleRepository.save(sale);
        entityManager.flush();
        entityManager.clear();

        results = saleRepository.findByBranch_Id(branch.getId());

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getId()).isEqualTo(saved.getId());
    }

    @Test
    void shouldAddProductAndSaveSale() {
        sale.addProduct(product);
        sale = saleRepository.save(sale);
        entityManager.flush();
        entityManager.clear();

        found = saleRepository.findById(sale.getId());

        assertThat(found).isPresent();
        assertThat(found.get().containsProduct(product.getId())).isTrue();
    }

    @Test
    void shouldRemoveProductAndSaveSale() {
        sale.addProduct(product);
        saved = saleRepository.save(sale);
        entityManager.flush();
        entityManager.clear();

        found = saleRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().containsProduct(product.getId())).isTrue();

        found.get().removeProduct(product);
        entityManager.flush();
        entityManager.clear();

        found = saleRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().containsProduct(product.getId())).isFalse();
    }

    @Test
    void shouldPersistIncreaseQuantity() {
        int expected1 = 1;
        int expected2 = 2;
        sale.addProduct(product);
        saved = saleRepository.save(sale);
        entityManager.flush();
        entityManager.clear();

        sale = saleRepository.findById(saved.getId()).orElseThrow();
        assertThat(sale.findItem(product).getQuantity()).isEqualTo(expected1);
        sale.increaseQuantity(product);
        entityManager.flush();
        entityManager.clear();

        sale = saleRepository.findById(saved.getId()).orElseThrow();
        assertThat(sale.findItem(product).getQuantity())
                .isEqualTo(expected2);
    }

    @Test
    void shouldPersistDecreaseQuantity() {
        int expected1 = 1;
        int expected2 = 2;
        sale.addProduct(product);
        saved = saleRepository.save(sale);
        entityManager.flush();
        entityManager.clear();

        sale = saleRepository.findById(saved.getId()).orElseThrow();
        assertThat(sale.findItem(product).getQuantity()).isEqualTo(expected1);
        sale.increaseQuantity(product);
        entityManager.flush();
        entityManager.clear();

        sale = saleRepository.findById(saved.getId()).orElseThrow();
        assertThat(sale.findItem(product).getQuantity()).isEqualTo(expected2);
        sale.decreaseQuantity(product);
        entityManager.flush();
        entityManager.clear();

        sale = saleRepository.findById(saved.getId()).orElseThrow();
        assertThat(sale.findItem(product).getQuantity()).isEqualTo(expected1);
    }

    @Test
    void shouldPersistFinishedStatus() {
        sale.finish();
        saved = saleRepository.save(sale);
        entityManager.flush();
        entityManager.clear();

        saved = saleRepository.findById(saved.getId()).orElseThrow();
        assertThat(saved.getStatus()).isEqualTo(SaleStatus.FINISHED);
    }

    @Test
    void shouldPersistCancelledStatus() {
        sale.cancel();
        saved = saleRepository.save(sale);
        entityManager.flush();
        entityManager.clear();

        sale = saleRepository.findById(saved.getId()).orElseThrow();
        assertThat(sale.getStatus()).isEqualTo(SaleStatus.CANCELLED);
    }

    @Test
    void shouldFindByCreatedAtBetween() {
        from = Instant.now().minusSeconds(10);
        to = Instant.now().plusSeconds(10);
        saved = saleRepository.save(sale);
        entityManager.flush();
        entityManager.clear();

        results  = saleRepository.findByCreatedAtBetween(from, to);
        assertThat(results.getFirst().getId()).isEqualTo(saved.getId());
    }

    @Test
    void shouldFindByClosedAtBetween() {
        from = Instant.now().minusSeconds(10);
        to = Instant.now().plusSeconds(10);
        saved = saleRepository.save(sale);
        entityManager.flush();
        entityManager.clear();

        sale = saleRepository.findById(saved.getId()).orElseThrow();
        assertThat(sale.getStatus()).isEqualTo(SaleStatus.OPEN);
        sale.finish();
        entityManager.flush();
        entityManager.clear();

        sale = saleRepository.findById(saved.getId()).orElseThrow();
        results = saleRepository.findByClosedAtBetween(from, to);

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getStatus()).isEqualTo(SaleStatus.FINISHED);
        assertThat(results.getFirst().getId()).isEqualTo(sale.getId());
        assertThat(results.getFirst().getClosedAt()).isBetween(from, to);
    }*/
}
