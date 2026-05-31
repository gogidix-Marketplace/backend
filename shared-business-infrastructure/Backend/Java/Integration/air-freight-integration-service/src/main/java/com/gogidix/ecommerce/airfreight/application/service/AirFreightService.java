package com.gogidix.ecommerce.airfreight.application.service;

import com.gogidix.ecommerce.airfreight.application.dto.*;
import com.gogidix.ecommerce.airfreight.application.mapper.AirFreightMapper;
import com.gogidix.ecommerce.airfreight.domain.model.AirFreight;
import com.gogidix.ecommerce.airfreight.domain.repository.AirFreightRepository;
import com.gogidix.ecommerce.airfreight.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirFreightService {

    private final AirFreightRepository repository;
    private final AirFreightMapper mapper;

    public AirFreightService(AirFreightRepository repository, AirFreightMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<AirFreightResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public AirFreightResponse getById(String id) {
        AirFreight entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AirFreight not found"));
        return mapper.toResponse(entity);
    }

    public AirFreightResponse create(CreateAirFreightRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        AirFreight entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        AirFreight saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public AirFreightResponse update(String id, UpdateAirFreightRequest request) {
        AirFreight entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AirFreight not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        AirFreight saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AirFreight not found"));
        repository.deleteById(id);
    }
}
