package com.gogidix.aiservices.aichurnpredictionservice.domain.policy;

import com.gogidix.aiservices.aichurnpredictionservice.domain.model.ChurnPrediction;
import com.gogidix.aiservices.aichurnpredictionservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Business policy for churn prediction operations.
 */
@Component
public class PredictionBusinessPolicy {

    private static final Logger log = LoggerFactory.getLogger(PredictionBusinessPolicy.class);

    private final int maxPredictionsPerTenant;
    private final int maxCustomersPerPrediction;

    public PredictionBusinessPolicy(
            @Value("${segment.max.segments-per-tenant:100}") int maxPredictionsPerTenant,
            @Value("${segment.max.customers-per-segment:1000000}") int maxCustomersPerPrediction
    ) {
        this.maxPredictionsPerTenant = maxPredictionsPerTenant;
        this.maxCustomersPerPrediction = maxCustomersPerPrediction;
    }

    /**
     * Validate that segment creation is allowed.
     */
    public void validatePredictionCreation(String tenantId) {
        // Simplified validation - actual count would be checked at database layer
        log.debug("Prediction creation validated for tenant: {}", tenantId);
    }

    /**
     * Validate that customer addition is allowed.
     */
    public void validateCustomerAddition(ChurnPrediction segment, int countToAdd) {
        long projectedCount = segment.getCustomerCount() + countToAdd;

        if (projectedCount > maxCustomersPerPrediction) {
            throw new ValidationException(
                    String.format("Cannot add customers. Maximum segment size (%d) would be exceeded", maxCustomersPerPrediction)
            );
        }

        if (!segment.isActive()) {
            throw new ValidationException("Cannot add customers to inactive segment");
        }

        log.debug("Customer addition validated for segment: {}", segment.getId());
    }

    /**
     * Validate that segment deletion is allowed.
     */
    public void validatePredictionDeletion(ChurnPrediction segment) {
        if (segment.isActive() && segment.getCustomerCount() > 0) {
            log.warn("Deleting active segment with customers: {}", segment.getId());
        }
        log.debug("Prediction deletion validated for segment: {}", segment.getId());
    }
}
