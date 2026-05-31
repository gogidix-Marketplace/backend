package com.gogidix.hr.benefitsadministration.domain.port.out;

import com.gogidix.hr.benefitsadministration.domain.model.BenefitEnrollment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Output port for benefit enrollment repository operations
 * Part of hexagonal architecture - secondary port
 */
public interface BenefitEnrollmentRepositoryPort {

    /**
     * Save a benefit enrollment
     * @param enrollment the enrollment to save
     * @return the saved enrollment
     */
    BenefitEnrollment save(BenefitEnrollment enrollment);

    /**
     * Find enrollment by ID
     * @param enrollmentId the enrollment ID
     * @param tenantId the tenant ID
     * @return optional enrollment
     */
    Optional<BenefitEnrollment> findById(String enrollmentId, String tenantId);

    /**
     * Find enrollments by employee ID
     * @param employeeId the employee ID
     * @param tenantId the tenant ID
     * @return list of enrollments
     */
    List<BenefitEnrollment> findByEmployeeId(String employeeId, String tenantId);

    /**
     * Find active enrollments by employee
     * @param employeeId the employee ID
     * @param tenantId the tenant ID
     * @return list of active enrollments
     */
    List<BenefitEnrollment> findActiveByEmployee(String employeeId, String tenantId);

    /**
     * Find enrollments by plan ID
     * @param planId the plan ID
     * @param tenantId the tenant ID
     * @return list of enrollments
     */
    List<BenefitEnrollment> findByPlanId(String planId, String tenantId);

    /**
     * Find enrollments by status
     * @param tenantId the tenant ID
     * @param status the enrollment status
     * @return list of enrollments
     */
    List<BenefitEnrollment> findByStatus(String tenantId, String status);

    /**
     * Find pending enrollments
     * @param tenantId the tenant ID
     * @return list of pending enrollments
     */
    List<BenefitEnrollment> findPending(String tenantId);

    /**
     * Find enrollments effective within date range
     * @param tenantId the tenant ID
     * @param startDate start date
     * @param endDate end date
     * @return list of enrollments
     */
    List<BenefitEnrollment> findEffectiveInPeriod(String tenantId, LocalDate startDate, LocalDate endDate);

    /**
     * Find enrollments expiring before date
     * @param tenantId the tenant ID
     * @param expiryDate the expiry date
     * @return list of enrollments
     */
    List<BenefitEnrollment> findExpiringBefore(String tenantId, LocalDate expiryDate);

    /**
     * Delete enrollment by ID
     * @param enrollmentId the enrollment ID
     * @param tenantId the tenant ID
     */
    void deleteById(String enrollmentId, String tenantId);

    /**
     * Check if employee is enrolled in plan
     * @param employeeId the employee ID
     * @param planId the plan ID
     * @param tenantId the tenant ID
     * @return true if enrolled, false otherwise
     */
    boolean isEmployeeEnrolled(String employeeId, String planId, String tenantId);

    /**
     * Count enrollments by plan
     * @param planId the plan ID
     * @param tenantId the tenant ID
     * @return enrollment count
     */
    long countByPlanId(String planId, String tenantId);
}
