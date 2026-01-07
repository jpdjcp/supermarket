package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.BranchCreateRequest;
import com.supermarket.supermarket_api.dto.BranchResponse;
import com.supermarket.supermarket_api.model.Branch;

import java.util.List;

public interface IBranchService {
    BranchResponse create(BranchCreateRequest branchCreateRequest);
    BranchResponse findById(Long id);
    Branch findRequiredById(Long id);
    List<BranchResponse> findAll();
    void delete(Long id);
}
