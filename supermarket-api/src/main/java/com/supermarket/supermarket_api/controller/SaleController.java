package com.supermarket.supermarket_api.controller;

import com.supermarket.supermarket_api.dto.AddItemRequest;
import com.supermarket.supermarket_api.dto.SaleCreateRequest;
import com.supermarket.supermarket_api.dto.SaleResponse;
import com.supermarket.supermarket_api.dto.SaleItemResponse;
import com.supermarket.supermarket_api.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
public class SaleController {

    private final SaleService service;

    public SaleController(SaleService service) {
        this.service = service;
    }

    @GetMapping
    public List<SaleResponse> list() { return service.findAll(); }

    @PostMapping
    public ResponseEntity<SaleResponse> create(@RequestBody @Valid SaleCreateRequest dto) {
        SaleResponse sale = service.createSale(dto.branchId());
        URI location = URI.create("/api/v1/sales/" + sale.id());
        return ResponseEntity.created(location).body(sale);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/{saleId}/items")
    public ResponseEntity<SaleItemResponse> addItem(@PathVariable Long saleId,
                                                    @Valid @RequestBody AddItemRequest request) {
        SaleItemResponse item = service.addProduct(saleId, request);
        URI location = URI.create("/api/v1/sales/" + saleId + "/items/" + item.id());
        return ResponseEntity.created(location).body(item);
    }

    @GetMapping("/{saleId}/items")
    public ResponseEntity<List<SaleItemResponse>> getItems(@PathVariable Long saleId) {
        return ResponseEntity.ok(service.getProducts(saleId));
    }
}
