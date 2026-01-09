package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.branch.BranchCreateRequest;
import com.supermarket.supermarket_api.dto.branch.BranchResponse;
import com.supermarket.supermarket_api.dto.sale.SaleResponse;
import com.supermarket.supermarket_api.model.Branch;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface IBranchService {
    BranchResponse create(BranchCreateRequest branchCreateRequest);
    BranchResponse findById(Long id);
    Branch findRequiredById(Long id);
    List<BranchResponse> findAll();
    void delete(Long id);
    List<SaleResponse> getSales(@Positive Long id);
}
