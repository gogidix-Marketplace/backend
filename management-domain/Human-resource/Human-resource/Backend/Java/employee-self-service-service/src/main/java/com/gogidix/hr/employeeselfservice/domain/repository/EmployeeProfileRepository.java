package com.gogidix.hr.employeeselfservice.domain.repository;

import com.gogidix.hr.employeeselfservice.domain.model.EmployeeProfile;

import java.util.List;
import java.util.Optional;

/**
 * Employee Profile Repository Interface (Port)
 * Defines the contract for employee profile persistence operations
 */
public interface EmployeeProfileRepository {

    EmployeeProfile save(EmployeeProfile profile);

    List<EmployeeProfile> saveAll(List<EmployeeProfile> profiles);

    Optional<EmployeeProfile> findById(String id);

    Optional<EmployeeProfile> findByEmployeeIdAndTenantId(String employeeId, String tenantId);

    Optional<EmployeeProfile> findByEmailAndTenantId(String email, String tenantId);

    List<EmployeeProfile> findByTenantId(String tenantId);

    List<EmployeeProfile> findByTenantIdAndProfileCompleted(String tenantId, Boolean completed);

    List<EmployeeProfile> findByTenantIdAndCompletionPercentageLessThan(String tenantId, Integer percentage);

    List<EmployeeProfile> findByTenantIdAndDepartment(String tenantId, String department);

    List<EmployeeProfile> findByTenantIdAndLocation(String tenantId, String location);

    List<EmployeeProfile> findByTenantIdAndEmploymentStatus(String tenantId, String status);

    List<EmployeeProfile> searchByName(String tenantId, String searchTerm);

    List<EmployeeProfile> searchByEmail(String tenantId, String email);

    List<EmployeeProfile> findByTenantIdAndGender(String tenantId, EmployeeProfile.Gender gender);

    List<EmployeeProfile> findByTenantIdAndMaritalStatus(String tenantId, EmployeeProfile.MaritalStatus maritalStatus);

    List<EmployeeProfile> findByTenantIdAndNationality(String tenantId, String nationality);

    List<EmployeeProfile> findByTenantIdAndTagsContaining(String tenantId, String tag);

    boolean existsByEmployeeIdAndTenantId(String employeeId, String tenantId);

    boolean existsByEmailAndTenantId(String email, String tenantId);

    void deleteById(String id);

    void deleteByEmployeeIdAndTenantId(String employeeId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndProfileCompleted(String tenantId, Boolean completed);
}
