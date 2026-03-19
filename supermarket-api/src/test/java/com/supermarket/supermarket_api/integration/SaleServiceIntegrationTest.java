package com.supermarket.supermarket_api.integration;

import com.supermarket.supermarket_api.dto.sale.SaleDetail;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.ItemResponse;
import com.supermarket.supermarket_api.model.*;
import com.supermarket.supermarket_api.repository.BranchRepository;
import com.supermarket.supermarket_api.repository.ProductRepository;
import com.supermarket.supermarket_api.repository.SaleRepository;
import com.supermarket.supermarket_api.repository.UserRepository;
import com.supermarket.supermarket_api.service.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class SaleServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private Branch branch;
    private Product product;
    private User user;

    @BeforeEach
    void setup() {
        String sku = "ABC-1234";
        String productName = "Milk";
        BigDecimal price = BigDecimal.valueOf(10);
        product = productRepository.save(new Product(sku, productName, price));

        String address = "Branch Address";
        branch = branchRepository.save(new Branch(address));

        String username = "John Jackson";
        String password = "vx1c25fHGF21";
        UserRole role = UserRole.ROLE_USER;
        user = new User(username, password);
        user.setRole(role);
        user = userRepository.save(user);
    }

    @Test
    void shouldCreateAndPersistSale() {
        SaleDetail created = saleService.createSale(branch.getId(), user.getId());

        assertThat(saleRepository.findById(created.id())).isPresent();
        assertThat(created).isNotNull();
        assertThat(created.createdAt()).isNotNull();
        assertThat(created.closedAt()).isNull();
        assertThat(created.status()).isEqualTo(SaleStatus.OPEN);
        assertThat(created.branchId()).isEqualTo(branch.getId());
        assertThat(created.userId()).isEqualTo(user.getId());
        assertThat(created.items()).isEmpty();
    }

    @Test
    void shouldAddProductToSale() {
        AddProductRequest request = new AddProductRequest(product.getId());
        SaleDetail sale = saleService.createSale(branch.getId(), user.getId());

        ItemResponse response = saleService.addProduct(sale.id(), request);

        assertThat(saleRepository.findById(sale.id())).isPresent();
        assertThat(response.saleId()).isEqualTo(sale.id());
        assertThat(response.productId()).isEqualTo(product.getId());
        assertThat(response.quantity()).isEqualTo(1);
        assertThat(response.subtotal()).isEqualTo(product.getPrice());
    }

    @Test
    void shouldRemoveProductFromSale() {
        AddProductRequest request = new AddProductRequest(product.getId());
        SaleDetail sale = saleService.createSale(branch.getId(), user.getId());
        saleService.addProduct(sale.id(), request);
        List<ItemResponse> items = saleService.findById(sale.id()).items();
        assertThat(items.getFirst().productId()).isEqualTo(product.getId());
        assertThat(items).hasSize(1);

        saleService.removeProduct(sale.id(), product.getId());

        SaleDetail result = saleService.findById(sale.id());
        assertThat(result.items()).isEmpty();
    }

    @Test
    void shouldIncreaseQuantity() {
        AddProductRequest request = new AddProductRequest(product.getId());
        SaleDetail sale = saleService.createSale(branch.getId(), user.getId());
        saleService.addProduct(sale.id(), request);

        saleService.increaseQuantity(sale.id(), product.getId());

        ItemResponse item = saleService.findById(sale.id()).items().getFirst();
        assertThat(item.productId()).isEqualTo(product.getId());
        assertThat(item.quantity()).isEqualTo(2);
    }

    @Test
    void shouldDecreaseQuantity() {
        AddProductRequest request = new AddProductRequest(product.getId());
        SaleDetail sale = saleService.createSale(branch.getId(), user.getId());
        saleService.addProduct(sale.id(), request);
        saleService.increaseQuantity(sale.id(), product.getId());

        ItemResponse afterIncrease = saleService.findById(sale.id()).items().getFirst();
        assertThat(afterIncrease.productId()).isEqualTo(product.getId());
        assertThat(afterIncrease.quantity()).isEqualTo(2);

        saleService.decreaseQuantity(sale.id(), product.getId());

        ItemResponse afterDecrease = saleService.findById(sale.id()).items().getFirst();
        assertThat(afterDecrease.productId()).isEqualTo(product.getId());
        assertThat(afterDecrease.quantity()).isEqualTo(1);
    }

    @Test
    void shouldFinishSale() {
        SaleDetail sale = saleService.createSale(branch.getId(), user.getId());

        saleService.finishSale(sale.id());

        SaleDetail finished = saleService.findById(sale.id());
        assertThat(finished.status()).isEqualTo(SaleStatus.FINISHED);
    }

    @Test
    void shouldCancelSale() {
        SaleDetail sale = saleService.createSale(branch.getId(), user.getId());

        saleService.cancelSale(sale.id());

        SaleDetail cancelled = saleService.findById(sale.id());
        assertThat(cancelled.status()).isEqualTo(SaleStatus.CANCELLED);
    }
}
