package com.supermarket.supermarket_api.controller;

import com.supermarket.supermarket_api.dto.ProductCreateRequest;
import com.supermarket.supermarket_api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/list")
    public ResponseEntity<List<ProductCreateRequest>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<ProductCreateRequest> create(@RequestBody ProductCreateRequest dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductCreateRequest> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/addItem/{id}")
    public ResponseEntity<ProductCreateRequest> update(@PathVariable Long id, @RequestBody ProductCreateRequest dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
