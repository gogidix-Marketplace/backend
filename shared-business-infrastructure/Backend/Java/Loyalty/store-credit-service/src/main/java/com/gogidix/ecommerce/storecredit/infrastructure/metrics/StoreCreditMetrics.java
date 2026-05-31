package com.gogidix.ecommerce.storecredit.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class StoreCreditMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public StoreCreditMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("storecredit.created").register(registry);
        this.updatedCounter = Counter.builder("storecredit.updated").register(registry);
        this.deletedCounter = Counter.builder("storecredit.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
