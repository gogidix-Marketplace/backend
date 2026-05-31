package com.gogidix.hr.benefitsadministration.domain.port.in;

import com.gogidix.hr.benefitsadministration.application.dto.request.CreateBenefitEnrollmentRequest;
import com.gogidix.hr.benefitsadministration.application.dto.response.BenefitEnrollmentResponse;
import com.gogidix.hr.benefitsadministration.application.dto.response.EnrollmentConfirmationResponse;

/**
 * Use case interface for employee benefit enrollment
 * Part of hexagonal architecture - input port
 */
public interface EnrollEmployeeInBenefitUseCase {

    /**
     * Enroll an employee in a benefit plan
     * @param request the enrollment request
     * @return the enrollment confirmation response
     */
    EnrollmentConfirmationResponse enrollEmployee(CreateBenefitEnrollmentRequest request);

    /**
     * Bulk enroll multiple employees in a benefit plan
     * @param requests list of enrollment requests
     * @return list of enrollment confirmation responses
     */
    java.util.List<EnrollmentConfirmationResponse> bulkEnrollEmployees(java.util.List<CreateBenefitEnrollmentRequest> requests);

    /**
     * Re-enroll an employee in a benefit plan (for renewal)
     * @param enrollmentId the existing enrollment ID
     * @return the enrollment confirmation response
     */
    EnrollmentConfirmationResponse reEnrollEmployee(String enrollmentId);
}
