package com.gogidix.hr.leavemanagement.domain.model;

import com.gogidix.hr.leavemanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Holiday
 * Represents a public or company holiday
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Holiday extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String stateCode;
    private String holidayId;
    private String holidayName;
    private LocalDate holidayDate;
    private String holidayType;
    private Boolean isRecurring;
    private String recurringPattern;
    private String recurrenceRule;
    private Boolean isPaid;
    private Boolean isOptional;
    private String description;
    private String festival;
    private String religion;
    private Integer duration;
    private String durationUnit;
    private LocalDate observedFrom;
    private LocalDate observedTo;
    private String timezone;
    private String notes;
    private Boolean isActive;
    private Integer displayOrder;
    private String category;
    private Boolean appliesToAllLocations;
    private java.util.List<String> applicableLocations;
    private Boolean appliesToAllDepartments;
    private java.util.List<String> applicableDepartments;

    /**
     * Check if holiday falls on given date
     */
    public boolean isOnDate(LocalDate date) {
        if (holidayDate == null) {
            return false;
        }

        if (Boolean.TRUE.equals(isRecurring)) {
            // Check if date matches recurring pattern
            return date.getMonth() == holidayDate.getMonth() &&
                   date.getDayOfMonth() == holidayDate.getDayOfMonth();
        }

        return date.equals(holidayDate);
    }

    /**
     * Check if holiday falls within date range
     */
    public boolean isWithinRange(LocalDate startDate, LocalDate endDate) {
        if (holidayDate == null) {
            return false;
        }

        if (observedFrom != null && observedTo != null) {
            // Multi-day holiday
            return !endDate.isBefore(observedFrom) && !startDate.isAfter(observedTo);
        }

        return !endDate.isBefore(holidayDate) && !startDate.isAfter(holidayDate);
    }

    /**
     * Get effective date for observation
     */
    public LocalDate getEffectiveDate() {
        if (observedFrom != null) {
            return observedFrom;
        }
        return holidayDate;
    }

    /**
     * Check if holiday is applicable to location
     */
    public boolean isApplicableToLocation(String location) {
        if (Boolean.TRUE.equals(appliesToAllLocations)) {
            return true;
        }
        return applicableLocations != null && applicableLocations.contains(location);
    }

    /**
     * Check if holiday is applicable to department
     */
    public boolean isApplicableToDepartment(String department) {
        if (Boolean.TRUE.equals(appliesToAllDepartments)) {
            return true;
        }
        return applicableDepartments != null && applicableDepartments.contains(department);
    }
}
