package com.gogidix.shared.infrastructure.services.communication.webhook.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.model.WebhookDeliveryLog;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
public interface WebhookDeliveryLogRepository extends MongoRepository<WebhookDeliveryLog, String> {
    // Find by tenant
    List<WebhookDeliveryLog> findByTenantId_Value(String tenantId);
    // Find by tenant and ID
    List<WebhookDeliveryLog> findByTenantId_ValueAndId(String tenantId, String id);
    // Find by tenant and webhook ID
    List<WebhookDeliveryLog> findByTenantId_ValueAndWebhookId(String tenantId, String webhookId);
    // Find by tenant and attempt ID
    List<WebhookDeliveryLog> findByTenantId_ValueAndAttemptId(String tenantId, String attemptId);
    // Find by tenant and status
    List<WebhookDeliveryLog> findByTenantId_ValueAndStatus(String tenantId, String status);
    // Find by tenant and event type
    List<WebhookDeliveryLog> findByTenantId_ValueAndEventType(String tenantId, String eventType);
    // Delete by tenant and ID
    void deleteByTenantId_ValueAndId(String tenantId, String id);
    // Delete by tenant and webhook ID
    void deleteByTenantId_ValueAndWebhookId(String tenantId, String webhookId);
    // Count by tenant
    long countByTenantId_Value(String tenantId);
    // Count by tenant and status
    long countByTenantId_ValueAndStatus(String tenantId, String status);
    // Find by tenant and date range
    @Query("{'tenantId.value': ?0, 'createdAt': {$gte: ?1, $lte: ?2}}")
    List<WebhookDeliveryLog> findByTenantAndDateRange(String tenantId, LocalDateTime start, LocalDateTime end);
    // Find failed deliveries by tenant
    @Query("{'tenantId.value': ?0, 'status': 'FAILED'}")
    List<WebhookDeliveryLog> findFailedByTenant(String tenantId);
    // Find retryable deliveries by tenant
    @Query("{'tenantId.value': ?0, 'status': {$in: ['FAILED', 'PENDING']}}")
    List<WebhookDeliveryLog> findRetryableByTenant(String tenantId);
    // Find recent deliveries by tenant and webhook
    @Query("{'tenantId.value': ?0, 'webhookId': ?1}")
    List<WebhookDeliveryLog> findRecentByTenantAndWebhook(String tenantId, String webhookId);
}
