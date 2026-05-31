package com.gogidix.hr.leavemanagement.application.service;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveStatus;
import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.model.LeaveRequest;
import com.gogidix.hr.leavemanagement.domain.repository.LeaveRequestRepository;
import com.gogidix.hr.leavemanagement.shared.exception.LeaveNotFoundException;
import com.gogidix.hr.leavemanagement.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Leave Request Query Service
 * Handles all read operations for leave requests
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveRequestQueryService {

    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveRequest getById(String requestId) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.findByRequestIdAndTenantId(requestId, tenantId)
                .orElseThrow(() -> new LeaveNotFoundException("LeaveRequest", requestId));
    }

    public List<LeaveRequest> getByEmployeeId(String employeeId) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.findByTenantIdAndEmployeeId(tenantId, employeeId);
    }

    public Page<LeaveRequest> getByEmployeeId(String employeeId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<LeaveRequest> requests = leaveRequestRepository.findByTenantIdAndEmployeeIdOrderBySubmissionDateDesc(tenantId, employeeId);
        return paginateList(requests, page, size);
    }

    public List<LeaveRequest> getByStatus(LeaveStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.findByTenantIdAndStatus(tenantId, status);
    }

    public Page<LeaveRequest> getByStatus(LeaveStatus status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.findByTenantIdAndStatus(tenantId, status, PageRequest.of(page, size));
    }

    public List<LeaveRequest> getByManagerId(String managerId) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.findByTenantIdAndManagerId(tenantId, managerId);
    }

    public List<LeaveRequest> getPendingApprovalsForManager(String managerId) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.findPendingApprovalForManager(tenantId, managerId);
    }

    public List<LeaveRequest> getByDateRange(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.findByTenantIdAndStartDateBetween(tenantId, startDate, endDate);
    }

    public List<LeaveRequest> getByEmployeeAndYear(String employeeId, String year) {
        String tenantId = RequestContextHolder.getTenantId();
        List<LeaveRequest> allRequests = leaveRequestRepository.findByTenantIdAndEmployeeId(tenantId, employeeId);
        return allRequests.stream()
                .filter(r -> year.equals(r.getYear()))
                .toList();
    }

    public List<LeaveRequest> getOverlappingRequests(String employeeId, LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.findOverlappingRequests(tenantId, employeeId, startDate, endDate);
    }

    public List<LeaveRequest> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.findByTenantId(tenantId);
    }

    public Page<LeaveRequest> getAllForTenant(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.findByTenantId(tenantId, PageRequest.of(page, size));
    }

    public List<LeaveRequest> getByLeaveType(LeaveType leaveType) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.findByTenantIdAndLeaveType(tenantId, leaveType);
    }

    public List<LeaveRequest> getByDepartment(String department) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.findByTenantIdAndDepartment(tenantId, department);
    }

    public List<LeaveRequest> getPendingCancellationRequests() {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.findPendingCancellationRequests(tenantId);
    }

    public Page<LeaveRequest> search(String searchTerm, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.searchRequests(tenantId, searchTerm, PageRequest.of(page, size));
    }

    public long countByStatus(LeaveStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.countByTenantIdAndStatus(tenantId, status);
    }

    public long countByEmployeeAndStatus(String employeeId, LeaveStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveRequestRepository.countByTenantIdAndEmployeeIdAndStatus(tenantId, employeeId, status);
    }

    private <T> Page<T> paginateList(List<T> list, int page, int size) {
        int start = (int) PageRequest.of(page, size).getOffset();
        int end = Math.min(start + size, list.size());
        List<T> paginatedList = start < list.size() ? list.subList(start, end) : List.of();
        return new PageImpl<>(paginatedList, PageRequest.of(page, size), list.size());
    }
}
