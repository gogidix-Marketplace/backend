package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.shared.requestcontext.TenantContextHolder;
import lombok.Getter;

import java.time.Instant;

/**
 * Base class for all domain events.
 * Domain events represent important state changes in the domain that need to be communicated to other services.
 */
@Getter
public abstract class DomainEvent {
    private final Instant occurredAt;
    private final String eventType;
    private final String aggregateType;
    private final String aggregateId;
    private final String tenantId;
    private final String correlationId;

    protected DomainEvent(String eventType, String aggregateType, String aggregateId) {
        this.occurredAt = Instant.now();
        this.eventType = eventType;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.tenantId = TenantContextHolder.getTenantId();
        this.correlationId = TenantContextHolder.getCorrelationId();
    }

    /**
     * Get the event type for serialization.
     *
     * @return the event type
     */
    public String getEventType() {
        return eventType;
    }
}
