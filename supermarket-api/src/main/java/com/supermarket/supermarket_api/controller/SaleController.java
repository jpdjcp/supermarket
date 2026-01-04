package com.supermarket.supermarket_api.controller;

import com.supermarket.supermarket_api.dto.AddItemRequest;
import com.supermarket.supermarket_api.dto.SaleDTO;
import com.supermarket.supermarket_api.service.SaleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private SaleService service;

    @GetMapping("/list")
    public List<SaleDTO> list() {
        return service.list();
    }

    @PostMapping("/create/{branchId}")
    public SaleDTO createSale(@PathVariable @NotNull Long branchId) {
        return service.createSale(branchId);
    }

    @GetMapping("/get/{id}")
    public SaleDTO get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/branch-id/{branchId}")
    public List<SaleDTO> getSalesByBranch(@PathVariable Long branchId) {
        return service.getSalesByBranch(branchId);
    }

    @PostMapping("/{saleId}/items")
    public SaleDTO addItem(@PathVariable Long saleId,
                           @Valid @RequestBody AddItemRequest request) {
        return service.addItem(saleId, request);
    }
}
