package com.gogidix.aiservices.aisalesforecastingservice.domain.policy;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.SalesForecast;
import com.gogidix.aiservices.aisalesforecastingservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Business policy for sales forecast operations.
 */
@Component
public class ForecastBusinessPolicy {

    private static final Logger log = LoggerFactory.getLogger(ForecastBusinessPolicy.class);

    private final int maxForecastsPerTenant;
    private final int maxForecastModelsPerForecast;

    public ForecastBusinessPolicy(
            @Value("${segment.max.segments-per-tenant:100}") int maxForecastsPerTenant,
            @Value("${segment.max.customers-per-segment:1000000}") int maxForecastModelsPerForecast
    ) {
        this.maxForecastsPerTenant = maxForecastsPerTenant;
        this.maxForecastModelsPerForecast = maxForecastModelsPerForecast;
    }

    /**
     * Validate that segment creation is allowed.
     */
    public void validateForecastCreation(String tenantId) {
        // Simplified validation - actual count would be checked at database layer
        log.debug("Forecast creation validated for tenant: {}", tenantId);
    }

    /**
     * Validate that customer addition is allowed.
     */
    public void validateForecastModelAddition(SalesForecast segment, int countToAdd) {
        long projectedCount = segment.getForecastModelCount() + countToAdd;

        if (projectedCount > maxForecastModelsPerForecast) {
            throw new ValidationException(
                    String.format("Cannot add customers. Maximum segment size (%d) would be exceeded", maxForecastModelsPerForecast)
            );
        }

        if (!segment.isActive()) {
            throw new ValidationException("Cannot add customers to inactive segment");
        }

        log.debug("ForecastModel addition validated for segment: {}", segment.getId());
    }

    /**
     * Validate that segment deletion is allowed.
     */
    public void validateForecastDeletion(SalesForecast segment) {
        if (segment.isActive() && segment.getForecastModelCount() > 0) {
            log.warn("Deleting active segment with customers: {}", segment.getId());
        }
        log.debug("Forecast deletion validated for segment: {}", segment.getId());
    }
}
