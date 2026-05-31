package com.gogidix.hr.leavemanagement.domain.model;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Leave Balance
 * Tracks leave balance for an employee by leave type
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalance extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String balanceId;
    private String employeeId;
    private String employeeName;
    private String employeeCode;
    private String department;
    private LeaveType leaveType;
    private Double totalAllocated;
    private Double used;
    private Double pending = 0.0;
    private Double available = 0.0;
    private Double carriedForward;
    private Double accrued;
    private Double encashed;
    private Double forfeited;
    private String year;
    private YearMonth period;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Double accrualRate;
    private String accrualFrequency;
    private Double maxAccrual;
    private Double carryForwardLimit;
    private LocalDate carryForwardExpiryDate;
    private Boolean isUnlimited;
    private Boolean isNegativeAllowed;
    private Double negativeLimit;
    private String policyId;
    private String lastCalculatedDate;
    private String notes;
    private Boolean isActive;

    /**
     * Calculate available balance
     */
    public void calculateAvailable() {
        if (isUnlimited != null && isUnlimited) {
            this.available = Double.MAX_VALUE;
            return;
        }

        double base = (totalAllocated != null ? totalAllocated : 0.0) +
                (carriedForward != null ? carriedForward : 0.0) +
                (accrued != null ? accrued : 0.0) -
                (used != null ? used : 0.0) -
                (encashed != null ? encashed : 0.0) -
                (forfeited != null ? forfeited : 0.0);

        this.available = Math.max(0.0, base);

        // Check negative limit
        if (Boolean.TRUE.equals(isNegativeAllowed) && negativeLimit != null && base < 0) {
            this.available = Math.max(negativeLimit, base);
        }
    }

    /**
     * Check if sufficient balance is available
     */
    public boolean hasSufficientBalance(Double requiredDays) {
        if (isUnlimited != null && isUnlimited) {
            return true;
        }

        calculateAvailable();

        // Check against available minus pending
        double effectiveAvailable = available - (pending != null ? pending : 0.0);

        return effectiveAvailable >= requiredDays;
    }

    /**
     * Deduct balance
     */
    public void deductBalance(Double days) {
        if (!hasSufficientBalance(days)) {
            throw new IllegalArgumentException("Insufficient leave balance. Available: " + available + ", Required: " + days);
        }

        this.used = (this.used != null ? this.used : 0.0) + days;
        calculateAvailable();
    }

    /**
     * Add pending balance
     */
    public void addPending(Double days) {
        this.pending = (this.pending != null ? this.pending : 0.0) + days;
    }

    /**
     * Remove pending balance
     */
    public void removePending(Double days) {
        this.pending = Math.max(0.0, (this.pending != null ? this.pending : 0.0) - days);
    }

    /**
     * Approve pending deduction
     */
    public void approvePending(Double days) {
        removePending(days);
        deductBalance(days);
    }

    /**
     * Reject pending - just remove from pending
     */
    public void rejectPending(Double days) {
        removePending(days);
    }

    /**
     * Add balance (e.g., from accrual or allocation)
     */
    public void addBalance(Double days) {
        this.accrued = (this.accrued != null ? this.accrued : 0.0) + days;
        calculateAvailable();
    }

    /**
     * Encash balance
     */
    public Double encashBalance(Double days) {
        if (days > available) {
            throw new IllegalArgumentException("Cannot encash more than available balance");
        }

        this.encashed = (this.encashed != null ? this.encashed : 0.0) + days;
        calculateAvailable();

        return days;
    }

    /**
     * Carry forward balance
     */
    public void carryForward(Double days, LocalDate expiryDate) {
        this.carriedForward = (this.carriedForward != null ? this.carriedForward : 0.0) + days;

        if (carryForwardLimit != null && this.carriedForward > carryForwardLimit) {
            this.forfeited = (this.forfeited != null ? this.forfeited : 0.0) +
                    (this.carriedForward - carryForwardLimit);
            this.carriedForward = carryForwardLimit;
        }

        this.carryForwardExpiryDate = expiryDate;
    }

    /**
     * Check if carried forward days have expired
     */
    public boolean isCarryForwardExpired() {
        return carryForwardExpiryDate != null && LocalDate.now().isAfter(carryForwardExpiryDate);
    }

    /**
     * Forfeit expired carried forward balance
     */
    public void forfeitExpiredCarryForward() {
        if (isCarryForwardExpired() && carriedForward != null && carriedForward > 0) {
            this.forfeited = (this.forfeited != null ? this.forfeited : 0.0) + carriedForward;
            this.carriedForward = 0.0;
            calculateAvailable();
        }
    }

    /**
     * Reset balance for new period
     */
    public void resetForNewPeriod(Double newAllocation) {
        // Forfeit expired carry forward
        forfeitExpiredCarryForward();

        // Reset totals for new period
        this.totalAllocated = newAllocation;
        this.used = 0.0;
        this.pending = 0.0;
        this.accrued = 0.0;
        this.encashed = 0.0;

        calculateAvailable();
    }

    /**
     * Get effective available balance (excluding pending)
     */
    public Double getEffectiveAvailable() {
        calculateAvailable();
        return available - (pending != null ? pending : 0.0);
    }

    /**
     * Get utilization percentage
     */
    public Double getUtilizationPercentage() {
        if (totalAllocated == null || totalAllocated == 0) {
            return 0.0;
        }
        double used = this.used != null ? this.used : 0.0;
        return (used / totalAllocated) * 100.0;
    }

    /**
     * Check if balance is low (less than 20%)
     */
    public boolean isLowBalance() {
        if (isUnlimited != null && isUnlimited) {
            return false;
        }
        return getUtilizationPercentage() >= 80.0;
    }

    /**
     * Get balance summary as string
     */
    public String getBalanceSummary() {
        return String.format("Available: %.2f, Used: %.2f, Pending: %.2f, Total: %.2f",
                available, used != null ? used : 0.0, pending != null ? pending : 0.0,
                totalAllocated != null ? totalAllocated : 0.0);
    }
}
