package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.ProductDTO;

import java.util.List;

public interface IProductService {
    List<ProductDTO> list();
    ProductDTO save(ProductDTO productDTO);
    ProductDTO get(Long id);
    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);
}
