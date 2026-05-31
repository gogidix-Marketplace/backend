package com.gogidix.ecommerce.pushnotification.application.service;

import com.gogidix.ecommerce.pushnotification.application.dto.*;
import com.gogidix.ecommerce.pushnotification.application.mapper.PushNotificationMapper;
import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import com.gogidix.ecommerce.pushnotification.domain.repository.PushNotificationRepository;
import com.gogidix.ecommerce.pushnotification.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PushNotificationService {

    private final PushNotificationRepository repository;
    private final PushNotificationMapper mapper;

    public PushNotificationService(PushNotificationRepository repository, PushNotificationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PushNotificationResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public PushNotificationResponse getById(String id) {
        PushNotification entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PushNotification not found"));
        return mapper.toResponse(entity);
    }

    public PushNotificationResponse create(CreatePushNotificationRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        PushNotification entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        PushNotification saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public PushNotificationResponse update(String id, UpdatePushNotificationRequest request) {
        PushNotification entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PushNotification not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        PushNotification saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PushNotification not found"));
        repository.deleteById(id);
    }
}
