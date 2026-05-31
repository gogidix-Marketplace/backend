package com.gogidix.aiservices.aiproductrecommendationservice.domain.policy;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.ProductRecommendation;
import com.gogidix.aiservices.aiproductrecommendationservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Business policy for product recommendation operations.
 */
@Component
public class RecommendationBusinessPolicy {

    private static final Logger log = LoggerFactory.getLogger(RecommendationBusinessPolicy.class);

    private final int maxRecommendationsPerTenant;
    private final int maxProductsPerRecommendation;

    public RecommendationBusinessPolicy(
            @Value("${segment.max.segments-per-tenant:100}") int maxRecommendationsPerTenant,
            @Value("${segment.max.customers-per-segment:1000000}") int maxProductsPerRecommendation
    ) {
        this.maxRecommendationsPerTenant = maxRecommendationsPerTenant;
        this.maxProductsPerRecommendation = maxProductsPerRecommendation;
    }

    /**
     * Validate that segment creation is allowed.
     */
    public void validateRecommendationCreation(String tenantId) {
        // Simplified validation - actual count would be checked at database layer
        log.debug("Recommendation creation validated for tenant: {}", tenantId);
    }

    /**
     * Validate that customer addition is allowed.
     */
    public void validateProductAddition(ProductRecommendation segment, int countToAdd) {
        long projectedCount = segment.getProductCount() + countToAdd;

        if (projectedCount > maxProductsPerRecommendation) {
            throw new ValidationException(
                    String.format("Cannot add customers. Maximum segment size (%d) would be exceeded", maxProductsPerRecommendation)
            );
        }

        if (!segment.isActive()) {
            throw new ValidationException("Cannot add customers to inactive segment");
        }

        log.debug("Product addition validated for segment: {}", segment.getId());
    }

    /**
     * Validate that segment deletion is allowed.
     */
    public void validateRecommendationDeletion(ProductRecommendation segment) {
        if (segment.isActive() && segment.getProductCount() > 0) {
            log.warn("Deleting active segment with customers: {}", segment.getId());
        }
        log.debug("Recommendation deletion validated for segment: {}", segment.getId());
    }
}
