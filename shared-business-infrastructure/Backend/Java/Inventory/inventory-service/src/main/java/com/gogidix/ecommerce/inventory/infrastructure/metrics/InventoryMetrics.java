package com.gogidix.ecommerce.inventory.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class InventoryMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public InventoryMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("inventory.created").register(registry);
        this.updatedCounter = Counter.builder("inventory.updated").register(registry);
        this.deletedCounter = Counter.builder("inventory.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
