package com.gogidix.hr.employee.domain.repository;

import com.gogidix.hr.employee.domain.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Employee Repository Interface (Port)
 * Defines the contract for employee persistence operations
 */
public interface EmployeeRepository {

    Employee save(Employee employee);

    List<Employee> saveAll(List<Employee> employees);

    Optional<Employee> findById(String id);

    Optional<Employee> findByEmployeeNumberAndTenantId(String employeeNumber, String tenantId);

    Optional<Employee> findByEmailAndTenantId(String email, String tenantId);

    // Convenience methods for single-tenant scenarios
    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByEmployeeNumber(String employeeNumber);

    List<Employee> findByTenantId(String tenantId);

    Page<Employee> findByTenantId(String tenantId, Pageable pageable);

    Page<Employee> searchByTenantIdAndSearchTerm(String tenantId, String searchTerm, Pageable pageable);

    List<Employee> findByTenantIdAndStatus(String tenantId, Employee.EmployeeStatus status);

    List<Employee> findByTenantIdAndDepartment(String tenantId, String department);

    List<Employee> findByTenantIdAndDepartmentId(String tenantId, String departmentId);

    List<Employee> findByTenantIdAndManagerId(String tenantId, String managerId);

    List<Employee> findByTenantIdAndLevel(String tenantId, Employee.EmployeeLevel level);

    List<Employee> findByTenantIdAndEmploymentType(String tenantId, Employee.EmploymentType employmentType);

    List<Employee> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<Employee> findByTenantIdAndLocation(String tenantId, String location);

    List<Employee> findByTenantIdAndCostCenter(String tenantId, String costCenter);

    List<Employee> findByTenantIdAndPositionId(String tenantId, String positionId);

    List<Employee> findByTenantIdAndHireDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Employee> findByTenantIdAndTerminationDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Employee> findActiveEmployees(String tenantId);

    List<Employee> findInactiveEmployees(String tenantId);

    List<Employee> findEmployeesOnLeave(String tenantId);

    List<Employee> findEmployeesOnProbation(String tenantId);

    List<Employee> findPendingOnboarding(String tenantId);

    List<Employee> findPendingTermination(String tenantId);

    List<Employee> searchByName(String tenantId, String firstName, String lastName);

    List<Employee> searchByKeyword(String tenantId, String keyword);

    List<Employee> findBySkillsContaining(String tenantId, String skill);

    List<Employee> findByCertificationsContaining(String tenantId, String certification);

    List<Employee> findByLanguagesContaining(String tenantId, String language);

    List<Employee> findUpcomingRetirements(String tenantId, LocalDate withinDate);

    List<Employee> findExpiringWorkPermits(String tenantId, LocalDate beforeDate);

    boolean existsByEmployeeNumberAndTenantId(String employeeNumber, String tenantId);

    boolean existsByEmailAndTenantId(String email, String tenantId);

    void deleteById(String id);

    void deleteByEmployeeNumberAndTenantId(String employeeNumber, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Employee.EmployeeStatus status);

    long countByTenantIdAndDepartment(String tenantId, String department);

    long countByTenantIdAndDepartmentId(String tenantId, String departmentId);

    long countByTenantIdAndLevel(String tenantId, Employee.EmployeeLevel level);

    long countByTenantIdAndEmploymentType(String tenantId, Employee.EmploymentType employmentType);

    long countByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<Employee> findDirectReports(String tenantId, String managerId);

    List<Employee> findByTenantIdAndActiveTrue(String tenantId);

    List<Employee> findByManagerIdIsNull(String tenantId);
}
