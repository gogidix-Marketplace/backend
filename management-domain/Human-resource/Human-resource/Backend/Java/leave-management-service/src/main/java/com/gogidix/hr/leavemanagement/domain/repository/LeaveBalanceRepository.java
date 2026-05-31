package com.gogidix.hr.leavemanagement.domain.repository;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.model.LeaveBalance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Leave Balance Repository Interface
 */
public interface LeaveBalanceRepository {

    LeaveBalance save(LeaveBalance balance);

    List<LeaveBalance> saveAll(List<LeaveBalance> balances);

    Optional<LeaveBalance> findById(String id);

    Optional<LeaveBalance> findByBalanceIdAndTenantId(String balanceId, String tenantId);

    Optional<LeaveBalance> findByEmployeeIdAndLeaveTypeAndYear(String employeeId, LeaveType leaveType, String year);

    List<LeaveBalance> findByTenantId(String tenantId);

    List<LeaveBalance> findByTenantIdAndEmployeeId(String tenantId, String employeeId);

    List<LeaveBalance> findByTenantIdAndEmployeeIdAndYear(String tenantId, String employeeId, String year);

    List<LeaveBalance> findByTenantIdAndYear(String tenantId, String year);

    List<LeaveBalance> findByTenantIdAndLeaveType(String tenantId, LeaveType leaveType);

    List<LeaveBalance> findByTenantIdAndPeriod(String tenantId, YearMonth period);

    List<LeaveBalance> findLowBalances(String tenantId);

    List<LeaveBalance> findExpiringCarryForward(String tenantId);

    List<LeaveBalance> findNegativeBalances(String tenantId);

    Page<LeaveBalance> searchBalances(String tenantId, String searchTerm, Pageable pageable);

    long countByTenantId(String tenantId);

    long countByTenantIdAndYear(String tenantId, String year);

    void deleteById(String id);

    void deleteByBalanceIdAndTenantId(String balanceId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    List<LeaveBalance> findByEmployeeIdAndLeaveType(String employeeId, LeaveType leaveType);

    List<LeaveBalance> findAllByEmployeeId(String employeeId);
}