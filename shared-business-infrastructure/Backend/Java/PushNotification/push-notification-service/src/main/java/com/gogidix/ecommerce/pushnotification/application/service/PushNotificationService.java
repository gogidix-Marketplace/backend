package com.gogidix.ecommerce.pushnotification.application.service;

import com.gogidix.ecommerce.pushnotification.application.dto.CreatePushNotificationRequest;
import com.gogidix.ecommerce.pushnotification.application.dto.UpdatePushNotificationRequest;
import com.gogidix.ecommerce.pushnotification.application.dto.PushNotificationResponse;
import com.gogidix.ecommerce.pushnotification.application.mapper.PushNotificationMapper;
import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import com.gogidix.ecommerce.pushnotification.domain.port.out.PushNotificationRepositoryPort;
import com.gogidix.ecommerce.pushnotification.domain.port.out.PushNotificationEventPublisher;
import com.gogidix.ecommerce.pushnotification.shared.exception.PushNotificationNotFoundException;
import com.gogidix.ecommerce.pushnotification.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.pushnotification.shared.requestcontext.RequestContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PushNotificationService {

    private final PushNotificationRepositoryPort repositoryPort;
    private final PushNotificationEventPublisher eventPublisher;
    private final PushNotificationMapper mapper;

    public PushNotificationService(PushNotificationRepositoryPort repositoryPort, PushNotificationEventPublisher eventPublisher, PushNotificationMapper mapper) {
        this.repositoryPort = repositoryPort;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    public PushNotificationResponse create(CreatePushNotificationRequest request) {
        RequestContext ctx = RequestContextHolder.getContext();
        PushNotification entity = mapper.toEntity(request);
        entity.setTenantId(ctx.getTenantId());
        PushNotification saved = repositoryPort.save(entity);
        eventPublisher.publishCreated(saved);
        return mapper.toResponse(saved);
    }

    public PushNotificationResponse getById(String id) {
        RequestContext ctx = RequestContextHolder.getContext();
        PushNotification entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new PushNotificationNotFoundException("PushNotification not found with id: " + id));
        return mapper.toResponse(entity);
    }

    public List<PushNotificationResponse> getList(int page, int size) {
        RequestContext ctx = RequestContextHolder.getContext();
        Page<PushNotification> result = repositoryPort.findByTenantId(ctx.getTenantId(), PageRequest.of(page, size));
        return result.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public PushNotificationResponse update(String id, UpdatePushNotificationRequest request) {
        RequestContext ctx = RequestContextHolder.getContext();
        PushNotification entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new PushNotificationNotFoundException("PushNotification not found with id: " + id));
        mapper.updateFromRequest(entity, request);
        PushNotification updated = repositoryPort.save(entity);
        eventPublisher.publishUpdated(updated);
        return mapper.toResponse(updated);
    }

    public void delete(String id) {
        RequestContext ctx = RequestContextHolder.getContext();
        PushNotification entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new PushNotificationNotFoundException("PushNotification not found with id: " + id));
        repositoryPort.deleteById(id);
        eventPublisher.publishDeleted(entity);
    }
}