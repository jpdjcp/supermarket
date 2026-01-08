package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.AddItemRequest;
import com.supermarket.supermarket_api.dto.SaleResponse;
import com.supermarket.supermarket_api.dto.SaleItemResponse;
import com.supermarket.supermarket_api.exception.SaleNotFoundException;
import com.supermarket.supermarket_api.mapper.SaleItemMapper;
import com.supermarket.supermarket_api.mapper.SaleMapper;
import com.supermarket.supermarket_api.model.Branch;
import com.supermarket.supermarket_api.model.Product;
import com.supermarket.supermarket_api.model.SaleItem;
import com.supermarket.supermarket_api.model.Sale;
import com.supermarket.supermarket_api.repository.SaleRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService implements ISaleService {

    private final SaleRepository repository;
    private final BranchService branchService;
    private final ProductService productService;
    private final SaleMapper saleMapper;
    private final SaleItemMapper saleItemMapper;

    public SaleService(
            SaleRepository repository,
            BranchService branchService,
            ProductService productService,
            SaleMapper saleMapper,
            SaleItemMapper saleItemMapper) {
        this.repository = repository;
        this.branchService = branchService;
        this.productService = productService;
        this.saleMapper = saleMapper;
        this.saleItemMapper = saleItemMapper;
    }

    @Override
    @Transactional
    public SaleResponse createSale(@NotNull Long branchId) {
        Branch branch = branchService.findRequiredById(branchId);

        Sale sale = new Sale(branch);
        repository.save(sale);

        return saleMapper.toResponse(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse findById(Long id) {
        return repository.findById(id)
                .map(saleMapper::toResponse)
                .orElseThrow(() -> new SaleNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getSalesByBranch(Long branchId) {
        return repository.findByBranchId(branchId)
                .stream()
                .map(saleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleItemResponse> getProducts(Long saleId) {

        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        return sale.getSaleItems()
                .stream()
                .map(saleItemMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public SaleItemResponse addProduct(Long id, AddItemRequest request) {

        Sale sale = repository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(id));

        Product product = productService.findRequiredById(request.productId());
        SaleItem item = sale.addSaleItem(product, request.quantity());
        repository.save(sale);

        return saleItemMapper.toResponse(item);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id, Long itemId) {

    }
}
