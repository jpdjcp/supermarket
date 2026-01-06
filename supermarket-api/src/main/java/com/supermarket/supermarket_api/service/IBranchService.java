package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.BranchDTO;
import com.supermarket.supermarket_api.model.Branch;

import java.util.List;

public interface IBranchService {
    List<BranchDTO> list();
    BranchDTO save(BranchDTO branchDTO);
    BranchDTO findById(Long id);
    Branch findEntityById(Long id);
    BranchDTO update(Long id, BranchDTO dto);
    void delete(Long id);
}
