package com.gogidix.cargo.eventdriven.domain.port;

import com.gogidix.cargo.eventdriven.domain.event.BaseDomainEvent;
import java.util.List;
import java.util.Optional;

public interface EventStorePort {
    void store(BaseDomainEvent event);
    Optional<BaseDomainEvent> findById(String eventId);
    List<BaseDomainEvent> findByAggregateId(String aggregateId);
    List<BaseDomainEvent> findByCorrelationId(String correlationId);
    List<BaseDomainEvent> findByTenantId(String tenantId, int limit);
}
