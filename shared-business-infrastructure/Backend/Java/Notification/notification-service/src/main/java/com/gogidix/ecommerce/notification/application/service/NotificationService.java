package com.gogidix.ecommerce.notification.application.service;

import com.gogidix.ecommerce.notification.application.dto.*;
import com.gogidix.ecommerce.notification.application.mapper.NotificationMapper;
import com.gogidix.ecommerce.notification.domain.model.Notification;
import com.gogidix.ecommerce.notification.domain.repository.NotificationRepository;
import com.gogidix.ecommerce.notification.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    public NotificationService(NotificationRepository repository, NotificationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<NotificationResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public NotificationResponse getById(String id) {
        Notification entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        return mapper.toResponse(entity);
    }

    public NotificationResponse create(CreateNotificationRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Notification entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Notification saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public NotificationResponse update(String id, UpdateNotificationRequest request) {
        Notification entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Notification saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        repository.deleteById(id);
    }
}
