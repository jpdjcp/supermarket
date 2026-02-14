package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.sale.SaleResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductResponse;
import com.supermarket.supermarket_api.exception.SaleItemNotFoundException;
import com.supermarket.supermarket_api.exception.SaleNotFoundException;
import com.supermarket.supermarket_api.exception.SaleNotOpenException;
import com.supermarket.supermarket_api.mapper.SaleItemMapper;
import com.supermarket.supermarket_api.mapper.SaleMapper;
import com.supermarket.supermarket_api.model.*;
import com.supermarket.supermarket_api.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    @InjectMocks
    SaleService saleService;

    @Mock
    SaleRepository saleRepository;

    @Mock
    BranchService branchService;

    @Mock
    ProductService productService;

    @Mock
    SaleMapper saleMapper;

    @Mock
    SaleItemMapper itemMapper;

    private Branch branch;
    private Sale sale;
    private Product product;
    private AddProductRequest addRequest;
    private AddProductResponse addResponse;

    @BeforeEach
    void setUp() {
        branch = new Branch("Branch address");
        sale = new Sale(branch);
        product = new Product("Milk", BigDecimal.valueOf(100));
        addRequest = new AddProductRequest(1L);
        addResponse = new AddProductResponse(
                1L,
                1L,
                1,
                BigDecimal.valueOf(100)
        );
    }

    @Test
    void createSale_shouldCreateSaleForBranch() {
        SaleResponse response = new SaleResponse(
                1L,
                branch.getId(),
                SaleStatus.OPEN,
                List.of(),
                BigDecimal.valueOf(1000)
        );

        when(branchService.findRequiredById(1L)).thenReturn(branch);
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        when(saleMapper.toResponse(any(Sale.class))).thenReturn(response);

        SaleResponse result = saleService.createSale(1L);

        assertThat(result).isNotNull();
        verify(branchService).findRequiredById(1L);
        verify(saleRepository).save(any(Sale.class));
    }

    @Test
    void addProduct_ShouldAddProduct() {
        Product product = new Product("Product name", BigDecimal.valueOf(1000));
        AddProductResponse response = new AddProductResponse(
                1L,
                1L,
                1,
                BigDecimal.valueOf(1000)
        );

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productService.findRequiredById(1L)).thenReturn(product);
        when(itemMapper.toResponse(any(SaleItem.class))).thenReturn(response);

        AddProductResponse result = saleService.addProduct(1L, addRequest);

        assertThat(result).isNotNull();
        verify(saleRepository).findById(1L);
        verify(productService).findRequiredById(1L);
        verify(itemMapper).toResponse(any(SaleItem.class));
    }

    @Test
    void addProductTwice_shouldIncreaseQuantity() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productService.findRequiredById(1L)).thenReturn(product);

        saleService.addProduct(1L, addRequest);
        saleService.addProduct(1L, addRequest);
        SaleItem item = saleService.getItem(1L, 1L);

        assertThat(item.getQuantity()).isEqualTo(2);
    }

    @Test
    void removeProductNotPresent_shouldThrow() {
        assertThatThrownBy(()-> sale.removeProduct(product))
                .isInstanceOf(SaleItemNotFoundException.class);
    }

    @Test
    void increaseQuantity_whenProductNotPresent_shouldThrow() {
        assertThatThrownBy(()->sale.increaseQuantity(product))
                .isInstanceOf(SaleItemNotFoundException.class);
    }

    @Test
    void decreaseQuantityBellowOne_shouldRemove() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productService.findRequiredById(1L)).thenReturn(product);

        saleService.addProduct(1L, addRequest);
        saleService.decreaseQuantity(1L, 1L);

        assertThat(saleService.containsProduct(1L, 1L))
                .isFalse();
    }

    @Test
    void findById_ShouldFindSale() {
        SaleResponse response = new SaleResponse(
                1L,
                1L,
                SaleStatus.OPEN,
                List.of(),
                BigDecimal.valueOf(1000)
        );

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleMapper.toResponse(sale)).thenReturn(response);

        SaleResponse result = saleService.findById(1L);

        assertThat(result).isNotNull();
        verify(saleRepository).findById(1L);
        verify(saleMapper).toResponse(any(Sale.class));
    }

    @Test
    void findByInvalidId_shouldThrow() {
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> saleService.findById(1L))
                .isInstanceOf(SaleNotFoundException.class);

        verify(saleRepository).findById(1L);
    }

    // *** State Mutation Tests ***

    @Test
    void addProductToSale_whenOpen_shouldAddProduct() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productService.findRequiredById(1L)).thenReturn(product);
        when(itemMapper.toResponse(any(SaleItem.class))).thenReturn(addResponse);

        AddProductResponse result = saleService.addProduct(1L, addRequest);

        assertThat(result).isEqualTo(addResponse);
    }

    @Test
    void addProductToSale_whenFinished_shouldThrow() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        sale.finish();
        assertThatThrownBy(() -> saleService.addProduct(1L, addRequest))
                .isInstanceOf(SaleNotOpenException.class);
    }

    @Test
    void addProductToSale_whenCancelled_shouldThrow() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        sale.cancel();
        assertThatThrownBy(() -> saleService.addProduct(1L, addRequest))
                .isInstanceOf(SaleNotOpenException.class);
    }

    @Test
    void removeProductFromSale_whenOpen_shouldRemoveProduct() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productService.findRequiredById(1L)).thenReturn(product);
        when(itemMapper.toResponse(any(SaleItem.class))).thenReturn(addResponse);

        saleService.addProduct(1L, addRequest);
        saleService.removeProduct(1L, 1L);

        assertThat(sale.getSaleItems().isEmpty()).isTrue();
    }

    @Test
    void removeProductFromSale_whenFinished_shouldThrow() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productService.findRequiredById(1L)).thenReturn(product);
        when(itemMapper.toResponse(any(SaleItem.class))).thenReturn(addResponse);

        saleService.addProduct(1L, addRequest);
        sale.finish();

        assertThatThrownBy(() -> saleService.removeProduct(1L, 1L))
                .isInstanceOf(SaleNotOpenException.class);
    }

    @Test
    void removeProductFromSale_whenCancelled_shouldThrow() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productService.findRequiredById(1L)).thenReturn(product);
        when(itemMapper.toResponse(any(SaleItem.class))).thenReturn(addResponse);

        saleService.addProduct(1L, addRequest);
        sale.cancel();

        assertThatThrownBy(() -> saleService.removeProduct(1L, 1L))
                .isInstanceOf(SaleNotOpenException.class);
    }

    @Test
    void increaseProductQuantity_whenOpen_shouldIncreaseQuantity() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productService.findRequiredById(1L)).thenReturn(product);

        saleService.addProduct(1L, addRequest);
        saleService.increaseQuantity(1L, 1L);
        SaleItem item = sale.getSaleItems().getFirst();

        assertThat(item.getQuantity()).isEqualTo(2);
    }

    @Test
    void increaseProductQuantity_whenFinished_shouldThrow() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productService.findRequiredById(1L)).thenReturn(product);

        saleService.addProduct(1L, addRequest);
        sale.finish();

        assertThatThrownBy(() -> saleService.increaseQuantity(1L, 1L))
                .isInstanceOf(SaleNotOpenException.class);
    }

    @Test
    void increaseProductQuantity_whenCancelled_shouldThrow() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productService.findRequiredById(1L)).thenReturn(product);

        saleService.addProduct(1L, addRequest);
        sale.cancel();

        assertThatThrownBy(() -> saleService.increaseQuantity(1L, 1L))
                .isInstanceOf(SaleNotOpenException.class);
    }

    @Test
    void decreaseProductQuantity_whenOpen_shouldDecreaseQuantity() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productService.findRequiredById(1L)).thenReturn(product);

        saleService.addProduct(1L, addRequest);
        saleService.addProduct(1L, addRequest);
        saleService.decreaseQuantity(1L, 1L);
        SaleItem item = sale.getSaleItems().getFirst();

        assertThat(item.getQuantity()).isEqualTo(1);
    }

    @Test
    void decreaseProductQuantity_whenFinished_shouldThrow() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        sale.finish();

        assertThatThrownBy(() -> saleService.decreaseQuantity(1L, 1L))
                .isInstanceOf(SaleNotOpenException.class);
    }

    @Test
    void decreaseProductQuantity_whenCancelled_shouldThrow() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        sale.cancel();

        assertThatThrownBy(() -> saleService.decreaseQuantity(1L, 1L))
                .isInstanceOf(SaleNotOpenException.class);
    }
}
