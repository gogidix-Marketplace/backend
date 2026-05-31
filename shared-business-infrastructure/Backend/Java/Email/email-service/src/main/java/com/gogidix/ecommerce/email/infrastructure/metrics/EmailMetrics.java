package com.gogidix.ecommerce.email.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class EmailMetrics {

    private final Counter createCounter;
    private final Counter updateCounter;
    private final Counter deleteCounter;
    private final Counter getCounter;

    public EmailMetrics(MeterRegistry registry) {
        this.createCounter = Counter.builder("email.create.count")
            .description("Number of Email creations")
            .register(registry);
        this.updateCounter = Counter.builder("email.update.count")
            .description("Number of Email updates")
            .register(registry);
        this.deleteCounter = Counter.builder("email.delete.count")
            .description("Number of Email deletions")
            .register(registry);
        this.getCounter = Counter.builder("email.get.count")
            .description("Number of Email retrievals")
            .register(registry);
    }

    public void incrementCreate() { createCounter.increment(); }
    public void incrementUpdate() { updateCounter.increment(); }
    public void incrementDelete() { deleteCounter.increment(); }
    public void incrementGet() { getCounter.increment(); }
}