package com.gogidix.transaction.audit.domain.model;

/**
 * Enumeration of actor types for audit logs.
 */
public enum ActorType {
    USER,
    SYSTEM,
    SERVICE,
    API,
    WEBHOOK,
    SCHEDULER
}
