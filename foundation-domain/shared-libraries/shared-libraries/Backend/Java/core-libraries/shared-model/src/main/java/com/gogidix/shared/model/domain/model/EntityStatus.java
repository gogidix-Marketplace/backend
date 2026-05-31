package com.gogidix.shared.model.domain.model;

/**
 * EntityStatus enum for domain entities.
 * Represents the lifecycle status of domain entities.
 */
public enum EntityStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    DELETED,
    PENDING,
    DRAFT,
    ARCHIVED,
    ERROR,
    PROCESSING,
    PERMANENTLY_DELETED
}