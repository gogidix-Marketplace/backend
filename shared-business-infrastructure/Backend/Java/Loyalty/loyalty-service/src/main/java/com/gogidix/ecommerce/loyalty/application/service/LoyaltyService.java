package com.gogidix.ecommerce.loyalty.application.service;

import com.gogidix.ecommerce.loyalty.application.dto.*;
import com.gogidix.ecommerce.loyalty.application.mapper.LoyaltyMapper;
import com.gogidix.ecommerce.loyalty.domain.model.Loyalty;
import com.gogidix.ecommerce.loyalty.domain.repository.LoyaltyRepository;
import com.gogidix.ecommerce.loyalty.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoyaltyService {

    private final LoyaltyRepository repository;
    private final LoyaltyMapper mapper;

    public LoyaltyService(LoyaltyRepository repository, LoyaltyMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<LoyaltyResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public LoyaltyResponse getById(String id) {
        Loyalty entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loyalty not found"));
        return mapper.toResponse(entity);
    }

    public LoyaltyResponse create(CreateLoyaltyRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Loyalty entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Loyalty saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public LoyaltyResponse update(String id, UpdateLoyaltyRequest request) {
        Loyalty entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loyalty not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Loyalty saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loyalty not found"));
        repository.deleteById(id);
    }
}
