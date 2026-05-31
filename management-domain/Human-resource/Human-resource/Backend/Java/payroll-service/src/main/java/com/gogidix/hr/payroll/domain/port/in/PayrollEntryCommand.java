package com.gogidix.hr.payroll.domain.port.in;

import com.gogidix.hr.payroll.domain.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Payroll Entry Command
 * Input commands for payroll entry operations
 */
public interface PayrollEntryCommand {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateEntryCommand {
        private String tenantId;
        private String countryCode;
        private String payrollId;
        private String employeeId;
        private String employeeName;
        private String employeeCode;
        private String department;
        private String position;
        private BigDecimal basicSalary;
        private BigDecimal overtimeHours;
        private BigDecimal overtimeRate;
        private BigDecimal bonus;
        private BigDecimal commission;
        private BigDecimal allowances;
        private BigDecimal healthInsurance;
        private BigDecimal dentalInsurance;
        private BigDecimal retirement401k;
        private PaymentMethod paymentMethod;
        private String bankAccountNumber;
        private String bankRoutingNumber;
        private String taxCode;
        private Integer taxExemptions;
        private String createdBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateEntryCommand {
        private String tenantId;
        private String entryId;
        private BigDecimal basicSalary;
        private BigDecimal overtimeHours;
        private BigDecimal overtimeRate;
        private BigDecimal bonus;
        private BigDecimal commission;
        private BigDecimal allowances;
        private BigDecimal healthInsurance;
        private BigDecimal dentalInsurance;
        private BigDecimal retirement401k;
        private PaymentMethod paymentMethod;
        private String bankAccountNumber;
        private String bankRoutingNumber;
        private String notes;
        private String updatedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateTaxCommand {
        private String tenantId;
        private String entryId;
        private BigDecimal federalTax;
        private BigDecimal stateTax;
        private BigDecimal localTax;
        private BigDecimal socialSecurityTax;
        private BigDecimal medicareTax;
        private BigDecimal otherTaxes;
        private String updatedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class HoldPaymentCommand {
        private String tenantId;
        private String entryId;
        private String reason;
        private String updatedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class ReleaseHoldCommand {
        private String tenantId;
        private String entryId;
        private String updatedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteEntryCommand {
        private String tenantId;
        private String entryId;
        private String deletedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsPaidCommand {
        private String tenantId;
        private String entryId;
        private String paymentReference;
        private String updatedBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CalculateEntryCommand {
        private String tenantId;
        private String entryId;
        private String calculatedBy;
    }
}
