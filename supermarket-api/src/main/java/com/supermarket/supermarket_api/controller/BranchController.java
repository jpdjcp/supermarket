package com.supermarket.supermarket_api.controller;

import com.supermarket.supermarket_api.dto.BranchDTO;
import com.supermarket.supermarket_api.dto.SaleDTO;
import com.supermarket.supermarket_api.service.BranchService;
import com.supermarket.supermarket_api.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {

    private final BranchService service;
    private final SaleService saleService;

    public BranchController(BranchService service, SaleService saleService) {
        this.service = service;
        this.saleService = saleService;
    }

    @GetMapping
    public ResponseEntity<List<BranchDTO>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<BranchDTO> create(@RequestBody BranchDTO dto) {
        BranchDTO result = service.save(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDTO> update(@PathVariable Long id, @RequestBody BranchDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/branches/sales?branchId={branchId}")
    public ResponseEntity<List<SaleDTO>> getSalesByBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(saleService.getSalesByBranch(branchId));
    }
}
