package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.BranchDTO;
import com.supermarket.supermarket_api.dto.SaleDTO;
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
        // Validación básica
        if (branchDTO == null) {
            throw new IllegalArgumentException("El DTO de sucursal no puede ser nulo");
        }

        if (branchDTO.address() == null || branchDTO.address().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección de la sucursal es obligatoria");
        }

        try {
            Branch branch = branchMapper.mapToBranch(branchDTO);
            Branch createdBranch = repository.save(branch);
            return branchMapper.mapToDTO(createdBranch);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la sucursal: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BranchDTO get(Long id) {
        return repository.findById(id)
                .map(branchMapper::mapToDTO)
                .orElse(null);
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
