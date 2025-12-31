package com.supermarket.supermarket_api.repository;

import com.supermarket.supermarket_api.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}
