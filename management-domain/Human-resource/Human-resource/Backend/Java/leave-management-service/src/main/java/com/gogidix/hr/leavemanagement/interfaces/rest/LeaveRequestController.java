package com.gogidix.hr.leavemanagement.interfaces.rest;

import com.gogidix.hr.leavemanagement.application.service.LeaveRequestCommandService;
import com.gogidix.hr.leavemanagement.application.service.LeaveRequestQueryService;
import com.gogidix.hr.leavemanagement.domain.enums.LeaveStatus;
import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.enums.RequestType;
import com.gogidix.hr.leavemanagement.domain.model.LeaveRequest;
import com.gogidix.hr.leavemanagement.domain.port.in.LeaveCommand;
import com.gogidix.hr.leavemanagement.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/leave-requests")
@RequiredArgsConstructor
@Tag(name = "Leave Requests", description = "Leave request management endpoints")
public class LeaveRequestController {

    private final LeaveRequestCommandService commandService;
    private final LeaveRequestQueryService queryService;

    @PostMapping
    @Operation(summary = "Create a new leave request")
    public ResponseEntity<LeaveRequest> createRequest(@Valid @RequestBody CreateRequestDto request) {
        LeaveCommand.CreateLeaveRequestCommand command = LeaveCommand.CreateLeaveRequestCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .employeeId(RequestContextHolder.getUserId())
                .employeeName(request.employeeName)
                .employeeCode(request.employeeCode)
                .department(request.department)
                .position(request.position)
                .managerId(request.managerId)
                .leaveType(request.leaveType)
                .requestType(request.requestType)
                .startDate(request.startDate)
                .endDate(request.endDate)
                .requestedDays(request.requestedDays)
                .hoursRequested(request.hoursRequested)
                .reason(request.reason)
                .contactDuringLeave(request.contactDuringLeave)
                .emergencyContact(request.emergencyContact)
                .isHalfDay(request.isHalfDay)
                .halfDayType(request.halfDayType)
                .reliefStaffId(request.reliefStaffId)
                .reliefStaffName(request.reliefStaffName)
                .handoverNotes(request.handoverNotes)
                .createdBy(RequestContextHolder.getUserId())
                .build();
        LeaveRequest leaveRequest = commandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveRequest);
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "Get leave request by ID")
    public ResponseEntity<LeaveRequest> getRequest(@PathVariable String requestId) {
        LeaveRequest request = queryService.getById(requestId);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get leave requests for employee")
    public ResponseEntity<Page<LeaveRequest>> getByEmployee(
            @PathVariable String employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<LeaveRequest> requests = queryService.getByEmployeeId(employeeId, page, size);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get leave requests by status")
    public ResponseEntity<Page<LeaveRequest>> getByStatus(
            @PathVariable LeaveStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<LeaveRequest> requests = queryService.getByStatus(status, page, size);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/manager/pending")
    @Operation(summary = "Get pending approvals for manager")
    public ResponseEntity<List<LeaveRequest>> getPendingApprovals() {
        String managerId = RequestContextHolder.getUserId();
        List<LeaveRequest> requests = queryService.getPendingApprovalsForManager(managerId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{requestId}/approve")
    @Operation(summary = "Approve leave request")
    public ResponseEntity<Void> approve(@PathVariable String requestId, @RequestBody ApprovalDto request) {
        LeaveCommand.ApproveLeaveRequestCommand command = LeaveCommand.ApproveLeaveRequestCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .requestId(requestId)
                .approverId(RequestContextHolder.getUserId())
                .approverName(request.approverName)
                .comments(request.comments)
                .build();
        commandService.approve(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{requestId}/reject")
    @Operation(summary = "Reject leave request")
    public ResponseEntity<Void> reject(@PathVariable String requestId, @RequestBody RejectionDto request) {
        LeaveCommand.RejectLeaveRequestCommand command = LeaveCommand.RejectLeaveRequestCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .requestId(requestId)
                .approverId(RequestContextHolder.getUserId())
                .reason(request.reason)
                .build();
        commandService.reject(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{requestId}/cancel")
    @Operation(summary = "Cancel leave request")
    public ResponseEntity<Void> cancel(@PathVariable String requestId, @RequestBody CancellationDto request) {
        LeaveCommand.CancelLeaveRequestCommand command = LeaveCommand.CancelLeaveRequestCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .requestId(requestId)
                .employeeId(RequestContextHolder.getUserId())
                .reason(request.reason)
                .build();
        commandService.cancel(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/overlapping")
    @Operation(summary = "Check for overlapping requests")
    public ResponseEntity<List<LeaveRequest>> checkOverlapping(
            @RequestParam String employeeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<LeaveRequest> requests = queryService.getOverlappingRequests(employeeId, startDate, endDate);
        return ResponseEntity.ok(requests);
    }

    public static class CreateRequestDto {
        public String employeeName;
        public String employeeCode;
        public String department;
        public String position;
        public String managerId;
        public LeaveType leaveType;
        public RequestType requestType;
        public LocalDate startDate;
        public LocalDate endDate;
        public Double requestedDays;
        public Double hoursRequested;
        public String reason;
        public String contactDuringLeave;
        public String emergencyContact;
        public Boolean isHalfDay;
        public String halfDayType;
        public String reliefStaffId;
        public String reliefStaffName;
        public String handoverNotes;
    }

    public static class ApprovalDto {
        public String approverName;
        public String comments;
    }

    public static class RejectionDto {
        public String reason;
    }

    public static class CancellationDto {
        public String reason;
    }
}
