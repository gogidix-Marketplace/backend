package com.gogidix.hr.payroll.domain.model;

import com.gogidix.hr.payroll.domain.enums.PaymentMethod;
import com.gogidix.hr.payroll.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Payroll Entry
 * Represents individual employee payroll entry for a specific payroll cycle
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PayrollEntry extends BaseEntity {

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
    private List<DeductionDetail> deductionDetails = new ArrayList<>();
    private List<EarningDetail> earningDetails = new ArrayList<>();
    private String notes;
    private Boolean isHold;
    private String holdReason;

    /**
     * Calculate gross pay
     */
    public void calculateGrossPay() {
        this.grossPay = basicSalary
                .add(overtimePay != null ? overtimePay : BigDecimal.ZERO)
                .add(bonus != null ? bonus : BigDecimal.ZERO)
                .add(commission != null ? commission : BigDecimal.ZERO)
                .add(allowances != null ? allowances : BigDecimal.ZERO);
    }

    /**
     * Calculate overtime pay
     */
    public void calculateOvertimePay() {
        if (overtimeHours != null && overtimeRate != null) {
            this.overtimePay = overtimeHours.multiply(overtimeRate);
        }
    }

    /**
     * Calculate total tax
     */
    public void calculateTotalTax() {
        this.totalTax = (federalTax != null ? federalTax : BigDecimal.ZERO)
                .add(stateTax != null ? stateTax : BigDecimal.ZERO)
                .add(localTax != null ? localTax : BigDecimal.ZERO)
                .add(socialSecurityTax != null ? socialSecurityTax : BigDecimal.ZERO)
                .add(medicareTax != null ? medicareTax : BigDecimal.ZERO)
                .add(otherTaxes != null ? otherTaxes : BigDecimal.ZERO);
    }

    /**
     * Calculate total deductions
     */
    public void calculateTotalDeductions() {
        this.totalDeductions = (healthInsurance != null ? healthInsurance : BigDecimal.ZERO)
                .add(dentalInsurance != null ? dentalInsurance : BigDecimal.ZERO)
                .add(retirement401k != null ? retirement401k : BigDecimal.ZERO)
                .add(otherDeductions != null ? otherDeductions : BigDecimal.ZERO);
    }

    /**
     * Calculate net pay
     */
    public void calculateNetPay() {
        calculateGrossPay();
        calculateTotalTax();
        calculateTotalDeductions();
        this.netPay = grossPay.subtract(totalTax).subtract(totalDeductions);
    }

    /**
     * Mark entry as paid
     */
    public void markAsPaid(LocalDate paymentDate) {
        this.paid = true;
        this.paymentDate = paymentDate;
    }

    /**
     * Hold payment
     */
    public void holdPayment(String reason) {
        this.isHold = true;
        this.holdReason = reason;
    }

    /**
     * Release hold
     */
    public void releaseHold() {
        this.isHold = false;
        this.holdReason = null;
    }

    /**
     * Check if payment can be processed
     */
    public boolean canProcessPayment() {
        return !isHold && !paid && netPay != null && netPay.compareTo(BigDecimal.ZERO) > 0;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeductionDetail {
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
    public static class EarningDetail {
        private String earningType;
        private String description;
        private BigDecimal amount;
        private Integer hours;
        private BigDecimal rate;
        private Integer quantity;
        private String category;
        private String referenceId;
    }
}
