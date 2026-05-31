package com.gogidix.ecommerce.analytics.domain.port.out;

import com.gogidix.ecommerce.analytics.domain.event.AnalyticsDomainEvent;

public interface AnalyticsEventPublisher {
    void publish(AnalyticsDomainEvent event);
}
