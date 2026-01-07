package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.BranchCreateRequest;
import com.supermarket.supermarket_api.dto.BranchResponse;
import com.supermarket.supermarket_api.exception.BranchNotFoundException;
import com.supermarket.supermarket_api.model.Branch;
import com.supermarket.supermarket_api.mapper.BranchMapper;
import com.supermarket.supermarket_api.repository.BranchRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService implements IBranchService {

    private final BranchRepository repository;
    private final BranchMapper mapper;

    public BranchService(BranchRepository repository, BranchMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public BranchResponse create(BranchCreateRequest branchCreateRequest) {

        Branch branch = mapper.toBranch(branchCreateRequest);
        return mapper.toResponse(repository.save(branch));
    }

    @Override
    @Transactional(readOnly = true)
    public BranchResponse findById(Long id) {
        Branch branch = repository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException(id));

        return mapper.toResponse(branch);
    }

    @Transactional(readOnly = true)
    public Branch getRequiredById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getRequiredById(id));
    }
}
