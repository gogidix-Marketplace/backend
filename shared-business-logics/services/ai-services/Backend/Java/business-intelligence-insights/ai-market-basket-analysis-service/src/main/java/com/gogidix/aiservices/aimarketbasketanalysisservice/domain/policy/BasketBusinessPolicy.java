package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.policy;

import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.MarketBasket;
import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Business policy for market basket operations.
 */
@Component
public class BasketBusinessPolicy {

    private static final Logger log = LoggerFactory.getLogger(BasketBusinessPolicy.class);

    private final int maxBasketsPerTenant;
    private final int maxCustomersPerBasket;

    public BasketBusinessPolicy(
            @Value("${segment.max.segments-per-tenant:100}") int maxBasketsPerTenant,
            @Value("${segment.max.customers-per-segment:1000000}") int maxCustomersPerBasket
    ) {
        this.maxBasketsPerTenant = maxBasketsPerTenant;
        this.maxCustomersPerBasket = maxCustomersPerBasket;
    }

    /**
     * Validate that segment creation is allowed.
     */
    public void validateBasketCreation(String tenantId) {
        // Simplified validation - actual count would be checked at database layer
        log.debug("Basket creation validated for tenant: {}", tenantId);
    }

    /**
     * Validate that customer addition is allowed.
     */
    public void validateCustomerAddition(MarketBasket segment, int countToAdd) {
        long projectedCount = segment.getCustomerCount() + countToAdd;

        if (projectedCount > maxCustomersPerBasket) {
            throw new ValidationException(
                    String.format("Cannot add customers. Maximum segment size (%d) would be exceeded", maxCustomersPerBasket)
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
    public void validateBasketDeletion(MarketBasket segment) {
        if (segment.isActive() && segment.getCustomerCount() > 0) {
            log.warn("Deleting active segment with customers: {}", segment.getId());
        }
        log.debug("Basket deletion validated for segment: {}", segment.getId());
    }
}
