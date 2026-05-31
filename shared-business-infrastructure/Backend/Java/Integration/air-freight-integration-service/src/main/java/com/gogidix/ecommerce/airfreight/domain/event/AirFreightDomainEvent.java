package com.gogidix.ecommerce.airfreight.domain.event;

import java.time.Instant;

public interface AirFreightDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
