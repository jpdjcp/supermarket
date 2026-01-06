package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.BranchDTO;
import com.supermarket.supermarket_api.model.Branch;

import java.util.List;

public interface IBranchService {
    List<BranchDTO> findAll();
    BranchDTO create(BranchDTO branchDTO);
    BranchDTO findById(Long id);
    Branch getRequiredById(Long id);
    void delete(Long id);
}
