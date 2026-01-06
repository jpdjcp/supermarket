package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.ProductDTO;

import java.util.List;

public interface IProductService {
    List<ProductDTO> findAll();
    ProductDTO create(ProductDTO productDTO);
    ProductDTO getById(Long id);
    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);
}
