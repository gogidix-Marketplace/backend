package com.gogidix.hr.benefitsadministration.domain.port.out;

import java.util.Optional;

/**
 * Output port for employee verification
 * Part of hexagonal architecture - secondary port
 * Used to validate employee information for benefit eligibility
 */
public interface EmployeeVerificationPort {

    /**
     * Employee information for verification
     */
    record EmployeeInfo(
        String employeeId,
        String tenantId,
        String firstName,
        String lastName,
        String email,
        String department,
        String jobTitle,
        String employmentStatus,
        java.time.LocalDate hireDate,
        java.time.LocalDate terminationDate,
        String countryCode,
        String workLocation,
        java.math.BigDecimal salary,
        String employmentType,
        Integer hoursPerWeek,
        boolean isFullTime,
        boolean isEligibleForBenefits
    ) {}

    /**
     * Get employee information for verification
     * @param employeeId the employee ID
     * @param tenantId the tenant ID
     * @return optional employee information
     */
    Optional<EmployeeInfo> getEmployeeInfo(String employeeId, String tenantId);

    /**
     * Verify if employee is eligible for benefits
     * @param employeeId the employee ID
     * @param tenantId the tenant ID
     * @return true if eligible, false otherwise
     */
    boolean isEligibleForBenefits(String employeeId, String tenantId);

    /**
     * Get employee's tenure in days
     * @param employeeId the employee ID
     * @param tenantId the tenant ID
     * @return tenure in days
     */
    long getTenureDays(String employeeId, String tenantId);

    /**
     * Check if employee is active
     * @param employeeId the employee ID
     * @param tenantId the tenant ID
     * @return true if active, false otherwise
     */
    boolean isEmployeeActive(String employeeId, String tenantId);
}
