package com.gogidix.ecommerce.pushnotification.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationMetrics {

    private final Counter createCounter;
    private final Counter updateCounter;
    private final Counter deleteCounter;
    private final Counter getCounter;

    public PushNotificationMetrics(MeterRegistry registry) {
        this.createCounter = Counter.builder("pushnotification.create.count")
            .description("Number of PushNotification creations")
            .register(registry);
        this.updateCounter = Counter.builder("pushnotification.update.count")
            .description("Number of PushNotification updates")
            .register(registry);
        this.deleteCounter = Counter.builder("pushnotification.delete.count")
            .description("Number of PushNotification deletions")
            .register(registry);
        this.getCounter = Counter.builder("pushnotification.get.count")
            .description("Number of PushNotification retrievals")
            .register(registry);
    }

    public void incrementCreate() { createCounter.increment(); }
    public void incrementUpdate() { updateCounter.increment(); }
    public void incrementDelete() { deleteCounter.increment(); }
    public void incrementGet() { getCounter.increment(); }
}