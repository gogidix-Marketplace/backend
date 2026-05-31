package com.gogidix.ecommerce.oceanshipping.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class OceanShippingMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public OceanShippingMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("oceanshipping.created").register(registry);
        this.updatedCounter = Counter.builder("oceanshipping.updated").register(registry);
        this.deletedCounter = Counter.builder("oceanshipping.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
