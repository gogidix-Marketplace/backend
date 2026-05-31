package com.gogidix.ecommerce.search.domain.event;

import java.time.Instant;

public interface SearchDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
