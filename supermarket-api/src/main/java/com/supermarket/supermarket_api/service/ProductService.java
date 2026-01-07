package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.ProductCreateRequest;
import com.supermarket.supermarket_api.dto.ProductResponse;
import com.supermarket.supermarket_api.dto.ProductUpdateRequest;
import com.supermarket.supermarket_api.exception.ProductNotFoundException;
import com.supermarket.supermarket_api.model.Product;
import com.supermarket.supermarket_api.mapper.ProductMapper;
import com.supermarket.supermarket_api.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

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
    public ProductResponse findById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return mapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findRequiredById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
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
    public ProductResponse updatePrice(Long id, ProductUpdateRequest dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.changePrice(dto.price());
        return mapper.toResponse(product);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = repository.findById(id)
                        .orElseThrow(() -> new ProductNotFoundException(id));

        repository.delete(product);
    }
}
