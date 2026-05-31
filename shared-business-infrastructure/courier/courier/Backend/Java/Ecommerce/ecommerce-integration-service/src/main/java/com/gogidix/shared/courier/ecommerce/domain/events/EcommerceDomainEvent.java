package com.gogidix.shared.courier.ecommerce.domain.events;

import java.time.Instant;

public interface EcommerceDomainEvent {
    String getEventId();
    String getAggregateId();
    String getOrderId();
    Instant getOccurredAt();
    default String getEventType() { return this.getClass().getSimpleName(); }
}
