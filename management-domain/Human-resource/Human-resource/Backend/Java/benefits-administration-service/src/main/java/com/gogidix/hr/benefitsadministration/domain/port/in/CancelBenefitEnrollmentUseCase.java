package com.gogidix.hr.benefitsadministration.domain.port.in;

import com.gogidix.hr.benefitsadministration.application.dto.response.BenefitEnrollmentResponse;

/**
 * Use case interface for canceling benefit enrollments
 * Part of hexagonal architecture - input port
 */
public interface CancelBenefitEnrollmentUseCase {

    /**
     * Cancel an employee's benefit enrollment
     * @param enrollmentId the enrollment ID to cancel
     * @param cancellationReason the reason for cancellation
     * @param effectiveDate the effective date of cancellation
     * @return the canceled enrollment response
     */
    BenefitEnrollmentResponse cancelEnrollment(String enrollmentId, String cancellationReason, java.time.LocalDate effectiveDate);

    /**
     * Terminate enrollment due to employee termination
     * @param employeeId the employee ID
     * @param terminationDate the termination date
     * @return list of terminated enrollments
     */
    java.util.List<BenefitEnrollmentResponse> terminateEmployeeEnrollments(String employeeId, java.time.LocalDate terminationDate);
}
