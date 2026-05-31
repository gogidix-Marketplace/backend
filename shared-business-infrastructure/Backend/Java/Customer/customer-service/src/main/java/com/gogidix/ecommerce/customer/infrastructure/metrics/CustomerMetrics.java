package com.gogidix.ecommerce.customer.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomerMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public CustomerMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("customer.created").register(registry);
        this.updatedCounter = Counter.builder("customer.updated").register(registry);
        this.deletedCounter = Counter.builder("customer.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
