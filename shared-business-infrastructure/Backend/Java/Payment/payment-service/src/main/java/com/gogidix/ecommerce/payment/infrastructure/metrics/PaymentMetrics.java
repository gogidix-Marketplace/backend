package com.gogidix.ecommerce.payment.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PaymentMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public PaymentMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("payment.created").register(registry);
        this.updatedCounter = Counter.builder("payment.updated").register(registry);
        this.deletedCounter = Counter.builder("payment.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
