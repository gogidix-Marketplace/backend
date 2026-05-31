package com.gogidix.shared.infrastructure.services.communication.webhook.domain.port.out;

import com.gogidix.shared.infrastructure.services.communication.webhook.domain.model.Webhook;

import java.util.List;
import java.util.Optional;

public interface IWebhookRepository {
    Webhook save(Webhook entity);
    Optional<Webhook> findById(String id);
    List<Webhook> findAllByTenantId(String tenantId);
    Optional<Webhook> findByIdAndTenantId(String id, String tenantId);
    Optional<Webhook> findByNameAndTenantId(String name, String tenantId);
    Optional<Webhook> findByUrlAndTenantId(String url, String tenantId);
    List<Webhook> findByTenantIdAndStatus(String tenantId, String status);
    List<Webhook> findByTenantIdAndEventType(String tenantId, String eventType);
    List<Webhook> findByTenantIdAndCreatedBy(String tenantId, String createdBy);
    long countByTenantId(String tenantId);
    void deleteByTenantIdAndId(String tenantId, String id);
    boolean existsByNameAndTenantId(String name, String tenantId);
}
