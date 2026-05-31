package com.gogidix.shared.infrastructure.services.communication.webhook.infrastructure.persistence.impl;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.model.Webhook;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.port.out.IWebhookRepository;
import com.gogidix.shared.infrastructure.services.communication.webhook.infrastructure.persistence.WebhookRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public class WebhookRepositoryImpl implements IWebhookRepository {
    private final WebhookRepository mongoRepository;
    public WebhookRepositoryImpl(WebhookRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }
    @Override
    public Webhook save(Webhook entity) {
        return mongoRepository.save(entity);
    }
    @Override
    public Optional<Webhook> findById(String id) {
        return mongoRepository.findById(id);
    }
    @Override
    public List<Webhook> findAllByTenantId(String tenantId) {
        return mongoRepository.findByTenantId_Value(tenantId);
    }
    @Override
    public Optional<Webhook> findByIdAndTenantId(String id, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public Optional<Webhook> findByNameAndTenantId(String name, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndName(tenantId, name);
    }
    @Override
    public Optional<Webhook> findByUrlAndTenantId(String url, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndUrl(tenantId, url);
    }
    @Override
    public List<Webhook> findByTenantIdAndStatus(String tenantId, String status) {
        return mongoRepository.findByTenantId_ValueAndStatus(tenantId, status);
    }
    @Override
    public List<Webhook> findByTenantIdAndEventType(String tenantId, String eventType) {
        return mongoRepository.findByTenantId_ValueAndEventType(tenantId, eventType);
    }
    @Override
    public List<Webhook> findByTenantIdAndCreatedBy(String tenantId, String createdBy) {
        return mongoRepository.findByTenantId_ValueAndCreatedBy(tenantId, createdBy);
    }
    @Override
    public long countByTenantId(String tenantId) {
        return mongoRepository.countByTenantId_Value(tenantId);
    }
    @Override
    public void deleteByTenantIdAndId(String tenantId, String id) {
        mongoRepository.deleteByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public boolean existsByNameAndTenantId(String name, String tenantId) {
        return mongoRepository.existsByTenantId_ValueAndName(tenantId, name);
    }
}
