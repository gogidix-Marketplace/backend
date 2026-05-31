package com.gogidix.hr.benefitsadministration.domain.port.out;

import com.gogidix.hr.benefitsadministration.domain.enums.BenefitType;
import com.gogidix.hr.benefitsadministration.domain.model.BenefitPlan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Output port for benefit plan repository operations
 * Part of hexagonal architecture - secondary port
 */
public interface BenefitPlanRepositoryPort {

    /**
     * Save a benefit plan
     * @param plan the benefit plan to save
     * @return the saved benefit plan
     */
    BenefitPlan save(BenefitPlan plan);

    /**
     * Find benefit plan by ID
     * @param planId the plan ID
     * @param tenantId the tenant ID
     * @return optional benefit plan
     */
    Optional<BenefitPlan> findById(String planId, String tenantId);

    /**
     * Find all benefit plans by tenant
     * @param tenantId the tenant ID
     * @return list of benefit plans
     */
    List<BenefitPlan> findByTenantId(String tenantId);

    /**
     * Find active benefit plans by tenant
     * @param tenantId the tenant ID
     * @return list of active benefit plans
     */
    List<BenefitPlan> findActiveByTenantId(String tenantId);

    /**
     * Find benefit plans by tenant and country
     * @param tenantId the tenant ID
     * @param countryCode the country code
     * @return list of benefit plans
     */
    List<BenefitPlan> findByTenantAndCountry(String tenantId, String countryCode);

    /**
     * Find benefit plans by type
     * @param tenantId the tenant ID
     * @param benefitType the benefit type
     * @return list of benefit plans
     */
    List<BenefitPlan> findByType(String tenantId, BenefitType benefitType);

    /**
     * Find benefit plans effective on a date
     * @param tenantId the tenant ID
     * @param effectiveDate the effective date
     * @return list of benefit plans
     */
    List<BenefitPlan> findEffectiveOn(String tenantId, LocalDate effectiveDate);

    /**
     * Find benefit plans expiring before a date
     * @param tenantId the tenant ID
     * @param expiryDate the expiry date
     * @return list of benefit plans
     */
    List<BenefitPlan> findExpiringBefore(String tenantId, LocalDate expiryDate);

    /**
     * Find benefit plan by code
     * @param planCode the plan code
     * @param tenantId the tenant ID
     * @return optional benefit plan
     */
    Optional<BenefitPlan> findByCode(String planCode, String tenantId);

    /**
     * Delete a benefit plan
     * @param planId the plan ID
     * @param tenantId the tenant ID
     */
    void deleteById(String planId, String tenantId);

    /**
     * Check if plan exists
     * @param planId the plan ID
     * @param tenantId the tenant ID
     * @return true if exists, false otherwise
     */
    boolean existsById(String planId, String tenantId);
}
