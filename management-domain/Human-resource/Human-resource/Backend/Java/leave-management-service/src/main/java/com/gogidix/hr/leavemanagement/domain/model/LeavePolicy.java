package com.gogidix.hr.leavemanagement.domain.model;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Leave Policy
 * Defines the rules and configuration for a leave type
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LeavePolicy extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String policyId;
    private String policyCode;
    private String policyName;
    private LeaveType leaveType;
    private String description;
    private Double annualAllocation;
    private Double maxAccrual;
    private Double accrualRate;
    private String accrualFrequency;
    private Double accrualPerPeriod;
    private Integer minServiceDays;
    private Integer maxConsecutiveDays;
    private Integer minDaysBetweenRequests;
    private Boolean requiresApproval;
    private Integer approvalLevels;
    private Boolean documentsRequired;
    private List<String> documentTypes;
    private Boolean paidLeave;
    private Double encashmentAllowed;
    private Double encashmentPercentage;
    private Boolean carryForwardAllowed;
    private Double carryForwardLimit;
    private Integer carryForwardExpiryDays;
    private Boolean proRatedForJoiners;
    private Boolean proRatedForLeavers;
    private Boolean negativeBalanceAllowed;
    private Double negativeBalanceLimit;
    private Boolean appliesToProbation;
    private Boolean appliesToContractors;
    private String applicability;
    private List<String> applicableDepartments;
    private List<String> applicableLocations;
    private Integer minNoticeDays;
    private Boolean canCancelApproved;
    private Integer cancellationNoticeHours;
    private Boolean halfDayAllowed;
    private Boolean shortNoticeAllowed;
    private Integer maxShortNoticePerYear;
    private Boolean sandwichRule;
    private Boolean excludeHolidays;
    private Boolean excludeWeekends;
    private YearEndProcessing yearEndProcessing;
    private Integer balanceExpiryDays;
    private Boolean isUnlimited;
    private Boolean isActive;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Integer priority;
    private String calculationMethod;
    private String timezone;
    private String notes;

    /**
     * Check if policy is effective for given date
     */
    public boolean isEffectiveForDate(LocalDate date) {
        if (!isActive) {
            return false;
        }

        boolean afterStart = effectiveFrom == null || !date.isBefore(effectiveFrom);
        boolean beforeEnd = effectiveTo == null || !date.isAfter(effectiveTo);

        return afterStart && beforeEnd;
    }

    /**
     * Calculate pro-rated allocation for partial year
     */
    public Double calculateProratedAllocation(LocalDate joinDate, LocalDate periodEnd) {
        if (!proRatedForJoiners || joinDate == null) {
            return annualAllocation;
        }

        long totalDays = Period.between(joinDate, periodEnd).getDays() + 1;
        long yearDays = periodEnd.lengthOfYear();

        double prorated = (annualAllocation * totalDays) / yearDays;

        // Round to 0.5 days
        return Math.round(prorated * 2.0) / 2.0;
    }

    /**
     * Validate leave request against policy
     */
    public List<String> validateRequest(LocalDate startDate, LocalDate endDate, Double requestedDays,
                                        Integer tenureDays, Double currentBalance) {
        List<String> errors = new ArrayList<>();

        // Check minimum service
        if (minServiceDays != null && tenureDays != null && tenureDays < minServiceDays) {
            errors.add("Minimum service of " + minServiceDays + " days required for this leave type");
        }

        // Check maximum consecutive days
        if (maxConsecutiveDays != null && requestedDays > maxConsecutiveDays) {
            errors.add("Maximum " + maxConsecutiveDays + " consecutive days allowed");
        }

        // Check approval requirement
        if (Boolean.TRUE.equals(requiresApproval)) {
            // Approval is required - no error, just informational
        }

        // Check notice period
        if (minNoticeDays != null) {
            LocalDate minRequestDate = LocalDate.now().plusDays(minNoticeDays);
            if (startDate.isBefore(minRequestDate)) {
                errors.add("Minimum notice of " + minNoticeDays + " days required");
            }
        }

        // Check documents
        if (Boolean.TRUE.equals(documentsRequired)) {
            // Documents required - will be checked separately
        }

        // Check negative balance
        if (Boolean.FALSE.equals(negativeBalanceAllowed)) {
            if (currentBalance != null && requestedDays > currentBalance) {
                errors.add("Insufficient leave balance. Available: " + currentBalance + ", Requested: " + requestedDays);
            }
        }

        // Check half day
        if (Boolean.FALSE.equals(halfDayAllowed) && requestedDays == 0.5) {
            errors.add("Half day leave not allowed for this leave type");
        }

        return errors;
    }

    /**
     * Calculate accrual for period
     */
    public Double calculateAccrual(Integer daysInPeriod) {
        if (accrualRate == null || accrualFrequency == null) {
            return 0.0;
        }

        if (daysInPeriod == null) {
            return 0.0;
        }

        return (accrualRate * daysInPeriod) / getDaysInAccrualPeriod();
    }

    /**
     * Get days in accrual period based on frequency
     */
    private Integer getDaysInAccrualPeriod() {
        return switch (accrualFrequency.toLowerCase()) {
            case "daily" -> 1;
            case "weekly" -> 7;
            case "biweekly" -> 14;
            case "monthly" -> 30;
            case "quarterly" -> 90;
            case "yearly" -> 365;
            default -> 30;
        };
    }

    /**
     * Check if sandwich rule applies
     */
    public boolean appliesSandwichRule() {
        return Boolean.TRUE.equals(sandwichRule);
    }

    /**
     * Check if holidays should be excluded
     */
    public boolean excludesHolidays() {
        return Boolean.TRUE.equals(excludeHolidays);
    }

    /**
     * Check if weekends should be excluded
     */
    public boolean excludesWeekends() {
        return Boolean.TRUE.equals(excludeWeekends);
    }

    /**
     * Get maximum encashable days
     */
    public Double getMaxEncashableDays(Double balance) {
        if (encashmentAllowed == null || balance == null) {
            return 0.0;
        }

        if (Boolean.TRUE.equals(isUnlimited)) {
            return balance;
        }

        return Math.min(encashmentAllowed, balance);
    }

    /**
     * Calculate encashment amount
     */
    public Double calculateEncashmentAmount(Double days, Double dailyRate) {
        if (days == null || dailyRate == null || encashmentPercentage == null) {
            return 0.0;
        }

        return days * dailyRate * (encashmentPercentage / 100.0);
    }

    public enum YearEndProcessing {
        LAPSE,
        CARRY_FORWARD,
        ENCASH,
        ROLL_OVER
    }
}
