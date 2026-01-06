package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.AddItemRequest;
import com.supermarket.supermarket_api.dto.SaleDTO;
import com.supermarket.supermarket_api.dto.SaleItemDTO;
import com.supermarket.supermarket_api.exception.SaleNotFoundException;
import com.supermarket.supermarket_api.mapper.ItemMapper;
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
    public SaleDTO createSale(@NotNull Long branchId) {
        Branch branch = branchService.getRequiredById(branchId);

        Sale sale = new Sale(branch);
        repository.save(sale);

        return saleMapper.mapToDTO(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleDTO getById(Long id) {
        return repository.findById(id)
                .map(saleMapper::mapToDTO)
                .orElseThrow(() -> new SaleNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleDTO> findAll() {
        return repository.findAll().stream()
                .map(saleMapper::mapToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleDTO> getSalesByBranch(Long branchId) {
        return repository.findByBranchId(branchId)
                .stream()
                .map(saleMapper::mapToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleItemDTO> getItemsBySale(Long saleId) {

        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        return sale.getSaleItems()
                .stream()
                .map(ItemMapper::mapToDTO)
                .toList();
    }

    @Override
    @Transactional
    public SaleItemDTO addItem(Long saleId, AddItemRequest request) {

        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));

        Product product = productService.getRequiredById(request.productId());
        SaleItem item = sale.addItem(product, request.quantity());
        repository.save(sale);

        return ItemMapper.mapToDTO(item);
    }
}
