package com.gogidix.hr.payroll.domain.model;

import com.gogidix.hr.payroll.domain.enums.PayrollStatus;
import com.gogidix.hr.payroll.domain.enums.SalaryFrequency;
import com.gogidix.hr.payroll.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Payroll aggregate root
 * Represents a complete payroll cycle for an organization
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Payroll extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String payrollId;
    private String payrollName;
    private YearMonth payrollPeriod;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate paymentDate;
    private PayrollStatus status = PayrollStatus.DRAFT;
    private SalaryFrequency frequency;
    private String currency;
    private BigDecimal totalGrossPay;
    private BigDecimal totalNetPay;
    private BigDecimal totalTaxes;
    private BigDecimal totalDeductions;
    private Integer employeeCount;
    private String processedBy;
    private String approvedBy;
    private LocalDate approvedDate;
    private String batchId;
    private String runType;
    private List<String> payrollEntryIds = new ArrayList<>();
    private String notes;
    private Boolean isLocked;
    private String lockedBy;
    private LocalDate lockedDate;

    /**
     * Calculate total payroll amounts
     */
    public void calculateTotals(List<PayrollEntry> entries) {
        this.totalGrossPay = entries.stream()
                .map(PayrollEntry::getGrossPay)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalNetPay = entries.stream()
                .map(PayrollEntry::getNetPay)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalTaxes = entries.stream()
                .map(PayrollEntry::getTotalTax)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalDeductions = entries.stream()
                .map(PayrollEntry::getTotalDeductions)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.employeeCount = entries.size();
    }

    /**
     * Submit payroll for approval
     */
    public void submitForApproval(String userId) {
        if (this.status != PayrollStatus.DRAFT) {
            throw new IllegalStateException("Payroll can only be submitted from DRAFT status");
        }
        this.status = PayrollStatus.PENDING_APPROVAL;
        this.updatedBy = userId;
    }

    /**
     * Approve payroll
     */
    public void approve(String userId) {
        if (this.status != PayrollStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Payroll can only be approved from PENDING_APPROVAL status");
        }
        this.status = PayrollStatus.APPROVED;
        this.approvedBy = userId;
        this.approvedDate = LocalDate.now();
        this.updatedBy = userId;
    }

    /**
     * Reject payroll
     */
    public void reject(String userId) {
        if (this.status != PayrollStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Payroll can only be rejected from PENDING_APPROVAL status");
        }
        this.status = PayrollStatus.REJECTED;
        this.updatedBy = userId;
    }

    /**
     * Process payroll
     */
    public void process(String userId) {
        if (this.status != PayrollStatus.APPROVED) {
            throw new IllegalStateException("Payroll can only be processed from APPROVED status");
        }
        this.status = PayrollStatus.PROCESSED;
        this.processedBy = userId;
        this.updatedBy = userId;
    }

    /**
     * Mark payroll as paid
     */
    public void markAsPaid(String userId) {
        if (this.status != PayrollStatus.PROCESSED) {
            throw new IllegalStateException("Payroll can only be marked as paid from PROCESSED status");
        }
        this.status = PayrollStatus.PAID;
        this.updatedBy = userId;
    }

    /**
     * Lock payroll
     */
    public void lock(String userId) {
        if (this.isLocked) {
            throw new IllegalStateException("Payroll is already locked");
        }
        this.isLocked = true;
        this.lockedBy = userId;
        this.lockedDate = LocalDate.now();
        this.updatedBy = userId;
    }

    /**
     * Unlock payroll
     */
    public void unlock(String userId) {
        if (!this.isLocked) {
            throw new IllegalStateException("Payroll is not locked");
        }
        this.isLocked = false;
        this.lockedBy = null;
        this.lockedDate = null;
        this.updatedBy = userId;
    }

    /**
     * Check if payroll can be modified
     */
    public boolean canModify() {
        return !this.isLocked && (this.status == PayrollStatus.DRAFT || this.status == PayrollStatus.REJECTED);
    }

    /**
     * Check if payroll is in final state
     */
    public boolean isInFinalState() {
        return this.status == PayrollStatus.PAID || this.status == PayrollStatus.CANCELLED;
    }
}
