package com.supermarket.supermarket_api.controller;

import com.supermarket.supermarket_api.dto.sale.SaleCreateRequest;
import com.supermarket.supermarket_api.dto.sale.SaleResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductRequest;
import com.supermarket.supermarket_api.dto.sale.saleItem.AddProductResponse;
import com.supermarket.supermarket_api.dto.sale.saleItem.SaleItemResponse;
import com.supermarket.supermarket_api.service.SaleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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

    @PostMapping
    public ResponseEntity<SaleResponse> create(@RequestBody @Valid SaleCreateRequest request) {
        SaleResponse sale = service.createSale(request.branchId());
        URI location = URI.create("/api/v1/sales/" + sale.id());
        return ResponseEntity.created(location).body(sale);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AddProductResponse> addProduct(@PathVariable @Positive Long id,
                                                         @Valid @RequestBody AddProductRequest request) {
        AddProductResponse item = service.addProduct(id, request);
        URI location = URI.create("/api/v1/sales/" + id);
        return ResponseEntity.created(location).body(item);
    }

    @GetMapping("/{saleId}/items")
    public ResponseEntity<List<SaleItemResponse>> getProducts(@PathVariable @Positive Long saleId) {
        return ResponseEntity.ok(service.getProducts(saleId));
    }

    @PatchMapping("/{saleId}/items/{productId}")
    public ResponseEntity<Void> removeProduct(
            @PathVariable @Positive Long saleId,
            @PathVariable @Positive Long productId) {
        service.removeProduct(saleId, productId);
        return ResponseEntity.noContent().build();
    }
}
