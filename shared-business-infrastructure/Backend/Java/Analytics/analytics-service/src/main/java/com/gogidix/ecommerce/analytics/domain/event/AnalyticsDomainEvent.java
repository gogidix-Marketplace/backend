package com.gogidix.ecommerce.analytics.domain.event;

import java.time.Instant;

public interface AnalyticsDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
