package com.gogidix.shared.warehousing.tenant.infrastructure.messaging;

import com.gogidix.shared.warehousing.tenant.domain.events.TenantCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Tenant Event Publisher
 *
 * Publishes tenant-related events to Kafka
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TenantEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TENANT_CREATED_TOPIC = "tenant.created";

    /**
     * Publish tenant created event
     */
    public void publishTenantCreated(TenantCreatedEvent event) {
        try {
            kafkaTemplate.send(TENANT_CREATED_TOPIC, event.getTenantId(), event);
            log.info("Published tenant created event for tenant: {}", event.getTenantId());
        } catch (Exception e) {
            log.error("Failed to publish tenant created event for tenant: {}", event.getTenantId(), e);
            throw new RuntimeException("Failed to publish tenant created event", e);
        }
    }
}
