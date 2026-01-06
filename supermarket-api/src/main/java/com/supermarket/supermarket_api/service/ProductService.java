package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.ProductDTO;
import com.supermarket.supermarket_api.exception.ProductNotFoundException;
import com.supermarket.supermarket_api.model.Product;
import com.supermarket.supermarket_api.mapper.ProductMapper;
import com.supermarket.supermarket_api.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ProductDTO create(ProductDTO dto) {
        Product product = ProductMapper.mapToProduct(dto);
        Product saved = repository.save(product);
        return ProductMapper.mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> result = repository.findById(id);
        return result.map(ProductMapper::mapToDTO)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product getRequiredById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return repository.findAll().stream()
                .map(ProductMapper::mapToDTO)
                .toList();
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Optional<Product> result = repository.findById(id);
        if (result.isPresent()) {
            Product product = result.get();
            product.changePrice(dto.price());
            return ProductMapper.mapToDTO(repository.save(product));
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
