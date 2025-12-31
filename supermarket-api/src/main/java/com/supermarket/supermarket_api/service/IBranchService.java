package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.BranchDTO;

import java.util.List;

public interface IBranchService {
    List<BranchDTO> list();
    BranchDTO save(BranchDTO branchDTO);
    BranchDTO get(Long id);
    BranchDTO update(Long id, BranchDTO dto);
    void delete(Long id);
}
