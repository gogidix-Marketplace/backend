package com.gogidix.ecommerce.oceanshipping.domain.event;

import java.time.Instant;

public interface OceanShippingDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
