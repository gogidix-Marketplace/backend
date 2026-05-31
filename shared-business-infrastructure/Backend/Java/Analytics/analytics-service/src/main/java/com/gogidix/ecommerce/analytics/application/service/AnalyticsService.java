package com.gogidix.ecommerce.analytics.application.service;

import com.gogidix.ecommerce.analytics.application.dto.*;
import com.gogidix.ecommerce.analytics.application.mapper.AnalyticsMapper;
import com.gogidix.ecommerce.analytics.domain.model.Analytics;
import com.gogidix.ecommerce.analytics.domain.repository.AnalyticsRepository;
import com.gogidix.ecommerce.analytics.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsService {

    private final AnalyticsRepository repository;
    private final AnalyticsMapper mapper;

    public AnalyticsService(AnalyticsRepository repository, AnalyticsMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<AnalyticsResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public AnalyticsResponse getById(String id) {
        Analytics entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Analytics not found"));
        return mapper.toResponse(entity);
    }

    public AnalyticsResponse create(CreateAnalyticsRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Analytics entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Analytics saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public AnalyticsResponse update(String id, UpdateAnalyticsRequest request) {
        Analytics entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Analytics not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Analytics saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Analytics not found"));
        repository.deleteById(id);
    }
}
