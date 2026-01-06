package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.ProductDTO;
import com.supermarket.supermarket_api.exception.ProductNotFoundException;
import com.supermarket.supermarket_api.model.Product;
import com.supermarket.supermarket_api.mapper.ProductMapper;
import com.supermarket.supermarket_api.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> list() {
        return repository.findAll().stream()
                .map(ProductMapper::mapToDTO)
                .toList();
    }

    @Override
    @Transactional
    public ProductDTO save(ProductDTO dto) {
        Product result = repository.save(ProductMapper.mapToProduct(dto));
        return ProductMapper.mapToDTO(result);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO get(Long id) {
        Optional<Product> result = repository.findById(id);
        return result.map(ProductMapper::mapToDTO)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Optional<Product> result = repository.findById(id);
        if (result.isPresent()) {
            Product product = result.get();
            product.setName(dto.name());
            product.setPrice(dto.price());
            return ProductMapper.mapToDTO(repository.save(product));
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Product getEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
