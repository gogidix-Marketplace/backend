package com.gogidix.ecommerce.loyalty.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public LoyaltyMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("loyalty.created").register(registry);
        this.updatedCounter = Counter.builder("loyalty.updated").register(registry);
        this.deletedCounter = Counter.builder("loyalty.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
