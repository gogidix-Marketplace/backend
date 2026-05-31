package com.gogidix.hr.benefitsadministration.domain.port.in;

import com.gogidix.hr.benefitsadministration.application.dto.request.SearchBenefitPlansRequest;
import com.gogidix.hr.benefitsadministration.application.dto.response.BenefitPlanResponse;
import com.gogidix.hr.benefitsadministration.application.dto.response.BenefitPlanDetailResponse;
import com.gogidix.hr.benefitsadministration.application.dto.response.EmployeeBenefitSummaryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Use case interface for querying benefit plans
 * Part of hexagonal architecture - input port
 */
public interface QueryBenefitPlansUseCase {

    /**
     * Get benefit plan by ID
     * @param planId the plan ID
     * @return the benefit plan response
     */
    Optional<BenefitPlanResponse> getBenefitPlanById(String planId);

    /**
     * Get detailed benefit plan information
     * @param planId the plan ID
     * @return detailed benefit plan response
     */
    Optional<BenefitPlanDetailResponse> getBenefitPlanDetail(String planId);

    /**
     * Get all active benefit plans for a tenant
     * @param tenantId the tenant ID
     * @return list of active benefit plans
     */
    List<BenefitPlanResponse> getActiveBenefitPlans(String tenantId);

    /**
     * Get benefit plans by country
     * @param tenantId the tenant ID
     * @param countryCode the country code
     * @return list of benefit plans for the country
     */
    List<BenefitPlanResponse> getBenefitPlansByCountry(String tenantId, String countryCode);

    /**
     * Get benefit plans by type
     * @param tenantId the tenant ID
     * @param benefitType the benefit type
     * @return list of benefit plans of the specified type
     */
    List<BenefitPlanResponse> getBenefitPlansByType(String tenantId, String benefitType);

    /**
     * Search benefit plans with filters
     * @param request the search request
     * @return list of matching benefit plans
     */
    List<BenefitPlanResponse> searchBenefitPlans(SearchBenefitPlansRequest request);

    /**
     * Get benefit plans available for enrollment
     * @param tenantId the tenant ID
     * @param employeeId the employee ID
     * @return list of available benefit plans
     */
    List<BenefitPlanResponse> getAvailablePlansForEnrollment(String tenantId, String employeeId);

    /**
     * Get employee's benefit summary
     * @param employeeId the employee ID
     * @return employee benefit summary
     */
    Optional<EmployeeBenefitSummaryResponse> getEmployeeBenefitSummary(String employeeId);

    /**
     * Get benefit plans expiring soon
     * @param tenantId the tenant ID
     * @param daysBeforeExpiry number of days before expiry
     * @return list of benefit plans expiring soon
     */
    List<BenefitPlanResponse> getPlansExpiringSoon(String tenantId, int daysBeforeExpiry);

    /**
     * Get benefit plans effective on a specific date
     * @param tenantId the tenant ID
     * @param effectiveDate the effective date
     * @return list of benefit plans effective on the date
     */
    List<BenefitPlanResponse> getPlansEffectiveOn(String tenantId, LocalDate effectiveDate);
}
