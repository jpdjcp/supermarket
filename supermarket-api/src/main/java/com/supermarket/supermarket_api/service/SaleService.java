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

    public SaleService(
            SaleRepository repository,
            BranchService branchService,
            ProductService productService,
            SaleMapper saleMapper) {
        this.repository = repository;
        this.branchService = branchService;
        this.productService = productService;
        this.saleMapper = saleMapper;
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
    public SaleResponse getById(Long id) {
        return repository.findById(id)
                .map(saleMapper::toResponse)
                .orElseThrow(() -> new SaleNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> findAll() {
        return repository.findAll().stream()
                .map(saleMapper::toResponse)
                .toList();
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
    public List<SaleItemResponse> getItemsBySale(Long saleId) {

        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        return sale.getSaleItems()
                .stream()
                .map(SaleItemMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public SaleItemResponse addItem(Long saleId, AddItemRequest request) {

        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        Product product = productService.findRequiredById(request.productId());
        SaleItem item = sale.addSaleItem(product, request.quantity());
        repository.save(sale);

        return SaleItemMapper.toResponse(item);
    }
}
