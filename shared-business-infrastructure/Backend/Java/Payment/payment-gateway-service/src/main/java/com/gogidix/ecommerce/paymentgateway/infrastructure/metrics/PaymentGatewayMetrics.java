package com.gogidix.ecommerce.paymentgateway.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public PaymentGatewayMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("paymentgateway.created").register(registry);
        this.updatedCounter = Counter.builder("paymentgateway.updated").register(registry);
        this.deletedCounter = Counter.builder("paymentgateway.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
