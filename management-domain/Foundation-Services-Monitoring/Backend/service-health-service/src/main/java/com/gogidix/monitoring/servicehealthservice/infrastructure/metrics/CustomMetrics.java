package com.gogidix.monitoring.servicehealthservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    private final Counter operationCounter;

    public CustomMetrics(MeterRegistry meterRegistry) {
        this.operationCounter = Counter.builder("servicehealthservice.operations")
                .description("Number of operations")
                .register(meterRegistry);
    }

    public void incrementOperation() {
        operationCounter.increment();
    }
}
