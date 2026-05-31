package com.gogidix.ecommerce.email.application.service;

import com.gogidix.ecommerce.email.application.dto.CreateEmailRequest;
import com.gogidix.ecommerce.email.application.dto.UpdateEmailRequest;
import com.gogidix.ecommerce.email.application.dto.EmailResponse;
import com.gogidix.ecommerce.email.application.mapper.EmailMapper;
import com.gogidix.ecommerce.email.domain.model.Email;
import com.gogidix.ecommerce.email.domain.port.out.EmailRepositoryPort;
import com.gogidix.ecommerce.email.domain.port.out.EmailEventPublisher;
import com.gogidix.ecommerce.email.shared.exception.EmailNotFoundException;
import com.gogidix.ecommerce.email.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.email.shared.requestcontext.RequestContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private final EmailRepositoryPort repositoryPort;
    private final EmailEventPublisher eventPublisher;
    private final EmailMapper mapper;

    public EmailService(EmailRepositoryPort repositoryPort, EmailEventPublisher eventPublisher, EmailMapper mapper) {
        this.repositoryPort = repositoryPort;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    public EmailResponse create(CreateEmailRequest request) {
        RequestContext ctx = RequestContextHolder.getContext();
        Email entity = mapper.toEntity(request);
        entity.setTenantId(ctx.getTenantId());
        Email saved = repositoryPort.save(entity);
        eventPublisher.publishCreated(saved);
        return mapper.toResponse(saved);
    }

    public EmailResponse getById(String id) {
        RequestContext ctx = RequestContextHolder.getContext();
        Email entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new EmailNotFoundException("Email not found with id: " + id));
        return mapper.toResponse(entity);
    }

    public List<EmailResponse> getList(int page, int size) {
        RequestContext ctx = RequestContextHolder.getContext();
        Page<Email> result = repositoryPort.findByTenantId(ctx.getTenantId(), PageRequest.of(page, size));
        return result.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public EmailResponse update(String id, UpdateEmailRequest request) {
        RequestContext ctx = RequestContextHolder.getContext();
        Email entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new EmailNotFoundException("Email not found with id: " + id));
        mapper.updateFromRequest(entity, request);
        Email updated = repositoryPort.save(entity);
        eventPublisher.publishUpdated(updated);
        return mapper.toResponse(updated);
    }

    public void delete(String id) {
        RequestContext ctx = RequestContextHolder.getContext();
        Email entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new EmailNotFoundException("Email not found with id: " + id));
        repositoryPort.deleteById(id);
        eventPublisher.publishDeleted(entity);
    }
}