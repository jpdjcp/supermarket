package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.BranchDTO;
import com.supermarket.supermarket_api.exception.BranchNotFoundException;
import com.supermarket.supermarket_api.model.Branch;
import com.supermarket.supermarket_api.mapper.BranchMapper;
import com.supermarket.supermarket_api.repository.BranchRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public BranchDTO create(BranchDTO branchDTO) {

        Branch branch = mapper.mapToBranch(branchDTO);
        return mapper.mapToDTO(repository.save(branch));
    }

    @Override
    @Transactional(readOnly = true)
    public BranchDTO findById(Long id) {
        Branch branch = repository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException(id));

        return mapper.mapToDTO(branch);
    }

    @Transactional(readOnly = true)
    public Branch getRequiredById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::mapToDTO)
                .toList();
    }

    public void delete(Long id) {
        Branch branch = repository.findById(id)
                        .orElseThrow(() -> new BranchNotFoundException(id));
        repository.delete(branch);
    }
}
