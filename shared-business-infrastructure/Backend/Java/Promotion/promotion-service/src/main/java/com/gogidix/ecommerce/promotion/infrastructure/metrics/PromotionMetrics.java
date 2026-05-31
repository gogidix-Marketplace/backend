package com.gogidix.ecommerce.promotion.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PromotionMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public PromotionMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("promotion.created").register(registry);
        this.updatedCounter = Counter.builder("promotion.updated").register(registry);
        this.deletedCounter = Counter.builder("promotion.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
