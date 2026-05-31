package com.gogidix.hr.payroll.application.dto.response;

import com.gogidix.hr.payroll.domain.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Payroll Entry Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollEntryResponse {

    private String id;
    private String tenantId;
    private String countryCode;
    private String payrollId;
    private String employeeId;
    private String employeeName;
    private String employeeCode;
    private String department;
    private String position;
    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
    private BigDecimal basicSalary;
    private BigDecimal overtimeHours;
    private BigDecimal overtimeRate;
    private BigDecimal overtimePay;
    private BigDecimal bonus;
    private BigDecimal commission;
    private BigDecimal allowances;
    private BigDecimal grossPay;
    private BigDecimal federalTax;
    private BigDecimal stateTax;
    private BigDecimal localTax;
    private BigDecimal socialSecurityTax;
    private BigDecimal medicareTax;
    private BigDecimal otherTaxes;
    private BigDecimal totalTax;
    private BigDecimal healthInsurance;
    private BigDecimal dentalInsurance;
    private BigDecimal retirement401k;
    private BigDecimal otherDeductions;
    private BigDecimal totalDeductions;
    private BigDecimal netPay;
    private PaymentMethod paymentMethod;
    private String bankAccountNumber;
    private String bankRoutingNumber;
    private String checkNumber;
    private LocalDate paymentDate;
    private Boolean paid;
    private String currency;
    private String taxCode;
    private Integer taxExemptions;
    private List<DeductionDetailResponse> deductionDetails;
    private List<EarningDetailResponse> earningDetails;
    private String notes;
    private Boolean isHold;
    private String holdReason;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeductionDetailResponse {
        private String deductionType;
        private String description;
        private BigDecimal amount;
        private Boolean pretax;
        private String referenceId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EarningDetailResponse {
        private String earningType;
        private String description;
        private BigDecimal amount;
        private Integer hours;
        private BigDecimal rate;
        private String referenceId;
    }
}
