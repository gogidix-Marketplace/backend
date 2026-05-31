package com.gogidix.ecommerce.promotion.application.service;

import com.gogidix.ecommerce.promotion.application.dto.*;
import com.gogidix.ecommerce.promotion.application.mapper.PromotionMapper;
import com.gogidix.ecommerce.promotion.domain.model.Promotion;
import com.gogidix.ecommerce.promotion.domain.repository.PromotionRepository;
import com.gogidix.ecommerce.promotion.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository repository;
    private final PromotionMapper mapper;

    public PromotionService(PromotionRepository repository, PromotionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PromotionResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public PromotionResponse getById(String id) {
        Promotion entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found"));
        return mapper.toResponse(entity);
    }

    public PromotionResponse create(CreatePromotionRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Promotion entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Promotion saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public PromotionResponse update(String id, UpdatePromotionRequest request) {
        Promotion entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Promotion saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found"));
        repository.deleteById(id);
    }
}
