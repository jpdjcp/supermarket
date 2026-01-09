package com.supermarket.supermarket_api.controller;

import com.supermarket.supermarket_api.dto.product.ProductCreateRequest;
import com.supermarket.supermarket_api.dto.product.ProductResponse;
import com.supermarket.supermarket_api.dto.product.ProductUpdateRequest;
import com.supermarket.supermarket_api.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductCreateRequest createRequest) {
        ProductResponse response = service.create(createRequest);

        return ResponseEntity
                .created(URI.create("/api/v1/products/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updatePrice(
            @PathVariable @Positive Long id,
            @RequestBody @Valid ProductUpdateRequest updateRequest) {

        return ResponseEntity.ok(service.updatePrice(id, updateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
