package com.supermarket.supermarket_api.controller;

import com.supermarket.supermarket_api.dto.BranchCreateRequest;
import com.supermarket.supermarket_api.dto.BranchResponse;
import com.supermarket.supermarket_api.dto.SaleDTO;
import com.supermarket.supermarket_api.service.BranchService;
import com.supermarket.supermarket_api.service.SaleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<BranchResponse> create(@Valid @RequestBody BranchCreateRequest dto) {
        BranchResponse result = service.create(dto);
        return ResponseEntity.created(
                        URI.create("/api/v1/branches/" + result.id()))
                .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> findById(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<BranchResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Positive @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/sales")
    public ResponseEntity<List<SaleDTO>> getSalesByBranch(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(saleService.getSalesByBranch(id));
    }
}
