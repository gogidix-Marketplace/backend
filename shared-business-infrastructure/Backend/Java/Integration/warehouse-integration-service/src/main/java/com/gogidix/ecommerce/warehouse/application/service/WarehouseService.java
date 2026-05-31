package com.gogidix.ecommerce.warehouse.application.service;

import com.gogidix.ecommerce.warehouse.application.dto.*;
import com.gogidix.ecommerce.warehouse.application.mapper.WarehouseMapper;
import com.gogidix.ecommerce.warehouse.domain.model.Warehouse;
import com.gogidix.ecommerce.warehouse.domain.repository.WarehouseRepository;
import com.gogidix.ecommerce.warehouse.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {

    private final WarehouseRepository repository;
    private final WarehouseMapper mapper;

    public WarehouseService(WarehouseRepository repository, WarehouseMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<WarehouseResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public WarehouseResponse getById(String id) {
        Warehouse entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));
        return mapper.toResponse(entity);
    }

    public WarehouseResponse create(CreateWarehouseRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Warehouse entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Warehouse saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public WarehouseResponse update(String id, UpdateWarehouseRequest request) {
        Warehouse entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Warehouse saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));
        repository.deleteById(id);
    }
}
