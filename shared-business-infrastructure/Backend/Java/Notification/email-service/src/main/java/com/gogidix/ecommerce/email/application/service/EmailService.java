package com.gogidix.ecommerce.email.application.service;

import com.gogidix.ecommerce.email.application.dto.*;
import com.gogidix.ecommerce.email.application.mapper.EmailMapper;
import com.gogidix.ecommerce.email.domain.model.Email;
import com.gogidix.ecommerce.email.domain.repository.EmailRepository;
import com.gogidix.ecommerce.email.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private final EmailRepository repository;
    private final EmailMapper mapper;

    public EmailService(EmailRepository repository, EmailMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<EmailResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public EmailResponse getById(String id) {
        Email entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));
        return mapper.toResponse(entity);
    }

    public EmailResponse create(CreateEmailRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Email entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Email saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public EmailResponse update(String id, UpdateEmailRequest request) {
        Email entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Email saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));
        repository.deleteById(id);
    }
}
