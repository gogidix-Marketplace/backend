package com.gogidix.hr.leavemanagement.domain.port.in;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveStatus;
import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.model.Holiday;
import com.gogidix.hr.leavemanagement.domain.model.LeaveBalance;
import com.gogidix.hr.leavemanagement.domain.model.LeavePolicy;
import com.gogidix.hr.leavemanagement.domain.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Leave Query Port
 * Input port for leave query operations
 */
public interface LeaveQueryPort {

    // Leave Request Queries
    Optional<LeaveRequest> findRequestById(String requestId);

    List<LeaveRequest> findRequestsByEmployee(String employeeId);

    Page<LeaveRequest> findRequestsByEmployee(String employeeId, Pageable pageable);

    List<LeaveRequest> findRequestsByStatus(LeaveStatus status);

    Page<LeaveRequest> findRequestsByStatus(LeaveStatus status, Pageable pageable);

    List<LeaveRequest> findRequestsByManager(String managerId);

    List<LeaveRequest> findPendingApprovalsForManager(String managerId);

    List<LeaveRequest> findRequestsByDateRange(LocalDate startDate, LocalDate endDate);

    List<LeaveRequest> findRequestsByEmployeeAndYear(String employeeId, String year);

    List<LeaveRequest> findOverlappingRequests(String employeeId, LocalDate startDate, LocalDate endDate);

    // Leave Balance Queries
    Optional<LeaveBalance> findBalanceByEmployeeAndType(String employeeId, LeaveType leaveType);

    List<LeaveBalance> findBalancesByEmployee(String employeeId);

    List<LeaveBalance> findBalancesByEmployeeAndYear(String employeeId, String year);

    List<LeaveBalance> findAllBalancesByEmployee(String employeeId);

    List<LeaveBalance> findLowBalances();

    List<LeaveBalance> findExpiringCarryForwardBalances();

    // Leave Policy Queries
    List<LeavePolicy> findAllPolicies();

    List<LeavePolicy> findActivePolicies();

    Optional<LeavePolicy> findPolicyByType(LeaveType leaveType);

    List<LeavePolicy> findPoliciesByCountry(String countryCode);

    // Holiday Queries
    List<Holiday> findHolidaysByYear(int year);

    List<Holiday> findUpcomingHolidays(int limit);

    List<Holiday> findHolidaysBetween(LocalDate startDate, LocalDate endDate);

    boolean isHoliday(LocalDate date);
}
