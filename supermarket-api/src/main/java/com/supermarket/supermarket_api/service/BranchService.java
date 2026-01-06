package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.BranchDTO;
import com.supermarket.supermarket_api.dto.SaleDTO;
import com.supermarket.supermarket_api.exception.BranchNotFoundException;
import com.supermarket.supermarket_api.model.Branch;
import com.supermarket.supermarket_api.mapper.BranchMapper;
import com.supermarket.supermarket_api.repository.BranchRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService implements IBranchService {

    @Autowired
    private BranchRepository repository;

    @Autowired
    private BranchMapper branchMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> list() {
        return repository.findAll().stream()
                .map(branchMapper::mapToDTO)
                .toList();
    }

    @Transactional
    @Override
    public BranchDTO save(BranchDTO branchDTO) {

        Branch branch = branchMapper.mapToBranch(branchDTO);
        return branchMapper.mapToDTO(repository.save(branch));
    }

    @Override
    @Transactional(readOnly = true)
    public BranchDTO get(Long id) {
        return repository.findById(id)
                .map(branchMapper::mapToDTO)
                .orElse(null);
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
            return branchMapper.mapToDTO(repository.save(branch));
        }
        return null;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
