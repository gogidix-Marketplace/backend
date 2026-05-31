package com.gogidix.hr.employee.domain.repository;

import com.gogidix.hr.employee.domain.model.EmployeeContact;

import java.util.List;
import java.util.Optional;

/**
 * Employee Contact Repository Interface (Port)
 * Defines the contract for contact persistence operations
 */
public interface EmployeeContactRepository {

    EmployeeContact save(EmployeeContact contact);

    List<EmployeeContact> saveAll(List<EmployeeContact> contacts);

    Optional<EmployeeContact> findById(String id);

    List<EmployeeContact> findByEmployeeId(String employeeId);

    List<EmployeeContact> findByEmployeeIdAndTenantId(String employeeId, String tenantId);

    List<EmployeeContact> findByTenantIdAndType(String tenantId, EmployeeContact.ContactType type);

    Optional<EmployeeContact> findPrimaryByEmployeeIdAndTenantId(String employeeId, String tenantId);

    List<EmployeeContact> findEmergencyContactsByEmployeeIdAndTenantId(String employeeId, String tenantId);

    List<EmployeeContact> findByTenantIdAndNameContaining(String tenantId, String name);

    void deleteById(String id);

    void deleteByEmployeeId(String employeeId);

    void deleteByEmployeeIdAndTenantId(String employeeId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByEmployeeId(String employeeId);

    long countByTenantIdAndType(String tenantId, EmployeeContact.ContactType type);

    List<EmployeeContact> findByEmployeeIdOrderByPriority(String employeeId);
}
