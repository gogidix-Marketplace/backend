package com.gogidix.ecommerce.notification.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class NotificationMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public NotificationMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("notification.created").register(registry);
        this.updatedCounter = Counter.builder("notification.updated").register(registry);
        this.deletedCounter = Counter.builder("notification.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
