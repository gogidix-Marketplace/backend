package com.gogidix.digitalmarketing.shared.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Base entity class for all domain entities
 * Provides common fields like id, tenantId, createdAt, updatedAt
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @Id
    protected String id;

    @Indexed
    protected String tenantId;

    protected Instant createdAt;

    protected Instant updatedAt;

    @Transient
    protected List<Object> domainEvents;

    /**
     * Constructor for creating a new entity with tenant ID
     */
    protected BaseEntity(String tenantId) {
        this.tenantId = tenantId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.domainEvents = new ArrayList<>();
    }

    /**
     * Update the timestamp
     */
    public void touch() {
        this.updatedAt = Instant.now();
    }

    /**
     * Add a domain event
     */
    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    /**
     * Clear domain events
     */
    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    /**
     * Get domain events
     */
    public List<Object> getDomainEvents() {
        return domainEvents != null ? new ArrayList<>(domainEvents) : new ArrayList<>();
    }
}
