package com.gogidix.ecommerce.airfreight.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class AirFreightMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public AirFreightMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("airfreight.created").register(registry);
        this.updatedCounter = Counter.builder("airfreight.updated").register(registry);
        this.deletedCounter = Counter.builder("airfreight.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
