package com.gogidix.hr.leavemanagement.domain.repository;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveStatus;
import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Leave Request Repository Interface
 */
public interface LeaveRequestRepository {

    LeaveRequest save(LeaveRequest request);

    List<LeaveRequest> saveAll(List<LeaveRequest> requests);

    Optional<LeaveRequest> findById(String id);

    Optional<LeaveRequest> findByRequestIdAndTenantId(String requestId, String tenantId);

    List<LeaveRequest> findByTenantId(String tenantId);

    Page<LeaveRequest> findByTenantId(String tenantId, Pageable pageable);

    List<LeaveRequest> findByTenantIdAndEmployeeId(String tenantId, String employeeId);

    List<LeaveRequest> findByTenantIdAndEmployeeIdOrderBySubmissionDateDesc(String tenantId, String employeeId);

    List<LeaveRequest> findByTenantIdAndStatus(String tenantId, LeaveStatus status);

    Page<LeaveRequest> findByTenantIdAndStatus(String tenantId, LeaveStatus status, Pageable pageable);

    List<LeaveRequest> findByTenantIdAndLeaveType(String tenantId, LeaveType leaveType);

    List<LeaveRequest> findByTenantIdAndEmployeeIdAndStatus(String tenantId, String employeeId, LeaveStatus status);

    List<LeaveRequest> findByTenantIdAndManagerId(String tenantId, String managerId);

    List<LeaveRequest> findPendingApprovalForManager(String tenantId, String managerId);

    List<LeaveRequest> findByTenantIdAndStartDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<LeaveRequest> findByTenantIdAndEndDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<LeaveRequest> findByTenantIdAndEmployeeIdAndLeaveTypeAndYear(String tenantId, String employeeId, LeaveType leaveType, String year);

    List<LeaveRequest> findOverlappingRequests(String tenantId, String employeeId, LocalDate startDate, LocalDate endDate);

    List<LeaveRequest> findPendingCancellationRequests(String tenantId);

    Page<LeaveRequest> searchRequests(String tenantId, String searchTerm, Pageable pageable);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, LeaveStatus status);

    long countByTenantIdAndEmployeeIdAndStatus(String tenantId, String employeeId, LeaveStatus status);

    boolean existsOverlappingRequest(String tenantId, String employeeId, LocalDate startDate, LocalDate endDate, String excludeRequestId);

    void deleteById(String id);

    void deleteByRequestIdAndTenantId(String requestId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    List<LeaveRequest> findActiveLeaveRequestsForEmployee(String tenantId, String employeeId, LocalDate date);

    List<LeaveRequest> findByTenantIdAndDepartment(String tenantId, String department);
}