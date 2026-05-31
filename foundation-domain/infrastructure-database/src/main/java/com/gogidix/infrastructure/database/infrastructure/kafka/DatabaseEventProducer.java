package com.gogidix.infrastructure.database.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.infrastructure.database.domain.model.ConnectionPoolConfiguration;
import com.gogidix.infrastructure.database.domain.model.DatabaseMigration;
import com.gogidix.infrastructure.database.domain.model.TenantDatabaseConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka event producer for database infrastructure events.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DatabaseEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String CONNECTION_POOL_TOPIC = "infrastructure.database.connection-pool.events";
    private static final String MIGRATION_TOPIC = "infrastructure.database.migration.events";
    private static final String TENANT_DATABASE_TOPIC = "infrastructure.database.tenant.events";
    private static final String QUERY_METRIC_TOPIC = "infrastructure.database.query-metrics.events";
    private static final String BACKUP_TOPIC = "infrastructure.database.backup.events";
    private static final String TRANSACTION_TOPIC = "infrastructure.database.transaction.events";

    /**
     * Publish connection pool created event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishConnectionPoolCreated(
            ConnectionPoolConfiguration configuration) {

        Map<String, Object> event = createEvent("connection-pool-created", configuration);
        return publishEvent(CONNECTION_POOL_TOPIC, configuration.getTenantId(), event);
    }

    /**
     * Publish connection pool updated event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishConnectionPoolUpdated(
            ConnectionPoolConfiguration configuration) {

        Map<String, Object> event = createEvent("connection-pool-updated", configuration);
        return publishEvent(CONNECTION_POOL_TOPIC, configuration.getTenantId(), event);
    }

    /**
     * Publish connection pool deleted event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishConnectionPoolDeleted(
            String tenantId, String poolName) {

        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "connection-pool-deleted");
        event.put("tenantId", tenantId);
        event.put("poolName", poolName);
        event.put("timestamp", LocalDateTime.now().toString());

        return publishEvent(CONNECTION_POOL_TOPIC, tenantId, event);
    }

    /**
     * Publish migration executed event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishMigrationExecuted(
            DatabaseMigration migration) {

        Map<String, Object> event = createEvent("migration-executed", migration);
        return publishEvent(MIGRATION_TOPIC, migration.getTenantId(), event);
    }

    /**
     * Publish migration failed event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishMigrationFailed(
            DatabaseMigration migration) {

        Map<String, Object> event = createEvent("migration-failed", migration);
        return publishEvent(MIGRATION_TOPIC, migration.getTenantId(), event);
    }

    /**
     * Publish tenant database created event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishTenantCreated(
            TenantDatabaseConfiguration configuration) {

        Map<String, Object> event = createEvent("tenant-database-created", configuration);
        return publishEvent(TENANT_DATABASE_TOPIC, configuration.getTenantId(), event);
    }

    /**
     * Publish tenant database activated event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishTenantActivated(
            TenantDatabaseConfiguration configuration) {

        Map<String, Object> event = createEvent("tenant-database-activated", configuration);
        return publishEvent(TENANT_DATABASE_TOPIC, configuration.getTenantId(), event);
    }

    /**
     * Publish tenant database deactivated event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishTenantDeactivated(
            TenantDatabaseConfiguration configuration) {

        Map<String, Object> event = createEvent("tenant-database-deactivated", configuration);
        return publishEvent(TENANT_DATABASE_TOPIC, configuration.getTenantId(), event);
    }

    /**
     * Publish tenant database deprovisioned event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishTenantDeprovisioned(
            String tenantId) {

        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "tenant-database-deprovisioned");
        event.put("tenantId", tenantId);
        event.put("timestamp", LocalDateTime.now().toString());

        return publishEvent(TENANT_DATABASE_TOPIC, tenantId, event);
    }

    /**
     * Publish slow query alert event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishSlowQueryAlert(
            String tenantId, String queryHash, long executionDuration, String query) {

        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "slow-query-alert");
        event.put("tenantId", tenantId);
        event.put("queryHash", queryHash);
        event.put("executionDuration", executionDuration);
        event.put("query", query);
        event.put("timestamp", LocalDateTime.now().toString());

        return publishEvent(QUERY_METRIC_TOPIC, tenantId, event);
    }

    /**
     * Publish backup completed event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishBackupCompleted(
            String tenantId, String backupName, String backupLocation, long fileSize) {

        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "backup-completed");
        event.put("tenantId", tenantId);
        event.put("backupName", backupName);
        event.put("backupLocation", backupLocation);
        event.put("fileSize", fileSize);
        event.put("timestamp", LocalDateTime.now().toString());

        return publishEvent(BACKUP_TOPIC, tenantId, event);
    }

    /**
     * Publish backup failed event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishBackupFailed(
            String tenantId, String backupName, String errorMessage) {

        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "backup-failed");
        event.put("tenantId", tenantId);
        event.put("backupName", backupName);
        event.put("errorMessage", errorMessage);
        event.put("timestamp", LocalDateTime.now().toString());

        return publishEvent(BACKUP_TOPIC, tenantId, event);
    }

    /**
     * Publish transaction committed event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishTransactionCommitted(
            String tenantId, String transactionId, String transactionType) {

        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "transaction-committed");
        event.put("tenantId", tenantId);
        event.put("transactionId", transactionId);
        event.put("transactionType", transactionType);
        event.put("timestamp", LocalDateTime.now().toString());

        return publishEvent(TRANSACTION_TOPIC, tenantId, event);
    }

    /**
     * Publish transaction rolled back event.
     */
    @Transactional
    public CompletableFuture<SendResult<String, String>> publishTransactionRolledBack(
            String tenantId, String transactionId, String transactionType, String reason) {

        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "transaction-rolled-back");
        event.put("tenantId", tenantId);
        event.put("transactionId", transactionId);
        event.put("transactionType", transactionType);
        event.put("reason", reason);
        event.put("timestamp", LocalDateTime.now().toString());

        return publishEvent(TRANSACTION_TOPIC, tenantId, event);
    }

    /**
     * Generic event publisher.
     */
    private CompletableFuture<SendResult<String, String>> publishEvent(
            String topic, String key, Map<String, Object> event) {

        try {
            String eventJson = objectMapper.writeValueAsString(event);
            log.debug("Publishing event to topic {}: {}", topic, eventJson);

            return kafkaTemplate.send(topic, key, eventJson)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish event to topic {}: {}", topic, ex.getMessage());
                        } else {
                            log.debug("Event published successfully to topic {}", topic);
                        }
                    });

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Create event map from domain object.
     */
    private Map<String, Object> createEvent(String eventType, Object data) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", eventType);
        event.put("data", data);
        event.put("timestamp", LocalDateTime.now().toString());
        return event;
    }
}
