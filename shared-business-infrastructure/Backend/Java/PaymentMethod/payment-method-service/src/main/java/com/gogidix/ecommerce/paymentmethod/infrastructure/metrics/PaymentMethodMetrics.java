package com.gogidix.ecommerce.paymentmethod.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMetrics {

    private final Counter createCounter;
    private final Counter updateCounter;
    private final Counter deleteCounter;
    private final Counter getCounter;

    public PaymentMethodMetrics(MeterRegistry registry) {
        this.createCounter = Counter.builder("paymentmethod.create.count")
            .description("Number of PaymentMethod creations")
            .register(registry);
        this.updateCounter = Counter.builder("paymentmethod.update.count")
            .description("Number of PaymentMethod updates")
            .register(registry);
        this.deleteCounter = Counter.builder("paymentmethod.delete.count")
            .description("Number of PaymentMethod deletions")
            .register(registry);
        this.getCounter = Counter.builder("paymentmethod.get.count")
            .description("Number of PaymentMethod retrievals")
            .register(registry);
    }

    public void incrementCreate() { createCounter.increment(); }
    public void incrementUpdate() { updateCounter.increment(); }
    public void incrementDelete() { deleteCounter.increment(); }
    public void incrementGet() { getCounter.increment(); }
}