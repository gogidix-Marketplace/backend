package com.gogidix.hr.countryhrmanagement.domain.model;

import com.gogidix.hr.countryhrmanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Domain model for Labor Law
 * Represents labor law regulations for a country
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "labor_laws")
public class LaborLaw extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String lawCode;
    private String lawName;
    private String lawCategory;
    private String description;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private String authority;
    private String authoritativeSource;
    private String lastAmendedDate;
    private String nextReviewDate;
    private Boolean isMandatory;
    private String applicability;
    private List<String> coveredEmployerTypes;
    private List<String> coveredEmployeeTypes;
    private Integer minimumWorkingAge;
    private Integer maximumWorkingHoursPerDay;
    private Integer maximumWorkingHoursPerWeek;
    private Integer maximumOvertimeHoursPerDay;
    private Integer maximumOvertimeHoursPerWeek;
    private Double overtimeRateMultiplier;
    private Double nightShiftRateMultiplier;
    private Double weekendRateMultiplier;
    private Double holidayRateMultiplier;
    private Integer minimumRestPeriodMinutes;
    private Integer minimumAnnualLeaveDays;
    private Integer minimumSickLeaveDays;
    private Integer minimumMaternityLeaveDays;
    private Integer minimumPaternityLeaveDays;
    private Integer minimumParentalLeaveDays;
    private Integer minimumNoticePeriodDays;
    private String severancePayFormula;
    private String terminationNoticeFormat;
    private Boolean allowContractRenewal;
    private Integer maximumContractDurationDays;
    private String discriminationProtections;
    private String equalOpportunityRequirements;
    private String healthAndSafetyStandards;
    private String dataProtectionRequirements;
    private String privacyPolicyRequirements;
    private String grievanceProcedureRequirements;
    private String collectiveBargainingRequirements;
    private String unionRepresentationRights;
    private String strikeProcedures;
    private String lockoutProcedures;
    private Map<String, String> additionalProvisions;
    private String complianceCertificate;
    private LocalDate certificationDate;
    private Boolean isActive;
    private Integer penaltyAmount;
    private String penaltyDescription;
    private String enforcementAgency;

    public boolean isEffectiveOn(LocalDate date) {
        return date != null &&
                (effectiveDate == null || !date.isBefore(effectiveDate)) &&
                (expiryDate == null || !date.isAfter(expiryDate));
    }

    public Double calculateOvertimeRate(Double baseRate, Integer overtimeHours) {
        if (baseRate == null || overtimeRateMultiplier == null) {
            return baseRate;
        }
        return baseRate * overtimeRateMultiplier;
    }

    public boolean isValidForEmployment(String employmentType, Integer employeeAge) {
        boolean validType = coveredEmployeeTypes == null ||
                (employmentType != null && coveredEmployeeTypes.contains(employmentType));
        boolean validAge = employeeAge != null &&
                (minimumWorkingAge == null || employeeAge >= minimumWorkingAge);
        return validType && validAge;
    }
}
