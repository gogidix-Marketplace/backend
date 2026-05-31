package com.gogidix.shared.courier.dispatch.domain.entity;

/**
 * Dispatch Order Status enum
 */
public enum DispatchStatus {
    PENDING,
    CONFIRMED,
    ASSIGNED,
    PICKUP_IN_PROGRESS,
    PICKED_UP,
    IN_TRANSIT,
    DELIVERY_IN_PROGRESS,
    DELIVERED,
    CANCELLED,
    FAILED
}
