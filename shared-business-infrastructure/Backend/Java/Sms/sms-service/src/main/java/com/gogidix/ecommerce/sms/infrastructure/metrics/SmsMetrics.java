package com.gogidix.ecommerce.sms.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class SmsMetrics {

    private final Counter createCounter;
    private final Counter updateCounter;
    private final Counter deleteCounter;
    private final Counter getCounter;

    public SmsMetrics(MeterRegistry registry) {
        this.createCounter = Counter.builder("sms.create.count")
            .description("Number of Sms creations")
            .register(registry);
        this.updateCounter = Counter.builder("sms.update.count")
            .description("Number of Sms updates")
            .register(registry);
        this.deleteCounter = Counter.builder("sms.delete.count")
            .description("Number of Sms deletions")
            .register(registry);
        this.getCounter = Counter.builder("sms.get.count")
            .description("Number of Sms retrievals")
            .register(registry);
    }

    public void incrementCreate() { createCounter.increment(); }
    public void incrementUpdate() { updateCounter.increment(); }
    public void incrementDelete() { deleteCounter.increment(); }
    public void incrementGet() { getCounter.increment(); }
}