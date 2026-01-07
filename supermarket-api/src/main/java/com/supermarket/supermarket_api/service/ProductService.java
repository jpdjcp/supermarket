package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.ProductCreateRequest;
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

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ProductCreateRequest create(ProductCreateRequest dto) {
        Product product = ProductMapper.toProduct(dto);
        Product saved = repository.save(product);
        return ProductMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCreateRequest findById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return ProductMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public Product getRequiredById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCreateRequest> findAll() {
        return repository.findAll().stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProductCreateRequest update(Long id, ProductCreateRequest dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.changePrice(dto.price());
        return ProductMapper.toResponse(product);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = repository.findById(id)
                        .orElseThrow(() -> new ProductNotFoundException(id));

        repository.delete(product);
    }
}
