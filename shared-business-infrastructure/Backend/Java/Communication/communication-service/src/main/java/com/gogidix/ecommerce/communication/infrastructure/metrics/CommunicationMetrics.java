package com.gogidix.ecommerce.communication.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CommunicationMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public CommunicationMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("communication.created").register(registry);
        this.updatedCounter = Counter.builder("communication.updated").register(registry);
        this.deletedCounter = Counter.builder("communication.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
