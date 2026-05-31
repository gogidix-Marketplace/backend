package com.gogidix.hr.benefitsadministration.domain.port.in;

import com.gogidix.hr.benefitsadministration.application.dto.request.CalculatePremiumRequest;
import com.gogidix.hr.benefitsadministration.application.dto.response.PremiumCalculationResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Use case interface for calculating benefit premiums
 * Part of hexagonal architecture - input port
 */
public interface CalculateBenefitPremiumUseCase {

    /**
     * Calculate premium for a benefit enrollment
     * @param request the premium calculation request
     * @return the premium calculation response
     */
    PremiumCalculationResponse calculatePremium(CalculatePremiumRequest request);

    /**
     * Calculate premium for a specific plan and coverage
     * @param planId the benefit plan ID
     * @param coverageLevel the coverage level
     * @param dependentsCount number of dependents
     * @return the calculated premium
     */
    PremiumCalculationResponse calculatePremiumForPlan(String planId, String coverageLevel, int dependentsCount);

    /**
     * Estimate annual premium cost for an employee
     * @param employeeId the employee ID
     * @param planId the benefit plan ID
     * @return the estimated annual premium
     */
    BigDecimal estimateAnnualPremium(String employeeId, String planId);

    /**
     * Get premium breakdown by period
     * @param enrollmentId the enrollment ID
     * @param startDate start date
     * @param endDate end date
     * @return list of premium amounts per period
     */
    java.util.List<PremiumCalculationResponse.PremiumBreakdown> getPremiumBreakdown(String enrollmentId, LocalDate startDate, LocalDate endDate);
}
