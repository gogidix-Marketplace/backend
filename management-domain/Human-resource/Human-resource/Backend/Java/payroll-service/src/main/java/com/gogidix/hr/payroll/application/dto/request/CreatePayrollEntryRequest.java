package com.gogidix.hr.payroll.application.dto.request;

import com.gogidix.hr.payroll.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Create Payroll Entry Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePayrollEntryRequest {

    @NotBlank(message = "Country code is required")
    private String countryCode;

    @NotBlank(message = "Payroll ID is required")
    private String payrollId;

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @NotBlank(message = "Employee name is required")
    private String employeeName;

    @NotBlank(message = "Employee code is required")
    private String employeeCode;

    private String department;

    private String position;

    @NotNull(message = "Pay period start is required")
    private LocalDate payPeriodStart;

    @NotNull(message = "Pay period end is required")
    private LocalDate payPeriodEnd;

    @NotNull(message = "Basic salary is required")
    @Positive(message = "Basic salary must be positive")
    private BigDecimal basicSalary;

    private BigDecimal overtimeHours;

    private BigDecimal overtimeRate;

    private BigDecimal bonus;

    private BigDecimal commission;

    private BigDecimal allowances;

    private BigDecimal federalTax;

    private BigDecimal stateTax;

    private BigDecimal localTax;

    private BigDecimal socialSecurityTax;

    private BigDecimal medicareTax;

    private BigDecimal otherTaxes;

    private BigDecimal healthInsurance;

    private BigDecimal dentalInsurance;

    private BigDecimal retirement401k;

    private BigDecimal otherDeductions;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String bankAccountNumber;

    private String bankRoutingNumber;

    @NotNull(message = "Currency is required")
    private String currency;

    private String taxCode;

    private Integer taxExemptions;

    private List<DeductionDetailRequest> deductionDetails;

    private List<EarningDetailRequest> earningDetails;

    private String notes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeductionDetailRequest {
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
    public static class EarningDetailRequest {
        private String earningType;
        private String description;
        private BigDecimal amount;
        private Integer hours;
        private BigDecimal rate;
        private String referenceId;
    }
}
