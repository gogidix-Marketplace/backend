package com.gogidix.ecommerce.sms.application.service;

import com.gogidix.ecommerce.sms.application.dto.*;
import com.gogidix.ecommerce.sms.application.mapper.SmsMapper;
import com.gogidix.ecommerce.sms.domain.model.Sms;
import com.gogidix.ecommerce.sms.domain.repository.SmsRepository;
import com.gogidix.ecommerce.sms.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsService {

    private final SmsRepository repository;
    private final SmsMapper mapper;

    public SmsService(SmsRepository repository, SmsMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<SmsResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public SmsResponse getById(String id) {
        Sms entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sms not found"));
        return mapper.toResponse(entity);
    }

    public SmsResponse create(CreateSmsRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Sms entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Sms saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public SmsResponse update(String id, UpdateSmsRequest request) {
        Sms entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sms not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Sms saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sms not found"));
        repository.deleteById(id);
    }
}
