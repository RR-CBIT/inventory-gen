package com.prodnretail.inventory_gen.service;

import com.prodnretail.inventory_gen.dto.SupplierDTO;
import com.prodnretail.inventory_gen.exception.ResourceNotFoundException;
import com.prodnretail.inventory_gen.model.Supplier;
import com.prodnretail.inventory_gen.repository.SupplierRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;


    public List<SupplierDTO> getAll() {
        return supplierRepository.findAll()
        .stream()
        .map(supplier-> modelMapper.map(supplier,SupplierDTO.class)).toList();
    }

    public SupplierDTO getById(UUID id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id)
                );
        return modelMapper.map(supplier,SupplierDTO.class);
    }

    public SupplierDTO create(SupplierDTO supplierdto) {
        Supplier supplier = modelMapper.map(supplierdto,Supplier.class);
        Supplier saved= supplierRepository.save(supplier);
        return modelMapper.map(saved,SupplierDTO.class);
    }

    public SupplierDTO update(UUID id, SupplierDTO dto) {
        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id)
                );
        modelMapper.map(dto,existing);
        Supplier updated=supplierRepository.save(existing);
        return modelMapper.map(updated,SupplierDTO.class);
    }

    public void delete(UUID id) {
        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id)
                );
        supplierRepository.delete(existing);
    }
}