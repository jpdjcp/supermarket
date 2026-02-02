package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.branch.BranchCreateRequest;
import com.supermarket.supermarket_api.dto.branch.BranchResponse;
import com.supermarket.supermarket_api.dto.sale.SaleResponse;
import com.supermarket.supermarket_api.exception.BranchNotFoundException;
import com.supermarket.supermarket_api.mapper.BranchMapper;
import com.supermarket.supermarket_api.mapper.SaleMapper;
import com.supermarket.supermarket_api.model.Branch;
import com.supermarket.supermarket_api.repository.BranchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BranchService implements IBranchService {

    private final BranchRepository repository;
    private final BranchMapper mapper;
    private final SaleMapper saleMapper;

    public BranchService(
            BranchRepository repository,
            BranchMapper mapper,
            SaleMapper saleMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.saleMapper = saleMapper;
    }

    @Transactional
    @Override
    public BranchResponse create(BranchCreateRequest branchCreateRequest) {

        Branch branch = mapper.toBranch(branchCreateRequest);
        return mapper.toResponse(repository.save(branch));
    }

    @Override
    @Transactional(readOnly = true)
    public BranchResponse findById(Long branchId) {
        Branch branch = repository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException(branchId));

        return mapper.toResponse(branch);
    }

    @Transactional(readOnly = true)
    public Branch findRequiredById(Long branchId) {
        return repository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException(branchId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long branchId) {
        repository.delete(findRequiredById(branchId));
    }

    @Override
    public List<SaleResponse> getSales(Long id) {
        Branch branch = repository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException(id));

        return branch.getSales().stream()
                .map(saleMapper::toResponse)
                .toList();
    }
}
