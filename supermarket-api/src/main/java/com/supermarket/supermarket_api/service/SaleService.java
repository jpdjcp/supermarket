package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.AddItemRequest;
import com.supermarket.supermarket_api.dto.SaleDTO;
import com.supermarket.supermarket_api.dto.SaleItemDTO;
import com.supermarket.supermarket_api.mapper.BranchMapper;
import com.supermarket.supermarket_api.mapper.ItemMapper;
import com.supermarket.supermarket_api.mapper.SaleMapper;
import com.supermarket.supermarket_api.model.Branch;
import com.supermarket.supermarket_api.model.Product;
import com.supermarket.supermarket_api.model.SaleItem;
import com.supermarket.supermarket_api.model.Sale;
import com.supermarket.supermarket_api.repository.SaleRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService implements ISaleService {

    private final SaleRepository repository;
    private final BranchService branchService;
    private final ProductService productService;
    private final SaleMapper saleMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SaleDTO> list() {
        return repository.findAll().stream()
                .map(saleMapper::mapToDTO)
                .toList();
    }

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
        Branch branch = branchService.getEntity(branchId);

        Sale sale = new Sale(branch);
        repository.save(sale);

        return saleMapper.mapToDTO(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleDTO get(Long id) {
        return repository.findById(id)
                .map(saleMapper::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
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
    @Transactional
    public SaleItemDTO addItem(Long saleId, AddItemRequest request) {
        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        Product product = productService.getEntityById(request.productId());

        SaleItem item = new SaleItem(
                null,
                sale,
                product,
                request.quantity(),
                product.getPrice() * request.quantity()
        );

        sale.getSaleItems().add(item);
        sale.setTotal(sale.getTotal() + item.getSubtotal());

        repository.save(sale);

        return ItemMapper.mapToDTO(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleItemDTO> getItemsBySale(Long saleId) {
        Sale sale = repository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        return sale.getSaleItems()
                .stream()
                .map(ItemMapper::mapToDTO)
                .toList();
    }
}
