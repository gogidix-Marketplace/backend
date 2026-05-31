package com.gogidix.ecommerce.inventorysync.application.service;

import com.gogidix.ecommerce.inventorysync.application.dto.*;
import com.gogidix.ecommerce.inventorysync.application.mapper.InventorySyncMapper;
import com.gogidix.ecommerce.inventorysync.domain.model.InventorySync;
import com.gogidix.ecommerce.inventorysync.domain.repository.InventorySyncRepository;
import com.gogidix.ecommerce.inventorysync.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventorySyncService {

    private final InventorySyncRepository repository;
    private final InventorySyncMapper mapper;

    public InventorySyncService(InventorySyncRepository repository, InventorySyncMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<InventorySyncResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public InventorySyncResponse getById(String id) {
        InventorySync entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("InventorySync not found"));
        return mapper.toResponse(entity);
    }

    public InventorySyncResponse create(CreateInventorySyncRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        InventorySync entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        InventorySync saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public InventorySyncResponse update(String id, UpdateInventorySyncRequest request) {
        InventorySync entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("InventorySync not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        InventorySync saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("InventorySync not found"));
        repository.deleteById(id);
    }
}
