package com.supermarket.supermarket_api.repository;

import com.supermarket.supermarket_api.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> { }
