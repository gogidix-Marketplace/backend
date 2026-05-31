package com.gogidix.ecommerce.sms.application.service;

import com.gogidix.ecommerce.sms.application.dto.CreateSmsRequest;
import com.gogidix.ecommerce.sms.application.dto.UpdateSmsRequest;
import com.gogidix.ecommerce.sms.application.dto.SmsResponse;
import com.gogidix.ecommerce.sms.application.mapper.SmsMapper;
import com.gogidix.ecommerce.sms.domain.model.Sms;
import com.gogidix.ecommerce.sms.domain.port.out.SmsRepositoryPort;
import com.gogidix.ecommerce.sms.domain.port.out.SmsEventPublisher;
import com.gogidix.ecommerce.sms.shared.exception.SmsNotFoundException;
import com.gogidix.ecommerce.sms.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.sms.shared.requestcontext.RequestContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SmsService {

    private final SmsRepositoryPort repositoryPort;
    private final SmsEventPublisher eventPublisher;
    private final SmsMapper mapper;

    public SmsService(SmsRepositoryPort repositoryPort, SmsEventPublisher eventPublisher, SmsMapper mapper) {
        this.repositoryPort = repositoryPort;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    public SmsResponse create(CreateSmsRequest request) {
        RequestContext ctx = RequestContextHolder.getContext();
        Sms entity = mapper.toEntity(request);
        entity.setTenantId(ctx.getTenantId());
        Sms saved = repositoryPort.save(entity);
        eventPublisher.publishCreated(saved);
        return mapper.toResponse(saved);
    }

    public SmsResponse getById(String id) {
        RequestContext ctx = RequestContextHolder.getContext();
        Sms entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new SmsNotFoundException("Sms not found with id: " + id));
        return mapper.toResponse(entity);
    }

    public List<SmsResponse> getList(int page, int size) {
        RequestContext ctx = RequestContextHolder.getContext();
        Page<Sms> result = repositoryPort.findByTenantId(ctx.getTenantId(), PageRequest.of(page, size));
        return result.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public SmsResponse update(String id, UpdateSmsRequest request) {
        RequestContext ctx = RequestContextHolder.getContext();
        Sms entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new SmsNotFoundException("Sms not found with id: " + id));
        mapper.updateFromRequest(entity, request);
        Sms updated = repositoryPort.save(entity);
        eventPublisher.publishUpdated(updated);
        return mapper.toResponse(updated);
    }

    public void delete(String id) {
        RequestContext ctx = RequestContextHolder.getContext();
        Sms entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new SmsNotFoundException("Sms not found with id: " + id));
        repositoryPort.deleteById(id);
        eventPublisher.publishDeleted(entity);
    }
}