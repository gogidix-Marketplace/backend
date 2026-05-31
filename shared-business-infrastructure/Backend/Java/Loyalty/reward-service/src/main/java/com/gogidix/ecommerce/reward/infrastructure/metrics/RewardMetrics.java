package com.gogidix.ecommerce.reward.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class RewardMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public RewardMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("reward.created").register(registry);
        this.updatedCounter = Counter.builder("reward.updated").register(registry);
        this.deletedCounter = Counter.builder("reward.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
