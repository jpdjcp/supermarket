package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.AddItemRequest;
import com.supermarket.supermarket_api.dto.SaleDTO;
import com.supermarket.supermarket_api.mapper.BranchMapper;
import com.supermarket.supermarket_api.mapper.ProductMapper;
import com.supermarket.supermarket_api.mapper.SaleMapper;
import com.supermarket.supermarket_api.model.Item;
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
@RequiredArgsConstructor
public class SaleService implements ISaleService {

    @Autowired
    private SaleRepository repository;

    @Autowired
    private BranchService branchService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private BranchMapper branchMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SaleDTO> list() {
        return repository.findAll().stream()
                .map(sale -> saleMapper.mapToDTO(sale))
                .toList();
    }

    @Override
    @Transactional
    public SaleDTO createSale(@NotNull Long branchId) {
        if (branchService.get(branchId) == null) throw new RuntimeException("Branch not found");
        var branch = branchMapper.mapToBranch(branchService.get(branchId));
        var sale = new Sale(null, branch, new ArrayList<>(), 0.0);
        return saleMapper.mapToDTO(repository.save(sale));
    }

    @Override
    @Transactional(readOnly = true)
    public SaleDTO get(Long id) {
        return repository.findById(id)
                .map(sale -> saleMapper.mapToDTO(sale))
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleDTO> getSalesByBranch(Long branchId) {
        return repository.findAllByBranchId(branchId)
                .stream()
                .map(sale -> saleMapper.mapToDTO(sale))
                .toList();
    }

    @Override
    @Transactional
    public SaleDTO addItem(Long saleId, AddItemRequest request) {
        if (request.quantity() < 1) throw new RuntimeException("Quantity must be greater than 0");
        var sale = repository.findById(saleId);
        var productDTO = productService.get(request.productId());
        if (sale.isEmpty()) throw new RuntimeException("Sale not found");
        if (productDTO == null) return null;

        var item = new Item(
                null,
                sale.get(),
                ProductMapper.mapToProduct(productDTO),
                request.quantity(),
                productDTO.price()* request.quantity()
        );

        sale.get().getItems().add(item);
        sale.get().setPrice(sale.get().getPrice() + item.getSubtotal());
        return saleMapper.mapToDTO(repository.save(sale.get()));
    }
}
