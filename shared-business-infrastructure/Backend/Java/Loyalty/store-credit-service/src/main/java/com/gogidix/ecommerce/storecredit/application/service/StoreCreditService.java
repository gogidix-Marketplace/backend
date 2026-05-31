package com.gogidix.ecommerce.storecredit.application.service;

import com.gogidix.ecommerce.storecredit.application.dto.*;
import com.gogidix.ecommerce.storecredit.application.mapper.StoreCreditMapper;
import com.gogidix.ecommerce.storecredit.domain.model.StoreCredit;
import com.gogidix.ecommerce.storecredit.domain.repository.StoreCreditRepository;
import com.gogidix.ecommerce.storecredit.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreCreditService {

    private final StoreCreditRepository repository;
    private final StoreCreditMapper mapper;

    public StoreCreditService(StoreCreditRepository repository, StoreCreditMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<StoreCreditResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public StoreCreditResponse getById(String id) {
        StoreCredit entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("StoreCredit not found"));
        return mapper.toResponse(entity);
    }

    public StoreCreditResponse create(CreateStoreCreditRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        StoreCredit entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        StoreCredit saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public StoreCreditResponse update(String id, UpdateStoreCreditRequest request) {
        StoreCredit entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("StoreCredit not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        StoreCredit saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("StoreCredit not found"));
        repository.deleteById(id);
    }
}
