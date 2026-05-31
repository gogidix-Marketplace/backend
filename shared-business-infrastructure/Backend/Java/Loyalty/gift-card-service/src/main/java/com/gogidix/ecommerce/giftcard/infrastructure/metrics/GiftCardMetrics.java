package com.gogidix.ecommerce.giftcard.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class GiftCardMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public GiftCardMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("giftcard.created").register(registry);
        this.updatedCounter = Counter.builder("giftcard.updated").register(registry);
        this.deletedCounter = Counter.builder("giftcard.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
