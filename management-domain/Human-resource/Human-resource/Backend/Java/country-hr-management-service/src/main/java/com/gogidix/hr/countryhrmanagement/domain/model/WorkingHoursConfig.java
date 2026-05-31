package com.gogidix.hr.countryhrmanagement.domain.model;

import com.gogidix.hr.countryhrmanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Domain model for Working Hours Configuration
 * Represents working hours regulations for a country
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHoursConfig extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String configCode;
    private String configName;
    private String description;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private String workScheduleType;
    private Integer standardHoursPerDay;
    private Integer standardHoursPerWeek;
    private Integer standardDaysPerWeek;
    private List<DayOfWeek> standardWorkDays;
    private String workShiftPattern;
    private List<WorkShift> workShifts;
    private Integer maximumHoursPerDay;
    private Integer maximumHoursPerWeek;
    private Integer maximumOvertimeHoursPerDay;
    private Integer maximumOvertimeHoursPerWeek;
    private Integer maximumOvertimeHoursPerMonth;
    private Double overtimeRateMultiplier;
    private Double nightShiftRateMultiplier;
    private Double weekendRateMultiplier;
    private Double holidayRateMultiplier;
    private String restPeriodRules;
    private Integer minimumRestPeriodMinutes;
    private Integer minimumRestPeriodHoursPerDay;
    private Integer minimumRestPeriodHoursPerWeek;
    private String breakTimeRules;
    private Integer minimumBreakTimeMinutes;
    private Integer maximumContinuousWorkHours;
    private String nightShiftDefinition;
    private String nightShiftStartHour;
    private String nightShiftEndHour;
    private String weekendDefinition;
    private List<DayOfWeek> weekendDays;
    private List<PublicHoliday> publicHolidays;
    private String holidayPayRules;
    private Double holidayPayRate;
    private String vacationAccrualRules;
    private Integer vacationDaysPerYear;
    private Integer vacationAccrualMonthsRequired;
    private String sickLeaveRules;
    private Integer sickDaysPerYear;
    private Integer sickLeavePayRate;
    private String parentalLeaveRules;
    private Integer maternityLeaveDays;
    private Integer paternityLeaveDays;
    private Integer parentalLeaveDays;
    private String specialLeaveTypes;
    private Map<String, Integer> specialLeaveDays;
    private String flexibleWorkingArrangements;
    private Boolean telecommutingAllowed;
    private Boolean flexibleScheduleAllowed;
    private Boolean jobSharingAllowed;
    private String partTimeRules;
    private String compressedWorkWeekRules;
    private Integer minimumAgeForOvertime;
    private Integer maximumAgeForNightWork;
    private String prohibitedWorkTypesForMinors;
    private String additionalSafetyRequirements;
    private Map<String, String> customFields;
    private String complianceAuthority;
    private String complianceReporting;
    private Boolean isActive;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkShift {
        private String shiftCode;
        private String shiftName;
        private String startTime;
        private String endTime;
        private Double duration;
        private String shiftType;
        private Double rateMultiplier;
        private Boolean requiresApproval;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublicHoliday {
        private String holidayCode;
        private String holidayName;
        private LocalDate date;
        private Boolean isRecurring;
        private String recurrencePattern;
        private Boolean isPaid;
        private Double payRate;
        private String observanceRules;
    }

    public boolean isOvertimeAllowed(Double hoursWorked, Integer employeeAge) {
        boolean hoursCheck = hoursWorked != null && maximumOvertimeHoursPerDay != null &&
                standardHoursPerDay != null &&
                hoursWorked > standardHoursPerDay &&
                hoursWorked <= (standardHoursPerDay + maximumOvertimeHoursPerDay);

        boolean ageCheck = employeeAge == null || minimumAgeForOvertime == null ||
                employeeAge >= minimumAgeForOvertime;

        return hoursCheck && ageCheck;
    }

    public boolean isWeekend(LocalDate date) {
        if (date == null || weekendDays == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return weekendDays.contains(dayOfWeek);
    }

    public boolean isNightShift(String time) {
        if (time == null || nightShiftStartHour == null || nightShiftEndHour == null) {
            return false;
        }
        // Handle night shifts that cross midnight (e.g., 22:00 to 06:00)
        if (nightShiftStartHour.compareTo(nightShiftEndHour) > 0) {
            // Shift crosses midnight: time is in shift if it's >= start OR <= end (both inclusive)
            return time.compareTo(nightShiftStartHour) >= 0 ||
                    time.compareTo(nightShiftEndHour) <= 0;
        } else {
            // Normal shift within same day: time is in shift if it's >= start AND <= end
            return time.compareTo(nightShiftStartHour) >= 0 &&
                    time.compareTo(nightShiftEndHour) <= 0;
        }
    }

    public Double calculateOvertimeRate(Double baseRate, String shiftType) {
        if (baseRate == null) {
            return null;
        }

        if ("NIGHT".equals(shiftType)) {
            return nightShiftRateMultiplier != null
                    ? baseRate * nightShiftRateMultiplier
                    : baseRate;
        } else if ("WEEKEND".equals(shiftType)) {
            return weekendRateMultiplier != null
                    ? baseRate * weekendRateMultiplier
                    : baseRate;
        } else if (overtimeRateMultiplier != null) {
            return baseRate * overtimeRateMultiplier;
        }

        return baseRate;
    }

    public boolean isEffectiveOn(LocalDate date) {
        return date != null &&
                (effectiveDate == null || !date.isBefore(effectiveDate)) &&
                (expiryDate == null || !date.isAfter(expiryDate));
    }
}
