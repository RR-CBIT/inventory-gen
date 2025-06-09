package com.prodnretail.inventory_gen.service;

import com.prodnretail.inventory_gen.model.Supplier;
import com.prodnretail.inventory_gen.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getSupplierById(UUID id) {
        return supplierRepository.findById(id);
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(UUID id, Supplier updatedSupplier) {
        return supplierRepository.findById(id).map(supplier -> {
            supplier.setSupplierName(updatedSupplier.getSupplierName());
            return supplierRepository.save(supplier);
        }).orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    public void deleteSupplier(UUID id) {
        supplierRepository.deleteById(id);
    }
}