package com.gogidix.ecommerce.search.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class SearchMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public SearchMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("search.created").register(registry);
        this.updatedCounter = Counter.builder("search.updated").register(registry);
        this.deletedCounter = Counter.builder("search.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
