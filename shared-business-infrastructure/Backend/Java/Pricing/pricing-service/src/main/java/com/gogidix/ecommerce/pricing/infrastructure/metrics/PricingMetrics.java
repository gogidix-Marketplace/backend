package com.gogidix.ecommerce.pricing.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class PricingMetrics {

    private final Counter rulesCreatedCounter;
    private final Counter rulesUpdatedCounter;
    private final Counter rulesDeletedCounter;
    private final Timer priceCalculationTimer;
    private final Counter priceCalculationErrorsCounter;

    public PricingMetrics(MeterRegistry registry) {
        this.rulesCreatedCounter = Counter.builder("pricing.rules.created")
                .description("Number of pricing rules created")
                .register(registry);

        this.rulesUpdatedCounter = Counter.builder("pricing.rules.updated")
                .description("Number of pricing rules updated")
                .register(registry);

        this.rulesDeletedCounter = Counter.builder("pricing.rules.deleted")
                .description("Number of pricing rules deleted")
                .register(registry);

        this.priceCalculationTimer = Timer.builder("pricing.calculation.duration")
                .description("Time taken for price calculations")
                .register(registry);

        this.priceCalculationErrorsCounter = Counter.builder("pricing.calculation.errors")
                .description("Number of price calculation errors")
                .register(registry);
    }

    public void recordRuleCreated() { rulesCreatedCounter.increment(); }
    public void recordRuleUpdated() { rulesUpdatedCounter.increment(); }
    public void recordRuleDeleted() { rulesDeletedCounter.increment(); }
    public Timer.Sample startCalculationTimer() { return Timer.start(); }
    public void stopCalculationTimer(Timer.Sample sample) { sample.stop(priceCalculationTimer); }
    public void recordCalculationError() { priceCalculationErrorsCounter.increment(); }
}
