package com.supermarket.supermarket_api.controller;

import com.supermarket.supermarket_api.dto.BranchDTO;
import com.supermarket.supermarket_api.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {

    @Autowired
    private BranchService service;

    @GetMapping("/list")
    public ResponseEntity<List<BranchDTO>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping("/create")
    public ResponseEntity<BranchDTO> save(@RequestBody BranchDTO dto) {
        var result = service.save(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BranchDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/addItem/{id}")
    public ResponseEntity<BranchDTO> update(@PathVariable Long id, @RequestBody BranchDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
