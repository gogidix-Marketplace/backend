package com.gogidix.ecommerce.wishlist.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class WishlistMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public WishlistMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("wishlist.created").register(registry);
        this.updatedCounter = Counter.builder("wishlist.updated").register(registry);
        this.deletedCounter = Counter.builder("wishlist.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
