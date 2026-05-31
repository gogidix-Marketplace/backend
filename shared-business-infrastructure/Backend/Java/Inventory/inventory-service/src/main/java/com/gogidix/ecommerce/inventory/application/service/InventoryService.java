package com.gogidix.ecommerce.inventory.application.service;

import com.gogidix.ecommerce.inventory.application.dto.*;
import com.gogidix.ecommerce.inventory.application.mapper.InventoryMapper;
import com.gogidix.ecommerce.inventory.domain.model.Inventory;
import com.gogidix.ecommerce.inventory.domain.repository.InventoryRepository;
import com.gogidix.ecommerce.inventory.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository repository;
    private final InventoryMapper mapper;

    public InventoryService(InventoryRepository repository, InventoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<InventoryResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public InventoryResponse getById(String id) {
        Inventory entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
        return mapper.toResponse(entity);
    }

    public InventoryResponse create(CreateInventoryRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Inventory entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Inventory saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public InventoryResponse update(String id, UpdateInventoryRequest request) {
        Inventory entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Inventory saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
        repository.deleteById(id);
    }
}
