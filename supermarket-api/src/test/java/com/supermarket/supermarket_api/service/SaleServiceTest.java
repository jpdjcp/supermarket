package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.sale.SaleResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductResponse;
import com.supermarket.supermarket_api.exception.SaleNotFoundException;
import com.supermarket.supermarket_api.mapper.SaleItemMapper;
import com.supermarket.supermarket_api.mapper.SaleMapper;
import com.supermarket.supermarket_api.model.Branch;
import com.supermarket.supermarket_api.model.Product;
import com.supermarket.supermarket_api.model.Sale;
import com.supermarket.supermarket_api.model.SaleItem;
import com.supermarket.supermarket_api.repository.SaleRepository;
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

    @Test
    void createSale_shouldCreateSaleForBranch() {

        Branch branch = new Branch("Branch address");
        Sale sale = new Sale(branch);
        SaleResponse response = new SaleResponse(1L, branch.getId(), List.of(), BigDecimal.valueOf(1000));

        when(branchService.findRequiredById(1L)).thenReturn(branch);
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        when(saleMapper.toResponse(any(Sale.class))).thenReturn(response);

        SaleResponse result = saleService.createSale(1L);

        assertThat(result).isNotNull();
        verify(branchService).findRequiredById(1L);
        verify(saleRepository).save(any(Sale.class));
    }

    @Test
    void addProductTest() {
        Branch branch = new Branch("Branch address");
        Sale sale = new Sale(branch);
        Product product = new Product("Product name", BigDecimal.valueOf(1000));
        AddProductResponse response = new AddProductResponse(
                1L,
                1L,
                1,
                BigDecimal.valueOf(1000));
        AddProductRequest request = new AddProductRequest(1L);

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productService.findRequiredById(1L)).thenReturn(product);
        when(itemMapper.toResponse(any(SaleItem.class))).thenReturn(response);

        AddProductResponse result = saleService.addProduct(1L, request);

        assertThat(result).isNotNull();
        verify(saleRepository).findById(1L);
        verify(productService).findRequiredById(1L);
        verify(itemMapper).toResponse(any(SaleItem.class));
    }

    @Test
    void findByIdTest() {
        //setup
        Branch branch = new Branch("Branch address");
        Sale sale = new Sale(branch);
        SaleResponse response = new SaleResponse(1L, 1L, List.of(), BigDecimal.valueOf(1000));

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleMapper.toResponse(sale)).thenReturn(response);

        SaleResponse result = saleService.findById(1L);

        assertThat(result).isNotNull();
        verify(saleRepository).findById(1L);
        verify(saleMapper).toResponse(any(Sale.class));
    }

    @Test
    void findByInvalidId() {
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> saleService.findById(1L))
                .isInstanceOf(SaleNotFoundException.class);

        verify(saleRepository).findById(1L);
    }
}
