package com.prodnretail.inventory_gen.service;

import com.prodnretail.inventory_gen.dto.SupplierDTO;
import com.prodnretail.inventory_gen.model.Supplier;
import com.prodnretail.inventory_gen.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    
    public SupplierService(SupplierRepository supplierRepository,ModelMapper modelMapper) {
        this.supplierRepository = supplierRepository;
        this.modelMapper=modelMapper;
    }

    public Supplier saveSupplier(Supplier supplier){
        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getSupplierById(UUID id) {
        return supplierRepository.findById(id);
    }

    public Supplier createSupplierFromDto(SupplierDTO supplierdto) {
        Supplier supplier = modelMapper.map(supplierdto,Supplier.class);
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplierFromDto(UUID id, SupplierDTO dto) {
        return supplierRepository.findById(id).map(existing -> {
            modelMapper.map(dto,existing);
            return supplierRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    public void deleteSupplier(UUID id) {
        supplierRepository.deleteById(id);
    }

    public SupplierDTO toDto(Supplier supplier){
        return SupplierDTO.builder()
        .id(supplier.getId())
        .supplierName(supplier.getSupplierName())
        .email(supplier.getEmail())
        .phone(supplier.getPhone())
        .build();
    }

    public Supplier toEntity(SupplierDTO dto){
        Supplier supplier = new Supplier();
        supplier.setId(dto.getId());
        supplier.setSupplierName(dto.getSupplierName());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());
        return supplier;
    }
}