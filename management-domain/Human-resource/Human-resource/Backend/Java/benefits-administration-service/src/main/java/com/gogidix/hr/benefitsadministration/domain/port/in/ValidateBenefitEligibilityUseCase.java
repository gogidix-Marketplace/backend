package com.gogidix.hr.benefitsadministration.domain.port.in;

import com.gogidix.hr.benefitsadministration.application.dto.request.ValidateEligibilityRequest;
import com.gogidix.hr.benefitsadministration.application.dto.response.EligibilityCheckResponse;

/**
 * Use case interface for validating benefit eligibility
 * Part of hexagonal architecture - input port
 */
public interface ValidateBenefitEligibilityUseCase {

    /**
     * Validate if an employee is eligible for a benefit plan
     * @param request the eligibility validation request
     * @return the eligibility check response
     */
    EligibilityCheckResponse validateEligibility(ValidateEligibilityRequest request);

    /**
     * Check if employee is within enrollment window
     * @param employeeId the employee ID
     * @param planId the benefit plan ID
     * @return true if within enrollment window, false otherwise
     */
    boolean isWithinEnrollmentWindow(String employeeId, String planId);

    /**
     * Get eligibility requirements for a benefit plan
     * @param planId the benefit plan ID
     * @return list of eligibility requirements
     */
    java.util.List<String> getEligibilityRequirements(String planId);

    /**
     * Check if dependent coverage is allowed
     * @param employeeId the employee ID
     * @param planId the benefit plan ID
     * @return true if dependents can be covered, false otherwise
     */
    boolean isDependentCoverageAllowed(String employeeId, String planId);
}
