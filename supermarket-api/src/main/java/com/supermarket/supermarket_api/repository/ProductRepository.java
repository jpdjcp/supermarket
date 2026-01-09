package com.supermarket.supermarket_api.repository;

import com.supermarket.supermarket_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
