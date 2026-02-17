package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.product.ProductCreateRequest;
import com.supermarket.supermarket_api.dto.product.ProductResponse;
import com.supermarket.supermarket_api.dto.product.ProductUpdateRequest;
import com.supermarket.supermarket_api.exception.ProductNotFoundException;
import com.supermarket.supermarket_api.mapper.ProductMapper;
import com.supermarket.supermarket_api.model.Product;
import com.supermarket.supermarket_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private final String SKU = "ABCD-1234";

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    private Product product;
    private ProductCreateRequest request;
    private ProductResponse response;
    private ProductUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        product = new Product(SKU, "Milk", BigDecimal.valueOf(10));
        request = new ProductCreateRequest(SKU, "Milk", BigDecimal.valueOf(10));
        response = new ProductResponse(1L, SKU,  "Milk", BigDecimal.valueOf(10));
        updateRequest = new ProductUpdateRequest(BigDecimal.valueOf(20));
    }

    @Test
    void create_shouldReturnAValidProductResponse() {
        when(mapper.toProduct(any(ProductCreateRequest.class)))
                .thenReturn(product);
        when(repository.save(any(Product.class)))
                .thenReturn(product);
        when(mapper.toResponse(any(Product.class)))
                .thenReturn(response);

        ProductResponse result = service.create(request);

        verify(mapper).toProduct(request);
        verify(repository).save(product);
        verify(mapper).toResponse(product);
        assertThat(result.name()).isEqualTo(request.name());
        assertThat(result.price()).isEqualTo(request.price());
    }

    @Test
    void findRequiredById_invalidId_shouldThrow() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findRequiredById(1L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void updatePrice_shouldUpdatePrice() {
        response = new ProductResponse(1L, SKU,  "Milk", BigDecimal.valueOf(20));

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(mapper.toResponse(product)).thenReturn(response);

        ProductResponse result = service.updatePrice(1L, updateRequest);

        assertThat(product.getPrice()).isEqualTo(response.price());
        verify(repository).findById(1L);
        verify(mapper).toResponse(product);
    }

    @Test
    void updatePrice_invalidId_shouldThrow() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updatePrice(1L, updateRequest))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void delete_shouldDelete() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        service.delete(1L);

        verify(repository).findById(1L);
        verify(repository).delete(product);
    }

    @Test
    void delete_invalidId_shouldThrow() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
