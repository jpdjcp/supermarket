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
    public BranchDTO save(BranchDTO branchDTO) {

        Branch branch = mapper.mapToBranch(branchDTO);
        return mapper.mapToDTO(repository.save(branch));
    }

    @Override
    @Transactional(readOnly = true)
    public BranchDTO get(Long id) {
        return repository.findById(id)
                .map(mapper::mapToDTO)
                .orElseThrow(() -> new BranchNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> list() {
        return repository.findAll().stream()
                .map(mapper::mapToDTO)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public Branch getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException(id));
    }

    @Override
    @Transactional
    public BranchDTO update(Long id, BranchDTO dto) {
        Optional<Branch> result = repository.findById(id);
        if (result.isPresent()) {
            Branch branch = result.get();
            branch.setAddress(dto.address());
            return mapper.mapToDTO(repository.save(branch));
        }
        else throw new BranchNotFoundException(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
