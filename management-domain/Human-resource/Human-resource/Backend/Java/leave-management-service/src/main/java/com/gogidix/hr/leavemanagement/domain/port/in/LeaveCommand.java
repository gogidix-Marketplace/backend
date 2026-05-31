package com.gogidix.hr.leavemanagement.domain.port.in;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Leave Command
 * Input commands for leave operations
 */
public interface LeaveCommand {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateLeaveRequestCommand {
        private String tenantId;
        private String countryCode;
        private String employeeId;
        private String employeeName;
        private String employeeCode;
        private String department;
        private String position;
        private String managerId;
        private LeaveType leaveType;
        private RequestType requestType;
        private LocalDate startDate;
        private LocalDate endDate;
        private Double requestedDays;
        private Double hoursRequested;
        private String reason;
        private String contactDuringLeave;
        private String emergencyContact;
        private Boolean isHalfDay;
        private String halfDayType;
        private List<AttachmentRequest> attachments;
        private String year;
        private String reliefStaffId;
        private String reliefStaffName;
        private String handoverNotes;
        private String createdBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateLeaveRequestCommand {
        private String tenantId;
        private String requestId;
        private LocalDate startDate;
        private LocalDate endDate;
        private Double requestedDays;
        private String reason;
        private String contactDuringLeave;
        private String emergencyContact;
        private List<AttachmentRequest> attachments;
        private String updatedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class ApproveLeaveRequestCommand {
        private String tenantId;
        private String requestId;
        private String approverId;
        private String approverName;
        private String comments;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class RejectLeaveRequestCommand {
        private String tenantId;
        private String requestId;
        private String approverId;
        private String reason;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelLeaveRequestCommand {
        private String tenantId;
        private String requestId;
        private String employeeId;
        private String reason;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateLeavePolicyCommand {
        private String tenantId;
        private String countryCode;
        private String policyCode;
        private String policyName;
        private LeaveType leaveType;
        private String description;
        private Double annualAllocation;
        private Double accrualRate;
        private String accrualFrequency;
        private Boolean requiresApproval;
        private Boolean documentsRequired;
        private Boolean paidLeave;
        private Boolean carryForwardAllowed;
        private Double carryForwardLimit;
        private Boolean isActive;
        private LocalDate effectiveFrom;
        private String createdBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateBalanceCommand {
        private String tenantId;
        private String balanceId;
        private Double totalAllocated;
        private Double carriedForward;
        private String updatedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateHolidayCommand {
        private String tenantId;
        private String countryCode;
        private String stateCode;
        private String holidayName;
        private LocalDate holidayDate;
        private String holidayType;
        private Boolean isRecurring;
        private Boolean isPaid;
        private String description;
        private String createdBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class AttachmentRequest {
        private String fileName;
        private String fileUrl;
        private String fileType;
        private Long fileSize;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class EncashBalanceCommand {
        private String tenantId;
        private String balanceId;
        private String employeeId;
        private Double days;
        private Double dailyRate;
        private String requestedBy;
    }
}
