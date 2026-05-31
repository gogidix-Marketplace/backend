package com.gogidix.hr.payroll.domain.model;

import com.gogidix.hr.payroll.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Payslip
 * Represents the detailed payslip for an employee
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Payslip extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String payslipId;
    private String payrollId;
    private String employeeId;
    private String employeeName;
    private String employeeCode;
    private String department;
    private String position;
    private YearMonth payPeriod;
    private LocalDate payDate;
    private String currency;
    private BigDecimal grossPay;
    private BigDecimal netPay;
    private BigDecimal totalTax;
    private BigDecimal totalDeductions;
    private BigDecimal ytdGrossPay;
    private BigDecimal ytdNetPay;
    private BigDecimal ytdTax;
    private List<EarningItem> earnings = new ArrayList<>();
    private List<TaxItem> taxes = new ArrayList<>();
    private List<DeductionItem> deductions = new ArrayList<>();
    private List<YearToDateItem> yearToDateItems = new ArrayList<>();
    private String paymentMethod;
    private String bankAccount;
    private String checkNumber;
    private Integer hoursWorked;
    private Integer overtimeHours;
    private BigDecimal hourlyRate;
    private BigDecimal salaryRate;
    private Integer payFrequency;
    private String payFrequencyText;
    private String taxPeriod;
    private String taxCode;
    private Integer taxExemptions;
    private LocalDate issuedDate;
    private String status;
    private Boolean isFinal;
    private String notes;
    private String companyInfo;
    private String companyAddress;
    private String companyLogo;

    /**
     * Add earning item
     */
    public void addEarning(EarningItem item) {
        this.earnings.add(item);
    }

    /**
     * Add tax item
     */
    public void addTax(TaxItem item) {
        this.taxes.add(item);
    }

    /**
     * Add deduction item
     */
    public void addDeduction(DeductionItem item) {
        this.deductions.add(item);
    }

    /**
     * Calculate totals
     */
    public void calculateTotals() {
        this.grossPay = earnings.stream()
                .map(EarningItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalTax = taxes.stream()
                .map(TaxItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalDeductions = deductions.stream()
                .map(DeductionItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.netPay = grossPay.subtract(totalTax).subtract(totalDeductions);
    }

    /**
     * Mark as issued
     */
    public void markAsIssued() {
        this.issuedDate = LocalDate.now();
        this.status = "ISSUED";
    }

    /**
     * Mark as final
     */
    public void markAsFinal() {
        this.isFinal = true;
        this.status = "FINAL";
    }

    /**
     * Get year-to-date total for a specific item
     */
    public BigDecimal getYtdTotal(String itemType, String itemCode) {
        return yearToDateItems.stream()
                .filter(item -> item.getItemType().equals(itemType) && item.getItemCode().equals(itemCode))
                .map(YearToDateItem::getAmount)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EarningItem {
        private String earningCode;
        private String earningType;
        private String description;
        private BigDecimal amount;
        private Integer hours;
        private BigDecimal rate;
        private Integer quantity;
        private Boolean isTaxable;
        private String category;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaxItem {
        private String taxCode;
        private String taxType;
        private String description;
        private BigDecimal amount;
        private BigDecimal rate;
        private BigDecimal taxableWages;
        private String category;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeductionItem {
        private String deductionCode;
        private String deductionType;
        private String description;
        private BigDecimal amount;
        private BigDecimal rate;
        private Boolean isPreTax;
        private Boolean isEmployeePaid;
        private String category;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class YearToDateItem {
        private String itemType;
        private String itemCode;
        private String description;
        private BigDecimal amount;
        private String category;
    }
}
