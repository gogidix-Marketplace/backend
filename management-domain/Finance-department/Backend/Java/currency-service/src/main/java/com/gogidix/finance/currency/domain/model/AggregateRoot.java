package com.gogidix.finance.currency.domain.model;

import com.gogidix.finance.currency.domain.event.DomainEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate Root Base Class
 * Provides domain event management for aggregate roots
 * Following DDD principles, aggregates emit domain events
 */
@Getter
public abstract class AggregateRoot extends BaseEntity {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot(String tenantId) {
        super(tenantId);
    }

    protected AggregateRoot() {
        super();
    }

    /**
     * Add a domain event to be published
     */
    protected void registerEvent(DomainEvent event) {
        if (event != null) {
            domainEvents.add(event);
        }
    }

    /**
     * Get all unpublished domain events
     */
    public List<DomainEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Mark events as published
     */
    public void markEventsAsCommitted() {
        domainEvents.clear();
    }

    /**
     * Check if there are uncommitted events
     */
    public boolean hasUncommittedEvents() {
        return !domainEvents.isEmpty();
    }
}
