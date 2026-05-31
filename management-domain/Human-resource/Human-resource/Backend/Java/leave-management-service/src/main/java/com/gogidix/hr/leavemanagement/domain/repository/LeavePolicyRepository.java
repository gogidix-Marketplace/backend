package com.gogidix.hr.leavemanagement.domain.repository;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.model.LeavePolicy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Leave Policy Repository Interface
 */
public interface LeavePolicyRepository {

    LeavePolicy save(LeavePolicy policy);

    List<LeavePolicy> saveAll(List<LeavePolicy> policies);

    Optional<LeavePolicy> findById(String id);

    Optional<LeavePolicy> findByPolicyIdAndTenantId(String policyId, String tenantId);

    Optional<LeavePolicy> findByPolicyCodeAndTenantId(String policyCode, String tenantId);

    List<LeavePolicy> findByTenantId(String tenantId);

    List<LeavePolicy> findByTenantIdAndIsActive(String tenantId, Boolean isActive);

    List<LeavePolicy> findByTenantIdAndLeaveType(String tenantId, LeaveType leaveType);

    List<LeavePolicy> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<LeavePolicy> findActivePoliciesForDate(String tenantId, LocalDate date);

    List<LeavePolicy> findPoliciesForEmployee(String tenantId, String employeeId, LocalDate date);

    boolean existsByPolicyCodeAndTenantId(String policyCode, String tenantId);

    void deleteById(String id);

    void deleteByPolicyIdAndTenantId(String policyId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);
}