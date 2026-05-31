package com.gogidix.ecommerce.sms.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class SmsMetrics {

    private final Counter createdCounter;
    private final Counter updatedCounter;
    private final Counter deletedCounter;

    public SmsMetrics(MeterRegistry registry) {
        this.createdCounter = Counter.builder("sms.created").register(registry);
        this.updatedCounter = Counter.builder("sms.updated").register(registry);
        this.deletedCounter = Counter.builder("sms.deleted").register(registry);
    }

    public void recordCreated() { createdCounter.increment(); }
    public void recordUpdated() { updatedCounter.increment(); }
    public void recordDeleted() { deletedCounter.increment(); }
}
