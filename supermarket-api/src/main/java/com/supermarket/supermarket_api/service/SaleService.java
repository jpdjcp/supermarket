package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.sale.SaleResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.SaleItemResponse;
import com.supermarket.supermarket_api.exception.SaleNotFoundException;
import com.supermarket.supermarket_api.mapper.SaleItemMapper;
import com.supermarket.supermarket_api.mapper.SaleMapper;
import com.supermarket.supermarket_api.model.Branch;
import com.supermarket.supermarket_api.model.Product;
import com.supermarket.supermarket_api.model.Sale;
import com.supermarket.supermarket_api.model.SaleItem;
import com.supermarket.supermarket_api.repository.SaleRepository;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SaleService implements ISaleService {

    private final SaleRepository repository;
    private final BranchService branchService;
    private final ProductService productService;
    private final SaleMapper saleMapper;
    private final SaleItemMapper itemMapper;

    @Override
    @Transactional
    public SaleResponse createSale(Long branchId) {
        Branch branch = branchService.findRequiredById(branchId);
        Sale sale = new Sale(branch);
        return saleMapper.toResponse(repository.save(sale));
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
    public List<SaleItemResponse> getItems(Long id) {
        Sale sale = findRequiredSale(id);

        return sale.getSaleItems().stream()
                .map(itemMapper::toSaleItemResponse)
                .toList();
    }

    @Override
    @Transactional
    public AddProductResponse addProduct(Long id, AddProductRequest request) {
        Sale sale = findRequiredSale(id);

        Product product = productService.findRequiredById(request.productId());
        SaleItem item = sale.addProduct(product);

        return itemMapper.toResponse(item);
    }

    @Override
    @Transactional
    public void removeProduct(Long id, Long productId) {
        Sale sale = findRequiredSale(id);

        Product product = productService.findRequiredById(productId);
        sale.removeProduct(product);
    }

    @Override
    @Transactional
    public void increaseQuantity(Long id, Long productId) {
        Sale sale = findRequiredSale(id);

        Product product = productService.findRequiredById(productId);
        sale.increaseQuantity(product);
    }


    @Override
    @Transactional
    public void decreaseQuantity(Long id, Long productId) {
        Sale sale = findRequiredSale(id);

        Product product = productService.findRequiredById(productId);
        sale.decreaseQuantity(product);
    }

    @Override
    @Transactional
    public SaleResponse finishSale(Long saleId) {
        Sale sale = findRequiredSale(saleId);
        sale.finish();
        return saleMapper.toResponse(sale);
    }

    @Override
    @Transactional
    public SaleResponse cancelSale(Long saleId) {
        Sale sale = findRequiredSale(saleId);
        sale.cancel();
        return saleMapper.toResponse(sale);
    }

    @Nonnull
    private Sale findRequiredSale(Long saleId) {
        return repository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));
    }
}
