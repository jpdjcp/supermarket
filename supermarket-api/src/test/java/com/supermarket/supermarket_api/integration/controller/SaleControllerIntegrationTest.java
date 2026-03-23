package com.supermarket.supermarket_api.integration.controller;

import com.supermarket.supermarket_api.dto.sale.SaleCreateRequest;
import com.supermarket.supermarket_api.dto.sale.SaleDetail;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.ItemResponse;
import com.supermarket.supermarket_api.integration.AbstractIntegrationTest;
import com.supermarket.supermarket_api.model.*;
import com.supermarket.supermarket_api.repository.BranchRepository;
import com.supermarket.supermarket_api.repository.ProductRepository;
import com.supermarket.supermarket_api.repository.SaleRepository;
import com.supermarket.supermarket_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;

@Transactional
public class SaleControllerIntegrationTest extends AbstractIntegrationTest {

    private final String BASE_URL = "/api/v1/sales";

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
    private Sale sale;
    private String token;

    @BeforeEach
    void setup() throws Exception {
        branch = branchRepository.save(new Branch("Branch Address"));
        product = productRepository
                .save(new Product("ABC-1234", "Milk", BigDecimal.valueOf(10)));

        String username = "user-" + System.currentTimeMillis();
        String password = "password";

        token = obtainAccessToken(username, password);
        user = userRepository.findByUsername(username).orElseThrow();
    }

    @Test
    void shouldCreateSale() throws Exception {
        SaleCreateRequest request = new SaleCreateRequest(branch.getId());

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        SaleDetail response = objectMapper.readValue(
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

        Sale saved = saleRepository.findById(response.id()).orElseThrow();
        assertThat(saved.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void shouldGetSaleById() throws Exception {
        sale = saleRepository.save(new Sale(branch, user));

        URI uri = URI.create("%s/%d".formatted(BASE_URL, sale.getId()));

        MvcResult result = mockMvc.perform(get(uri)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        SaleDetail response = objectMapper.readValue(
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
        AddProductRequest addRequest = new AddProductRequest(product.getId());
        sale = saleRepository.save(new Sale(branch, user));

        URI uri = URI.create("%s/%d/items".formatted(BASE_URL, sale.getId()));

        MvcResult result = mockMvc.perform(post(uri)
                        .header("Authorization", "Bearer " + token)
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

        mockMvc.perform(delete(uri)
                        .header("Authorization", "Bearer " + token))
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

        mockMvc.perform(patch(uri)
                        .header("Authorization", "Bearer " + token))
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

        mockMvc.perform(patch(uri)
                        .header("Authorization", "Bearer " + token))
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

        MvcResult result = mockMvc.perform(post(uri)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        SaleDetail response = objectMapper.readValue(
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

        MvcResult result = mockMvc.perform(post(uri)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        SaleDetail response = objectMapper.readValue(
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
