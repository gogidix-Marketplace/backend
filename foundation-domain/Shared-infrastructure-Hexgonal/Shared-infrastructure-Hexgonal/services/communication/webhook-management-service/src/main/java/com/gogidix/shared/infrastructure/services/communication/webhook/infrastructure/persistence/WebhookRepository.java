package com.gogidix.shared.infrastructure.services.communication.webhook.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.model.Webhook;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;
public interface WebhookRepository extends MongoRepository<Webhook, String> {
    // Find by tenant
    List<Webhook> findByTenantId_Value(String tenantId);
    // Find by tenant and ID
    Optional<Webhook> findByTenantId_ValueAndId(String tenantId, String id);
    // Find by tenant and name
    Optional<Webhook> findByTenantId_ValueAndName(String tenantId, String name);
    // Find by tenant and status
    List<Webhook> findByTenantId_ValueAndStatus(String tenantId, String status);
    // Find by tenant and event type
    List<Webhook> findByTenantId_ValueAndEventType(String tenantId, String eventType);
    // Find by tenant and event types (in list)
    @Query("{'tenantId.value': ?0, 'eventTypes': {$in: ?1}}")
    List<Webhook> findByTenantId_ValueAndEventTypesIn(String tenantId, List<String> eventTypes);
    // Find by tenant and created by
    List<Webhook> findByTenantId_ValueAndCreatedBy(String tenantId, String createdBy);
    // Delete by tenant and ID
    void deleteByTenantId_ValueAndId(String tenantId, String id);
    // Count by tenant
    long countByTenantId_Value(String tenantId);
    // Check exists by tenant and name
    boolean existsByTenantId_ValueAndName(String tenantId, String name);
    // Find active webhooks by tenant
    @Query("{'tenantId.value': ?0, 'status': 'ACTIVE'}")
    List<Webhook> findActiveByTenant(String tenantId);
    // Find active webhooks by tenant and event type
    @Query("{'tenantId.value': ?0, 'status': 'ACTIVE', '$or': [{'eventType': ?1}, {'eventTypes': ?1}]}")
    List<Webhook> findActiveByTenantAndEventType(String tenantId, String eventType);
    // Find by tenant and URL
    Optional<Webhook> findByTenantId_ValueAndUrl(String tenantId, String url);
}
