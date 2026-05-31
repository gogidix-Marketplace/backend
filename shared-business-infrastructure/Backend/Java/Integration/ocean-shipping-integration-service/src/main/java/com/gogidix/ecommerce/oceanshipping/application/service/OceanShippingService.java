package com.gogidix.ecommerce.oceanshipping.application.service;

import com.gogidix.ecommerce.oceanshipping.application.dto.*;
import com.gogidix.ecommerce.oceanshipping.application.mapper.OceanShippingMapper;
import com.gogidix.ecommerce.oceanshipping.domain.model.OceanShipping;
import com.gogidix.ecommerce.oceanshipping.domain.repository.OceanShippingRepository;
import com.gogidix.ecommerce.oceanshipping.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OceanShippingService {

    private final OceanShippingRepository repository;
    private final OceanShippingMapper mapper;

    public OceanShippingService(OceanShippingRepository repository, OceanShippingMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<OceanShippingResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public OceanShippingResponse getById(String id) {
        OceanShipping entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("OceanShipping not found"));
        return mapper.toResponse(entity);
    }

    public OceanShippingResponse create(CreateOceanShippingRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        OceanShipping entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        OceanShipping saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public OceanShippingResponse update(String id, UpdateOceanShippingRequest request) {
        OceanShipping entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("OceanShipping not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        OceanShipping saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("OceanShipping not found"));
        repository.deleteById(id);
    }
}
