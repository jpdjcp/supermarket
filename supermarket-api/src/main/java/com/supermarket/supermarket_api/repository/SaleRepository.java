package com.supermarket.supermarket_api.repository;

import com.supermarket.supermarket_api.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findAllByBranchId(Long branchId);
}
