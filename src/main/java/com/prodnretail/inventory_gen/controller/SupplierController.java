package com.prodnretail.inventory_gen.controller;

import com.prodnretail.inventory_gen.dto.SupplierDTO;
import com.prodnretail.inventory_gen.model.Supplier;
import com.prodnretail.inventory_gen.service.SupplierService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers() {
        List<SupplierDTO> dtos = supplierService.getAllSuppliers().stream()
        .map(supplierService::toDto).toList();
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable UUID id) {
        return supplierService.getSupplierById(id)
                .map(supplier -> new ResponseEntity<>(supplierService.toDto(supplier),HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(@RequestBody SupplierDTO dto) {
        Supplier supplier = supplierService.saveSupplier(supplierService.toEntity(dto));
        return new ResponseEntity<>(supplierService.toDto(supplier),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable UUID id, @RequestBody SupplierDTO dto) {
        try {
            Supplier supplier = supplierService.updateSupplierFromDto(id, dto);
            return ResponseEntity.ok(supplierService.toDto(supplier));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable UUID id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}