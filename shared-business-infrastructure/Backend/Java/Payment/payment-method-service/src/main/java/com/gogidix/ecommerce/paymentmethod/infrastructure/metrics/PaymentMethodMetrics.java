package com.gogidix.ecommerce.paymentmethod.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public PaymentMethodMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("paymentmethod.created").register(registry);
        this.updatedCounter = Counter.builder("paymentmethod.updated").register(registry);
        this.deletedCounter = Counter.builder("paymentmethod.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
