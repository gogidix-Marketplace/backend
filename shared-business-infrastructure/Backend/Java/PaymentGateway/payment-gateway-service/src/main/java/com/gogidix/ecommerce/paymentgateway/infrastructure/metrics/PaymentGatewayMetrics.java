package com.gogidix.ecommerce.paymentgateway.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayMetrics {

    private final Counter createCounter;
    private final Counter updateCounter;
    private final Counter deleteCounter;
    private final Counter getCounter;

    public PaymentGatewayMetrics(MeterRegistry registry) {
        this.createCounter = Counter.builder("paymentgateway.create.count")
            .description("Number of PaymentGateway creations")
            .register(registry);
        this.updateCounter = Counter.builder("paymentgateway.update.count")
            .description("Number of PaymentGateway updates")
            .register(registry);
        this.deleteCounter = Counter.builder("paymentgateway.delete.count")
            .description("Number of PaymentGateway deletions")
            .register(registry);
        this.getCounter = Counter.builder("paymentgateway.get.count")
            .description("Number of PaymentGateway retrievals")
            .register(registry);
    }

    public void incrementCreate() { createCounter.increment(); }
    public void incrementUpdate() { updateCounter.increment(); }
    public void incrementDelete() { deleteCounter.increment(); }
    public void incrementGet() { getCounter.increment(); }
}