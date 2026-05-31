package com.gogidix.hr.payroll.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Payroll Command
 * Input commands for payroll operations
 */
public interface PayrollCommand {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CreatePayrollCommand {
        private String tenantId;
        private String countryCode;
        private String payrollName;
        private YearMonth payrollPeriod;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDate paymentDate;
        private String frequency;
        private String currency;
        private String runType;
        private List<String> employeeIds;
        private String notes;
        private String createdBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdatePayrollCommand {
        private String tenantId;
        private String payrollId;
        private String payrollName;
        private LocalDate paymentDate;
        private String notes;
        private String updatedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class SubmitPayrollCommand {
        private String tenantId;
        private String payrollId;
        private String submittedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class ApprovePayrollCommand {
        private String tenantId;
        private String payrollId;
        private String approvedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class RejectPayrollCommand {
        private String tenantId;
        private String payrollId;
        private String reason;
        private String rejectedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class ProcessPayrollCommand {
        private String tenantId;
        private String payrollId;
        private String processedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsPaidCommand {
        private String tenantId;
        private String payrollId;
        private String paymentReference;
        private String processedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class LockPayrollCommand {
        private String tenantId;
        private String payrollId;
        private String lockedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class UnlockPayrollCommand {
        private String tenantId;
        private String payrollId;
        private String unlockedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelPayrollCommand {
        private String tenantId;
        private String payrollId;
        private String reason;
        private String cancelledBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class AddEmployeeCommand {
        private String tenantId;
        private String payrollId;
        private String employeeId;
        private String addedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveEmployeeCommand {
        private String tenantId;
        private String payrollId;
        private String employeeId;
        private String removedBy;
        private String reason;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class GeneratePayslipsCommand {
        private String tenantId;
        private String payrollId;
        private String generatedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CalculateTaxCommand {
        private String tenantId;
        private String payrollId;
        private String taxRuleId;
        private String employeeId;
        private BigDecimal grossPay;
        private Integer exemptions;
        private BigDecimal ytdTax;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class DeletePayrollCommand {
        private String tenantId;
        private String payrollId;
        private String deletedBy;
    }
}
