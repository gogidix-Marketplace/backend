package com.gogidix.ecommerce.pushnotification.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public PushNotificationMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("pushnotification.created").register(registry);
        this.updatedCounter = Counter.builder("pushnotification.updated").register(registry);
        this.deletedCounter = Counter.builder("pushnotification.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
