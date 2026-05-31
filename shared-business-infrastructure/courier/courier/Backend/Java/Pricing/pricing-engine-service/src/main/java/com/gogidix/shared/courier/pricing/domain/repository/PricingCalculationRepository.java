package com.gogidix.shared.courier.pricing.domain.repository;

import com.gogidix.shared.courier.pricing.domain.entity.PricingCalculation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Pricing Calculation Entity
 * Manages historical pricing calculation records
 */
@Repository
public interface PricingCalculationRepository extends MongoRepository<PricingCalculation, String> {

    /**
     * Find calculation by tenant and calculation ID
     */
    PricingCalculation findByTenantIdAndCalculationId(String tenantId, String calculationId);

    /**
     * Find calculations by customer
     */
    List<PricingCalculation> findByTenantIdAndCustomerId(String tenantId, String customerId);

    /**
     * Find calculations by service type
     */
    List<PricingCalculation> findByTenantIdAndServiceType(String tenantId, String serviceType);

    /**
     * Find calculations within date range
     */
    List<PricingCalculation> findByTenantIdAndCreatedAtBetween(
            String tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find calculations by request ID
     */
    PricingCalculation findByTenantIdAndRequestId(String tenantId, String requestId);

    /**
     * Find recent calculations with pagination
     */
    Page<PricingCalculation> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find calculations by applied rule
     */
    List<PricingCalculation> findByTenantIdAndAppliedRuleId(String tenantId, String ruleId);

    /**
     * Calculate total revenue for tenant within date range
     */
    @Query(value = "{'tenantId': ?0, 'createdAt': {$gte: ?1, $lte: ?2}}",
           fields = "{'totalAmount': 1}")
    List<PricingCalculation> findByTenantIdAndCreatedAtBetweenForRevenue(
            String tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find calculations created after a certain date
     */
    List<PricingCalculation> findByTenantIdAndCreatedAtAfter(String tenantId, LocalDateTime date);

    /**
     * Count calculations by tenant
     */
    Long countByTenantId(String tenantId);
}
