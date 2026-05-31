package com.gogidix.hr.employee.domain.repository;

import com.gogidix.hr.employee.domain.model.EmploymentRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Employment Record Repository Interface (Port)
 * Defines the contract for employment record persistence operations
 */
public interface EmploymentRecordRepository {

    EmploymentRecord save(EmploymentRecord record);

    List<EmploymentRecord> saveAll(List<EmploymentRecord> records);

    Optional<EmploymentRecord> findById(String id);

    List<EmploymentRecord> findByEmployeeId(String employeeId);

    List<EmploymentRecord> findByEmployeeIdAndTenantId(String employeeId, String tenantId);

    List<EmploymentRecord> findByTenantIdAndEventType(String tenantId, String eventType);

    List<EmploymentRecord> findByEmployeeIdAndEventType(String employeeId, String eventType);

    List<EmploymentRecord> findByEmployeeIdAndEventDateBetween(String employeeId, LocalDate startDate, LocalDate endDate);

    List<EmploymentRecord> findByTenantIdAndEventDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<EmploymentRecord> findByEmployeeIdOrderByEventDateDesc(String employeeId);

    List<EmploymentRecord> findRecentByEmployeeId(String employeeId, int limit);

    List<EmploymentRecord> findByTenantIdAndApprovedBy(String tenantId, String approvedBy);

    List<EmploymentRecord> findTerminationsByTenantIdAndDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<EmploymentRecord> findHiresByTenantIdAndDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<EmploymentRecord> findPromotionsByTenantIdAndDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<EmploymentRecord> findSalaryChangesByTenantIdAndDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    void deleteById(String id);

    void deleteByEmployeeId(String employeeId);

    void deleteByEmployeeIdAndTenantId(String employeeId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByEmployeeId(String employeeId);

    long countByTenantIdAndEventType(String tenantId, String eventType);

    long countByTenantIdAndEventDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    Optional<EmploymentRecord> findLatestByEmployeeId(String employeeId);
}
