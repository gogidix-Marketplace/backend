package com.gogidix.cargo.eventdriven.infrastructure.persistence;

import com.gogidix.cargo.eventdriven.domain.event.BaseDomainEvent;
import com.gogidix.cargo.eventdriven.domain.port.EventStorePort;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryEventStoreAdapter implements EventStorePort {
    private final Map<String, BaseDomainEvent> eventById = new ConcurrentHashMap<>();
    private final Map<String, List<BaseDomainEvent>> eventsByAggregate = new ConcurrentHashMap<>();
    private final Map<String, List<BaseDomainEvent>> eventsByCorrelation = new ConcurrentHashMap<>();
    private final Map<String, List<BaseDomainEvent>> eventsByTenant = new ConcurrentHashMap<>();

    @Override
    public void store(BaseDomainEvent event) {
        eventById.put(event.getEventId(), event);
        eventsByAggregate.computeIfAbsent(event.getAggregateId(), k -> new ArrayList<>()).add(event);
        if (event.getCorrelationId() != null) {
            eventsByCorrelation.computeIfAbsent(event.getCorrelationId(), k -> new ArrayList<>()).add(event);
        }
        if (event.getTenantId() != null) {
            eventsByTenant.computeIfAbsent(event.getTenantId(), k -> new ArrayList<>()).add(event);
        }
    }

    @Override
    public Optional<BaseDomainEvent> findById(String eventId) {
        return Optional.ofNullable(eventById.get(eventId));
    }

    @Override
    public List<BaseDomainEvent> findByAggregateId(String aggregateId) {
        return eventsByAggregate.getOrDefault(aggregateId, Collections.emptyList());
    }

    @Override
    public List<BaseDomainEvent> findByCorrelationId(String correlationId) {
        return eventsByCorrelation.getOrDefault(correlationId, Collections.emptyList());
    }

    @Override
    public List<BaseDomainEvent> findByTenantId(String tenantId, int limit) {
        return eventsByTenant.getOrDefault(tenantId, Collections.emptyList())
            .stream().limit(limit).collect(Collectors.toList());
    }
}
