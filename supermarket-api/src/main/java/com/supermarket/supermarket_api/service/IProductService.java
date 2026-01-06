package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.ProductDTO;
import com.supermarket.supermarket_api.model.Product;

import java.util.List;

public interface IProductService {
    List<ProductDTO> findAll();
    ProductDTO create(ProductDTO productDTO);
    ProductDTO findById(Long id);
    Product getRequiredById(Long id);
    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);
}
