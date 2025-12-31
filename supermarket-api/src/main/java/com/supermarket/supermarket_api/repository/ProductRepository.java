package com.supermarket.supermarket_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.supermarket.supermarket_api.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
