package com.gogidix.aiservices.aifrauddetectionservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

/**
 * Abstract base class for all DTOs.
 * Provides common fields for all data transfer objects.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDTO {

    /**
     * Unique identifier for the entity.
     */
    protected String id;

    /**
     * Timestamp when the entity was created.
     */
    protected Instant createdAt;

    /**
     * Timestamp when the entity was last updated.
     */
    protected Instant updatedAt;
}
