package com.gogidix.ecommerce.discount.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class DiscountMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public DiscountMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("discount.created").register(registry);
        this.updatedCounter = Counter.builder("discount.updated").register(registry);
        this.deletedCounter = Counter.builder("discount.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
