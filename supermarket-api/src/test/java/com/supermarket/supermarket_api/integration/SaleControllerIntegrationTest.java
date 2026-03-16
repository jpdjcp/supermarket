package com.supermarket.supermarket_api.integration;

import com.supermarket.supermarket_api.dto.sale.SaleCreateRequest;
import com.supermarket.supermarket_api.dto.sale.SaleDetail;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.ItemResponse;
import com.supermarket.supermarket_api.model.*;
import com.supermarket.supermarket_api.repository.BranchRepository;
import com.supermarket.supermarket_api.repository.ProductRepository;
import com.supermarket.supermarket_api.repository.SaleRepository;
import com.supermarket.supermarket_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SaleControllerIntegrationTest extends AbstractIntegrationTest {

    private final String BASE_URL = "/api/v1/sales";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    private SaleCreateRequest request;
    private SaleDetail response;
    private AddProductRequest addRequest;
    private MvcResult result;
    private Sale sale;

    @BeforeEach
    void setup() {
        String address = "Branch Address";
        branch = branchRepository.save(new Branch(address));

        String sku = "ABC-1234";
        String name = "Milk";
        BigDecimal price = BigDecimal.valueOf(10);
        product = productRepository.save(new Product(sku, name, price));

        String username = "John Jackson";
        String password = "vjklznv43xv3213d";
        UserRole role = UserRole.ROLE_USER;
        user = userRepository.save(new User(username, password));
    }

    @Test
    void shouldCreateSale() throws Exception {
        request = new SaleCreateRequest(branch.getId(), user.getId());

        result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SaleDetail.class);

        assertThat(response.id()).isNotNull();
        assertThat(response.branchId()).isEqualTo(branch.getId());
        assertThat(response.userId()).isEqualTo(user.getId());
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.closedAt()).isNull();
        assertThat(response.status()).isEqualTo(SaleStatus.OPEN);
        assertThat(response.items()).isEmpty();
        assertThat(response.total()).isZero();
    }

    @Test
    void shouldGetSaleById() throws Exception {
        sale = saleRepository.save(new Sale(branch, user));

        URI uri = URI.create("%s/%d".formatted(BASE_URL, sale.getId()));

        result = mockMvc
                .perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SaleDetail.class);

        assertThat(response.id()).isEqualTo(sale.getId());
        assertThat(response.branchId()).isEqualTo(branch.getId());
        assertThat(response.userId()).isEqualTo(user.getId());
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.closedAt()).isNull();
        assertThat(response.status()).isEqualTo(SaleStatus.OPEN);
        assertThat(response.items()).isEmpty();
        assertThat(response.total()).isZero();
    }

    @Test
    void shouldAddProductToSale() throws Exception {
        addRequest = new AddProductRequest(product.getId());
        sale = saleRepository.save(new Sale(branch, user));

        URI uri = URI.create("%s/%d/items".formatted(BASE_URL, sale.getId()));

        result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        ItemResponse itemResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ItemResponse.class);

        Sale saved = saleRepository.findById(sale.getId()).orElseThrow();
        assertThat(saved.calculateTotal()).isEqualTo(BigDecimal.valueOf(10));

        assertThat(itemResponse.saleId()).isEqualTo(sale.getId());
        assertThat(itemResponse.productId()).isEqualTo(product.getId());
        assertThat(itemResponse.quantity()).isEqualTo(1);
        assertThat(itemResponse.subtotal()).isEqualTo(BigDecimal.valueOf(10));
    }

    @Test
    void shouldRemoveProductFromSale() throws Exception {
        sale = saleRepository.save(new Sale(branch, user));
        sale.addProduct(product);
        Sale afterAdd = saleRepository.findById(sale.getId()).orElseThrow();
        assertThat(afterAdd.containsProduct(product.getId())).isTrue();

        URI uri = URI.create("%s/%d/items/%d"
                .formatted(BASE_URL, sale.getId(), product.getId()));

        mockMvc.perform(delete(uri))
                .andExpect(status().isNoContent())
                .andReturn();

        Sale afterRemove = saleRepository.findById(sale.getId()).orElseThrow();
        assertThat(afterRemove.getId()).isEqualTo(sale.getId());
        assertThat(afterRemove.getSaleItems()).isEmpty();
        assertThat(afterRemove.calculateTotal()).isZero();
    }

    @Test
    void shouldIncreaseQuantity() throws Exception {
        sale = saleRepository.save(new Sale(branch, user));
        sale.addProduct(product);

        URI uri = URI.create("%s/%d/items/%d/increase"
                .formatted(BASE_URL, sale.getId(), product.getId()));

        mockMvc.perform(patch(uri))
                .andExpect(status().isNoContent())
                .andReturn();

        Sale saved = saleRepository.findById(sale.getId()).orElseThrow();
        assertThat(saved.getId()).isEqualTo(sale.getId());
        assertThat(saved.containsProduct(product.getId())).isTrue();
        assertThat(saved.getSaleItems().getFirst().getQuantity()).isEqualTo(2);
        assertThat(saved.calculateTotal()).isEqualTo(BigDecimal.valueOf(20));
    }

    @Test
    void shouldDecreaseQuantity() throws Exception {
        sale = saleRepository.save(new Sale(branch, user));
        sale.addProduct(product);
        sale.increaseQuantity(product);

        URI uri = URI.create("%s/%d/items/%d/decrease"
                .formatted(BASE_URL, sale.getId(), product.getId()));

        mockMvc.perform(patch(uri))
                .andExpect(status().isNoContent())
                .andReturn();

        Sale saved = saleRepository.findById(sale.getId()).orElseThrow();
        assertThat(saved.getId()).isEqualTo(sale.getId());
        assertThat(saved.containsProduct(product.getId())).isTrue();
        assertThat(saved.getSaleItems().getFirst().getQuantity()).isEqualTo(1);
        assertThat(saved.calculateTotal()).isEqualTo(BigDecimal.valueOf(10));
    }

    @Test
    void shouldFinishSale() throws Exception {
        sale = saleRepository.save(new Sale(branch, user));
        URI uri = URI.create("%s/%d/finish"
                .formatted(BASE_URL, sale.getId()));

        result = mockMvc.perform(post(uri))
                .andExpect(status().isOk())
                .andReturn();

        response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SaleDetail.class);

        Sale saved = saleRepository.findById(sale.getId()).orElseThrow();
        assertThat(saved.getStatus()).isEqualTo(SaleStatus.FINISHED);

        assertThat(response.id()).isEqualTo(sale.getId());
        assertThat(response.userId()).isEqualTo(user.getId());
        assertThat(response.branchId()).isEqualTo(branch.getId());
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.closedAt()).isNotNull();
        assertThat(response.status()).isEqualTo(SaleStatus.FINISHED);
    }

    @Test
    void shouldCancelSale() throws Exception {
        sale = saleRepository.save(new Sale(branch, user));
        URI uri = URI.create("%s/%d/cancel"
                .formatted(BASE_URL, sale.getId()));

        result = mockMvc.perform(post(uri))
                .andExpect(status().isOk())
                .andReturn();

        response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SaleDetail.class);

        Sale saved = saleRepository.findById(sale.getId()).orElseThrow();
        assertThat(saved.getStatus()).isEqualTo(SaleStatus.CANCELLED);

        assertThat(response.id()).isEqualTo(sale.getId());
        assertThat(response.userId()).isEqualTo(user.getId());
        assertThat(response.branchId()).isEqualTo(branch.getId());
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.closedAt()).isNotNull();
        assertThat(response.status()).isEqualTo(SaleStatus.CANCELLED);
    }
}
