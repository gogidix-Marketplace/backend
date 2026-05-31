package com.gogidix.ecommerce.email.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class EmailMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public EmailMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("email.created").register(registry);
        this.updatedCounter = Counter.builder("email.updated").register(registry);
        this.deletedCounter = Counter.builder("email.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
