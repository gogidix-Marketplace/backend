package com.gogidix.ecommerce.inventorysync.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class InventorySyncMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public InventorySyncMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("inventorysync.created").register(registry);
        this.updatedCounter = Counter.builder("inventorysync.updated").register(registry);
        this.deletedCounter = Counter.builder("inventorysync.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
