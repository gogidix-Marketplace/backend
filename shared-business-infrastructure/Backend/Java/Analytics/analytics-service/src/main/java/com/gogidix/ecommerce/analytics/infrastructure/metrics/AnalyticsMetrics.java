package com.gogidix.ecommerce.analytics.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public AnalyticsMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("analytics.created").register(registry);
        this.updatedCounter = Counter.builder("analytics.updated").register(registry);
        this.deletedCounter = Counter.builder("analytics.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
