package com.gogidix.ecommerce.warehouse.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class WarehouseMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public WarehouseMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("warehouse.created").register(registry);
        this.updatedCounter = Counter.builder("warehouse.updated").register(registry);
        this.deletedCounter = Counter.builder("warehouse.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
