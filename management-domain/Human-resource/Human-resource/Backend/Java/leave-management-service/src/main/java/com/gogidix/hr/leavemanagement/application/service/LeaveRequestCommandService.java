package com.gogidix.hr.leavemanagement.application.service;

import com.gogidix.hr.leavemanagement.domain.event.LeaveRequestApprovedEvent;
import com.gogidix.hr.leavemanagement.domain.event.LeaveRequestCancelledEvent;
import com.gogidix.hr.leavemanagement.domain.event.LeaveRequestCreatedEvent;
import com.gogidix.hr.leavemanagement.domain.event.LeaveRequestRejectedEvent;
import com.gogidix.hr.leavemanagement.domain.model.LeaveBalance;
import com.gogidix.hr.leavemanagement.domain.model.LeavePolicy;
import com.gogidix.hr.leavemanagement.domain.model.LeaveRequest;
import com.gogidix.hr.leavemanagement.domain.port.out.EventPublisher;
import com.gogidix.hr.leavemanagement.domain.port.in.LeaveCommand;
import com.gogidix.hr.leavemanagement.domain.repository.LeaveBalanceRepository;
import com.gogidix.hr.leavemanagement.domain.repository.LeavePolicyRepository;
import com.gogidix.hr.leavemanagement.domain.repository.LeaveRequestRepository;
import com.gogidix.hr.leavemanagement.shared.exception.LeaveNotFoundException;
import com.gogidix.hr.leavemanagement.shared.exception.LeaveValidationException;
import com.gogidix.hr.leavemanagement.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Leave Request Command Service
 * Handles all write operations for leave requests
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveRequestCommandService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeavePolicyRepository leavePolicyRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public LeaveRequest create(LeaveCommand.CreateLeaveRequestCommand command) {
        log.info("Creating leave request for employee: {} from {} to {}",
                command.getEmployeeId(), command.getStartDate(), command.getEndDate());

        // Validate dates
        if (command.getStartDate().isAfter(command.getEndDate())) {
            throw new LeaveValidationException("Start date must be before or equal to end date");
        }

        // Check for overlapping requests
        List<LeaveRequest> overlapping = leaveRequestRepository.findOverlappingRequests(
                command.getTenantId(), command.getEmployeeId(), command.getStartDate(), command.getEndDate());
        if (!overlapping.isEmpty()) {
            throw new LeaveValidationException("Overlapping leave request exists");
        }

        // Get or create leave balance
        String year = String.valueOf(command.getStartDate().getYear());
        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeAndYear(command.getEmployeeId(), command.getLeaveType(), year)
                .orElseGet(() -> createInitialBalance(command, year));

        // Get policy for validation
        LeavePolicy policy = leavePolicyRepository.findByTenantIdAndLeaveType(command.getTenantId(), command.getLeaveType())
                .stream()
                .filter(LeavePolicy::getIsActive)
                .findFirst()
                .orElse(null);

        // Calculate duration
        Double duration = calculateDuration(command.getStartDate(), command.getEndDate(), command.getRequestType(), command.getHoursRequested());

        // Validate against policy and balance
        if (policy != null) {
            int tenureDays = calculateTenureDays(command.getEmployeeId());
            List<String> errors = policy.validateRequest(command.getStartDate(), command.getEndDate(),
                    duration, tenureDays, balance.getAvailable());
            if (!errors.isEmpty()) {
                throw new LeaveValidationException("Validation failed: " + String.join(", ", errors));
            }
        }

        // Check balance
        if (!balance.hasSufficientBalance(duration) && !isNegativeBalanceAllowed(policy)) {
            throw new LeaveValidationException("Insufficient leave balance. Available: " + balance.getAvailable());
        }

        String requestId = generateRequestId();

        LeaveRequest request = new LeaveRequest();
        request.setId(UUID.randomUUID().toString());
        request.setTenantId(command.getTenantId());
        request.setCountryCode(command.getCountryCode());
        request.setRequestId(requestId);
        request.setEmployeeId(command.getEmployeeId());
        request.setEmployeeName(command.getEmployeeName());
        request.setEmployeeCode(command.getEmployeeCode());
        request.setDepartment(command.getDepartment());
        request.setPosition(command.getPosition());
        request.setManagerId(command.getManagerId());
        request.setLeaveType(command.getLeaveType());
        request.setRequestType(command.getRequestType());
        request.setStatus(com.gogidix.hr.leavemanagement.domain.enums.LeaveStatus.PENDING);
        request.setStartDate(command.getStartDate());
        request.setEndDate(command.getEndDate());
        request.setRequestedDays(duration);
        request.setHoursRequested(command.getHoursRequested());
        request.setReason(command.getReason());
        request.setContactDuringLeave(command.getContactDuringLeave());
        request.setEmergencyContact(command.getEmergencyContact());
        request.setIsHalfDay(command.getIsHalfDay());
        request.setHalfDayType(command.getHalfDayType());
        request.setSubmissionDate(LocalDate.now());
        request.setRequiresApproval(policy != null ? policy.getRequiresApproval() : true);
        request.setIsPaid(policy != null ? policy.getPaidLeave() : true);
        request.setCarryForwardAllowed(policy != null ? policy.getCarryForwardAllowed() : false);
        request.setYear(year);
        request.setReliefStaffId(command.getReliefStaffId());
        request.setReliefStaffName(command.getReliefStaffName());
        request.setHandoverNotes(command.getHandoverNotes());
        request.setCreatedByEmployee(command.getCreatedBy());
        request.setDocumentsVerified(false);
        request.setUpdatedBy(command.getCreatedBy());

        // Add attachments if provided
        if (command.getAttachments() != null) {
            command.getAttachments().forEach(att -> request.getAttachments().add(
                    LeaveRequest.Attachment.builder()
                            .attachmentId(UUID.randomUUID().toString())
                            .fileName(att.getFileName())
                            .fileUrl(att.getFileUrl())
                            .fileType(att.getFileType())
                            .fileSize(att.getFileSize())
                            .uploadedAt(java.time.LocalDateTime.now())
                            .uploadedBy(command.getCreatedBy())
                            .build()
            ));
        }

        LeaveRequest savedRequest = leaveRequestRepository.save(request);

        // Add pending to balance
        balance.addPending(duration);
        leaveBalanceRepository.save(balance);

        // Publish event
        eventPublisher.publish("leave-request-created", new LeaveRequestCreatedEvent(
                savedRequest.getTenantId(),
                savedRequest.getRequestId(),
                savedRequest.getEmployeeId(),
                savedRequest.getEmployeeName(),
                savedRequest.getLeaveType().name(),
                savedRequest.getStartDate(),
                savedRequest.getEndDate(),
                savedRequest.getRequestedDays(),
                savedRequest.getSubmissionDate().toString()
        ));

        log.info("Created leave request: {}", requestId);
        return savedRequest;
    }

    @Transactional
    public LeaveRequest update(LeaveCommand.UpdateLeaveRequestCommand command) {
        log.info("Updating leave request: {}", command.getRequestId());

        LeaveRequest request = leaveRequestRepository.findByRequestIdAndTenantId(command.getRequestId(), command.getTenantId())
                .orElseThrow(() -> new LeaveNotFoundException("LeaveRequest", command.getRequestId()));

        if (request.getStatus() != com.gogidix.hr.leavemanagement.domain.enums.LeaveStatus.PENDING) {
            throw new LeaveValidationException("Cannot modify request in status: " + request.getStatus());
        }

        if (command.getStartDate() != null) request.setStartDate(command.getStartDate());
        if (command.getEndDate() != null) request.setEndDate(command.getEndDate());
        if (command.getRequestedDays() != null) request.setRequestedDays(command.getRequestedDays());
        if (command.getReason() != null) request.setReason(command.getReason());
        if (command.getContactDuringLeave() != null) request.setContactDuringLeave(command.getContactDuringLeave());
        if (command.getEmergencyContact() != null) request.setEmergencyContact(command.getEmergencyContact());
        request.setUpdatedBy(command.getUpdatedBy());

        LeaveRequest updatedRequest = leaveRequestRepository.save(request);
        log.info("Updated leave request: {}", command.getRequestId());
        return updatedRequest;
    }

    @Transactional
    public void approve(LeaveCommand.ApproveLeaveRequestCommand command) {
        log.info("Approving leave request: {} by {}", command.getRequestId(), command.getApproverId());

        LeaveRequest request = leaveRequestRepository.findByRequestIdAndTenantId(command.getRequestId(), command.getTenantId())
                .orElseThrow(() -> new LeaveNotFoundException("LeaveRequest", command.getRequestId()));

        request.approve(command.getApproverId(), command.getApproverName());
        LeaveRequest savedRequest = leaveRequestRepository.save(request);

        // Update balance
        String year = String.valueOf(savedRequest.getStartDate().getYear());
        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeAndYear(savedRequest.getEmployeeId(), savedRequest.getLeaveType(), year)
                .orElseThrow(() -> new LeaveNotFoundException("LeaveBalance", savedRequest.getEmployeeId()));

        if (savedRequest.getStatus() == com.gogidix.hr.leavemanagement.domain.enums.LeaveStatus.APPROVED) {
            balance.approvePending(savedRequest.getRequestedDays());
            leaveBalanceRepository.save(balance);
        }

        // Publish event
        eventPublisher.publish("leave-request-approved", new LeaveRequestApprovedEvent(
                savedRequest.getTenantId(),
                savedRequest.getRequestId(),
                savedRequest.getEmployeeId(),
                command.getApproverId(),
                command.getApproverName(),
                savedRequest.getLeaveType().name(),
                savedRequest.getStartDate(),
                savedRequest.getEndDate(),
                savedRequest.getRequestedDays(),
                java.time.LocalDateTime.now().toString()
        ));

        log.info("Approved leave request: {}", command.getRequestId());
    }

    @Transactional
    public void reject(LeaveCommand.RejectLeaveRequestCommand command) {
        log.info("Rejecting leave request: {} by {}", command.getRequestId(), command.getApproverId());

        LeaveRequest request = leaveRequestRepository.findByRequestIdAndTenantId(command.getRequestId(), command.getTenantId())
                .orElseThrow(() -> new LeaveNotFoundException("LeaveRequest", command.getRequestId()));

        request.reject(command.getApproverId(), command.getReason());
        LeaveRequest savedRequest = leaveRequestRepository.save(request);

        // Remove pending from balance
        String year = String.valueOf(savedRequest.getStartDate().getYear());
        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeAndYear(savedRequest.getEmployeeId(), savedRequest.getLeaveType(), year)
                .orElseThrow(() -> new LeaveNotFoundException("LeaveBalance", savedRequest.getEmployeeId()));

        balance.rejectPending(savedRequest.getRequestedDays());
        leaveBalanceRepository.save(balance);

        // Publish event
        eventPublisher.publish("leave-request-rejected", new LeaveRequestRejectedEvent(
                savedRequest.getTenantId(),
                savedRequest.getRequestId(),
                savedRequest.getEmployeeId(),
                command.getApproverId(),
                command.getReason(),
                savedRequest.getLeaveType().name(),
                java.time.LocalDateTime.now().toString()
        ));

        log.info("Rejected leave request: {}", command.getRequestId());
    }

    @Transactional
    public void cancel(LeaveCommand.CancelLeaveRequestCommand command) {
        log.info("Cancelling leave request: {} by {}", command.getRequestId(), command.getEmployeeId());

        LeaveRequest request = leaveRequestRepository.findByRequestIdAndTenantId(command.getRequestId(), command.getTenantId())
                .orElseThrow(() -> new LeaveNotFoundException("LeaveRequest", command.getRequestId()));

        request.cancel(command.getEmployeeId(), command.getReason());
        LeaveRequest savedRequest = leaveRequestRepository.save(request);

        // Update balance if approved
        if (request.getStatus() == com.gogidix.hr.leavemanagement.domain.enums.LeaveStatus.APPROVED) {
            String year = String.valueOf(savedRequest.getStartDate().getYear());
            LeaveBalance balance = leaveBalanceRepository
                    .findByEmployeeIdAndLeaveTypeAndYear(savedRequest.getEmployeeId(), savedRequest.getLeaveType(), year)
                    .orElseThrow(() -> new LeaveNotFoundException("LeaveBalance", savedRequest.getEmployeeId()));

            balance.addBalance(savedRequest.getRequestedDays());
            leaveBalanceRepository.save(balance);
        } else {
            // Remove pending
            String year = String.valueOf(savedRequest.getStartDate().getYear());
            LeaveBalance balance = leaveBalanceRepository
                    .findByEmployeeIdAndLeaveTypeAndYear(savedRequest.getEmployeeId(), savedRequest.getLeaveType(), year)
                    .orElseThrow(() -> new LeaveNotFoundException("LeaveBalance", savedRequest.getEmployeeId()));

            balance.rejectPending(savedRequest.getRequestedDays());
            leaveBalanceRepository.save(balance);
        }

        // Publish event
        eventPublisher.publish("leave-request-cancelled", new LeaveRequestCancelledEvent(
                savedRequest.getTenantId(),
                savedRequest.getRequestId(),
                savedRequest.getEmployeeId(),
                command.getEmployeeId(),
                command.getReason(),
                savedRequest.getLeaveType().name(),
                java.time.LocalDateTime.now().toString()
        ));

        log.info("Cancelled leave request: {}", command.getRequestId());
    }

    @Transactional
    public void delete(String requestId, String tenantId) {
        log.info("Deleting leave request: {}", requestId);

        LeaveRequest request = leaveRequestRepository.findByRequestIdAndTenantId(requestId, tenantId)
                .orElseThrow(() -> new LeaveNotFoundException("LeaveRequest", requestId));

        if (request.getStatus() != com.gogidix.hr.leavemanagement.domain.enums.LeaveStatus.PENDING) {
            throw new LeaveValidationException("Cannot delete request in status: " + request.getStatus());
        }

        leaveRequestRepository.deleteByRequestIdAndTenantId(requestId, tenantId);
        log.info("Deleted leave request: {}", requestId);
    }

    private Double calculateDuration(LocalDate startDate, LocalDate endDate, com.gogidix.hr.leavemanagement.domain.enums.RequestType requestType, Double hours) {
        if (requestType == com.gogidix.hr.leavemanagement.domain.enums.RequestType.HOURS && hours != null) {
            return hours / 8.0;
        }
        if (requestType == com.gogidix.hr.leavemanagement.domain.enums.RequestType.HALF_DAY) {
            return 0.5;
        }
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return (double) days;
    }

    private LeaveBalance createInitialBalance(LeaveCommand.CreateLeaveRequestCommand command, String year) {
        LeavePolicy policy = leavePolicyRepository.findByTenantIdAndLeaveType(command.getTenantId(), command.getLeaveType())
                .stream()
                .filter(LeavePolicy::getIsActive)
                .findFirst()
                .orElse(null);

        Double allocation = policy != null ? policy.getAnnualAllocation() : 0.0;

        LeaveBalance balance = new LeaveBalance();
        balance.setId(UUID.randomUUID().toString());
        balance.setTenantId(command.getTenantId());
        balance.setCountryCode(command.getCountryCode());
        balance.setBalanceId(generateBalanceId());
        balance.setEmployeeId(command.getEmployeeId());
        balance.setEmployeeName(command.getEmployeeName());
        balance.setEmployeeCode(command.getEmployeeCode());
        balance.setDepartment(command.getDepartment());
        balance.setLeaveType(command.getLeaveType());
        balance.setTotalAllocated(allocation);
        balance.setUsed(0.0);
        balance.setPending(0.0);
        balance.setCarriedForward(0.0);
        balance.setAccrued(0.0);
        balance.setEncashed(0.0);
        balance.setForfeited(0.0);
        balance.setYear(year);
        balance.setIsUnlimited(policy != null && policy.getIsUnlimited() != null ? policy.getIsUnlimited() : false);
        balance.setIsNegativeAllowed(policy != null && policy.getNegativeBalanceAllowed() != null ? policy.getNegativeBalanceAllowed() : false);
        balance.setNegativeLimit(policy != null ? policy.getNegativeBalanceLimit() : null);
        balance.setPolicyId(policy != null ? policy.getPolicyId() : null);
        balance.setIsActive(true);
        balance.setUpdatedBy(command.getCreatedBy());

        balance.calculateAvailable();
        return leaveBalanceRepository.save(balance);
    }

    private boolean isNegativeBalanceAllowed(LeavePolicy policy) {
        return policy != null && Boolean.TRUE.equals(policy.getNegativeBalanceAllowed());
    }

    private int calculateTenureDays(String employeeId) {
        // This would typically be fetched from employee service
        // For now, assume minimum tenure
        return 365;
    }

    private String generateRequestId() {
        return "LR-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateBalanceId() {
        return "LB-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
