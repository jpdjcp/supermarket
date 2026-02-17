package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.product.ProductCreateRequest;
import com.supermarket.supermarket_api.dto.product.ProductResponse;
import com.supermarket.supermarket_api.dto.product.ProductUpdateRequest;
import com.supermarket.supermarket_api.exception.ProductNotFoundException;
import com.supermarket.supermarket_api.mapper.ProductMapper;
import com.supermarket.supermarket_api.model.Product;
import com.supermarket.supermarket_api.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public ProductResponse create(ProductCreateRequest dto) {
        Product product = mapper.toProduct(dto);
        Product saved = repository.save(product);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Long productId) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return mapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findRequiredById(Long productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findBySKU(String sku) {
        Product product = repository.findBySKU(sku)
                .orElseThrow(()-> new ProductNotFoundException(sku));
        return mapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse updatePrice(Long productId, ProductUpdateRequest dto) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.changePrice(dto.price());
        return mapper.toResponse(product);
    }

    @Override
    @Transactional
    public void delete(Long productId) {
        Product product = repository.findById(productId)
                        .orElseThrow(() -> new ProductNotFoundException(productId));

        repository.delete(product);
    }
}
