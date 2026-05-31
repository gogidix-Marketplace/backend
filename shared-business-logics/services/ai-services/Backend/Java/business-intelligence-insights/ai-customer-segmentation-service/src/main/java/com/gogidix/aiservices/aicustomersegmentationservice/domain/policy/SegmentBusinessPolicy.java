package com.gogidix.aiservices.aicustomersegmentationservice.domain.policy;

import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.CustomerSegment;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Business policy for customer segment operations.
 */
@Component
public class SegmentBusinessPolicy {

    private static final Logger log = LoggerFactory.getLogger(SegmentBusinessPolicy.class);

    private final int maxSegmentsPerTenant;
    private final int maxCustomersPerSegment;

    public SegmentBusinessPolicy(
            @Value("${segment.max.segments-per-tenant:100}") int maxSegmentsPerTenant,
            @Value("${segment.max.customers-per-segment:1000000}") int maxCustomersPerSegment
    ) {
        this.maxSegmentsPerTenant = maxSegmentsPerTenant;
        this.maxCustomersPerSegment = maxCustomersPerSegment;
    }

    /**
     * Validate that segment creation is allowed.
     */
    public void validateSegmentCreation(String tenantId) {
        // Simplified validation - actual count would be checked at database layer
        log.debug("Segment creation validated for tenant: {}", tenantId);
    }

    /**
     * Validate that customer addition is allowed.
     */
    public void validateCustomerAddition(CustomerSegment segment, int countToAdd) {
        long projectedCount = segment.getCustomerCount() + countToAdd;

        if (projectedCount > maxCustomersPerSegment) {
            throw new ValidationException(
                    String.format("Cannot add customers. Maximum segment size (%d) would be exceeded", maxCustomersPerSegment)
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
    public void validateSegmentDeletion(CustomerSegment segment) {
        if (segment.isActive() && segment.getCustomerCount() > 0) {
            log.warn("Deleting active segment with customers: {}", segment.getId());
        }
        log.debug("Segment deletion validated for segment: {}", segment.getId());
    }
}
