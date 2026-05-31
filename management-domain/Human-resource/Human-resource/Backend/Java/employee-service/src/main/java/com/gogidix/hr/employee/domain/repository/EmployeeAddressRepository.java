package com.gogidix.hr.employee.domain.repository;

import com.gogidix.hr.employee.domain.model.EmployeeAddress;

import java.util.List;
import java.util.Optional;

/**
 * Employee Address Repository Interface (Port)
 * Defines the contract for address persistence operations
 */
public interface EmployeeAddressRepository {

    EmployeeAddress save(EmployeeAddress address);

    List<EmployeeAddress> saveAll(List<EmployeeAddress> addresses);

    Optional<EmployeeAddress> findById(String id);

    List<EmployeeAddress> findByEmployeeId(String employeeId);

    List<EmployeeAddress> findByEmployeeIdAndTenantId(String employeeId, String tenantId);

    List<EmployeeAddress> findByTenantIdAndType(String tenantId, EmployeeAddress.AddressType type);

    Optional<EmployeeAddress> findPrimaryByEmployeeIdAndTenantId(String employeeId, String tenantId);

    List<EmployeeAddress> findEffectiveByEmployeeIdAndTenantId(String employeeId, String tenantId);

    List<EmployeeAddress> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<EmployeeAddress> findByTenantIdAndCity(String tenantId, String city);

    List<EmployeeAddress> findByTenantIdAndState(String tenantId, String state);

    void deleteById(String id);

    void deleteByEmployeeId(String employeeId);

    void deleteByEmployeeIdAndTenantId(String employeeId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByEmployeeId(String employeeId);

    long countByTenantIdAndType(String tenantId, EmployeeAddress.AddressType type);
}
