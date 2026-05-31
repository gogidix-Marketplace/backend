package com.gogidix.hr.leavemanagement.domain.model;

import com.gogidix.hr.leavemanagement.domain.enums.ApprovalStatus;
import com.gogidix.hr.leavemanagement.domain.enums.LeaveStatus;
import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.enums.RequestType;
import com.gogidix.hr.leavemanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Leave Request Aggregate Root
 * Represents a leave request submitted by an employee
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String requestId;
    private String employeeId;
    private String employeeName;
    private String employeeCode;
    private String department;
    private String position;
    private String managerId;
    private LeaveType leaveType;
    private RequestType requestType;
    private LeaveStatus status = LeaveStatus.PENDING;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double requestedDays;
    private Double hoursRequested;
    private String reason;
    private String rejectionReason;
    private String contactDuringLeave;
    private String emergencyContact;
    private Boolean isHalfDay;
    private String halfDayType;
    private LocalDate submissionDate;
    private LocalDateTime approvalDate;
    private String approvedBy;
    private String approverName;
    private Integer approvalLevel;
    private List<ApprovalStep> approvalSteps = new ArrayList<>();
    private List<Attachment> attachments = new ArrayList<>();
    private String policyId;
    private Boolean requiresApproval;
    private Boolean isPaid;
    private Boolean carryForwardAllowed;
    private Double carriedForwardDays;
    private String year;
    private Boolean isEncashment;
    private Double encashmentAmount;
    private String reliefStaffId;
    private String reliefStaffName;
    private String handoverNotes;
    private String cancellationReason;
    private LocalDate cancellationDate;
    private String createdByEmployee;
    private Boolean documentsVerified;
    private String verificationNotes;
    private Boolean overlapWithHoliday;
    private String holidayAdjustmentNotes;

    /**
     * Submit leave request for approval
     */
    public void submit(String employeeId) {
        if (this.status != LeaveStatus.PENDING) {
            throw new IllegalStateException("Leave request can only be submitted when in PENDING status");
        }
        this.submissionDate = LocalDate.now();
        this.createdByEmployee = employeeId;
    }

    /**
     * Approve leave request
     */
    public void approve(String approverId, String approverName) {
        if (this.status != LeaveStatus.PENDING) {
            throw new IllegalStateException("Can only approve PENDING leave requests");
        }

        // Check if this is the final approval level
        boolean isFinalApproval = this.approvalLevel == null ||
                this.approvalLevel == 0 ||
                (this.approvalSteps != null && this.approvalSteps.size() > 0 &&
                        this.approvalLevel >= this.approvalSteps.size());

        if (isFinalApproval) {
            this.status = LeaveStatus.APPROVED;
            this.approvalDate = LocalDateTime.now();
        }

        this.approvedBy = approverId;
        this.approverName = approverName;

        // Record approval step
        if (this.approvalSteps == null) {
            this.approvalSteps = new ArrayList<>();
        }
        this.approvalSteps.add(ApprovalStep.builder()
                .level(this.approvalLevel != null ? this.approvalLevel : 1)
                .approverId(approverId)
                .approverName(approverName)
                .status(ApprovalStatus.APPROVED)
                .timestamp(LocalDateTime.now())
                .build());

        if (this.approvalLevel != null) {
            this.approvalLevel++;
        }
    }

    /**
     * Reject leave request
     */
    public void reject(String approverId, String reason) {
        if (this.status != LeaveStatus.PENDING) {
            throw new IllegalStateException("Can only reject PENDING leave requests");
        }

        this.status = LeaveStatus.REJECTED;
        this.rejectionReason = reason;
        this.approvedBy = approverId;

        // Record rejection step
        if (this.approvalSteps == null) {
            this.approvalSteps = new ArrayList<>();
        }
        this.approvalSteps.add(ApprovalStep.builder()
                .level(this.approvalLevel != null ? this.approvalLevel : 1)
                .approverId(approverId)
                .status(ApprovalStatus.REJECTED)
                .comments(reason)
                .timestamp(LocalDateTime.now())
                .build());
    }

    /**
     * Cancel leave request
     */
    public void cancel(String employeeId, String reason) {
        if (this.status == LeaveStatus.CANCELLED || this.status == LeaveStatus.REJECTED) {
            throw new IllegalStateException("Cannot cancel a request that is already " + this.status);
        }

        if (this.status == LeaveStatus.USED && !canCancelUsedLeave()) {
            throw new IllegalStateException("Cannot cancel leave that has already been used");
        }

        this.status = LeaveStatus.CANCELLED;
        this.cancellationReason = reason;
        this.cancellationDate = LocalDate.now();
        this.updatedBy = employeeId;
    }

    /**
     * Calculate duration in days
     */
    public Double calculateDuration() {
        if (startDate == null || endDate == null) {
            return 0.0;
        }

        if (requestType == RequestType.FULL_DAY) {
            long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            return (double) days;
        }

        if (requestType == RequestType.HALF_DAY) {
            return 0.5;
        }

        if (requestType == RequestType.HOURS && hoursRequested != null) {
            return hoursRequested / 8.0; // Assuming 8-hour workday
        }

        return requestedDays != null ? requestedDays : 0.0;
    }

    /**
     * Check if leave spans weekend
     */
    public boolean spansWeekend() {
        if (startDate == null || endDate == null) {
            return false;
        }

        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            if (current.getDayOfWeek().getValue() >= 6) { // Saturday or Sunday
                return true;
            }
            current = current.plusDays(1);
        }
        return false;
    }

    /**
     * Check if can cancel used leave
     */
    public boolean canCancelUsedLeave() {
        // Allow cancellation within 24 hours of approval
        if (this.approvalDate != null) {
            return LocalDateTime.now().isBefore(this.approvalDate.plusHours(24));
        }
        return false;
    }

    /**
     * Check if request is overdue
     */
    public boolean isOverdue() {
        return this.endDate != null && LocalDate.now().isAfter(this.endDate)
                && this.status == LeaveStatus.APPROVED;
    }

    /**
     * Check if documents are required
     */
    public boolean documentsRequired() {
        return leaveType == LeaveType.SICK ||
                leaveType == LeaveType.MATERNITY ||
                leaveType == LeaveType.PATERNITY ||
                leaveType == LeaveType.COMPASSIONATE;
    }

    /**
     * Mark as used
     */
    public void markAsUsed() {
        if (this.status != LeaveStatus.APPROVED) {
            throw new IllegalStateException("Can only mark APPROVED leave as USED");
        }
        this.status = LeaveStatus.USED;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApprovalStep {
        private Integer level;
        private String approverId;
        private String approverName;
        private ApprovalStatus status;
        private LocalDateTime timestamp;
        private String comments;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attachment {
        private String attachmentId;
        private String fileName;
        private String fileUrl;
        private String fileType;
        private Long fileSize;
        private LocalDateTime uploadedAt;
        private String uploadedBy;
    }
}
