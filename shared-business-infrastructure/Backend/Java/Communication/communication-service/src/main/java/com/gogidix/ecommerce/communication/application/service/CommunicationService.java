package com.gogidix.ecommerce.communication.application.service;

import com.gogidix.ecommerce.communication.application.dto.*;
import com.gogidix.ecommerce.communication.application.mapper.CommunicationMapper;
import com.gogidix.ecommerce.communication.domain.model.Communication;
import com.gogidix.ecommerce.communication.domain.repository.CommunicationRepository;
import com.gogidix.ecommerce.communication.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationService {

    private final CommunicationRepository repository;
    private final CommunicationMapper mapper;

    public CommunicationService(CommunicationRepository repository, CommunicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CommunicationResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public CommunicationResponse getById(String id) {
        Communication entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Communication not found"));
        return mapper.toResponse(entity);
    }

    public CommunicationResponse create(CreateCommunicationRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Communication entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Communication saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public CommunicationResponse update(String id, UpdateCommunicationRequest request) {
        Communication entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Communication not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Communication saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Communication not found"));
        repository.deleteById(id);
    }
}
