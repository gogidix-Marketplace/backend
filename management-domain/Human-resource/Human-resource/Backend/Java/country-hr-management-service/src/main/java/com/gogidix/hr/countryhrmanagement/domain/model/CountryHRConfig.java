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
import java.util.Map;

/**
 * Domain model for Country HR Configuration
 * Represents HR settings specific to a country
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "country_hr_configs")
public class CountryHRConfig extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String countryName;
    private String currency;
    private String timeZone;
    private String dateFormat;
    private String numberFormat;
    private String defaultLanguage;
    private Map<String, String> supportedLanguages;
    private Integer standardWorkingHoursPerWeek;
    private Integer standardWorkingDaysPerWeek;
    private Integer minimumAnnualLeaveDays;
    private Integer maximumWorkingHoursPerDay;
    private Integer minimumNoticePeriodDays;
    private String overtimeCalculationMethod;
    private Boolean overtimeRequiresApproval;
    private String probationPeriodDuration;
    private Integer probationPeriodDays;
    private Boolean socialSecurityRequired;
    private Boolean healthInsuranceRequired;
    private Boolean unemploymentInsuranceRequired;
    private String retirementAge;
    private String minimumEmploymentAge;
    private LocalDate minimumWageEffectiveDate;
    private Double minimumWageHourly;
    private Double minimumWageMonthly;
    private String taxIdentificationPattern;
    private String nationalIdPattern;
    private Boolean electronicPayslipRequired;
    private String payslipDistributionMethod;
    private Integer payslipRetentionPeriodDays;
    private Map<String, String> customFields;
    private String laborLawCategory;
    private Boolean collectiveBargainingRequired;
    private String worksCouncilRequired;
    private Boolean dataPrivacyConsentRequired;
    private String gdprComplianceLevel;
    private LocalDateTime lastUpdated;
    private String lastUpdatedBy;
    private Boolean isActive;

    public boolean isCompliantWithWorkingHours(Double hoursWorked) {
        return hoursWorked != null &&
                hoursWorked <= maximumWorkingHoursPerDay &&
                standardWorkingHoursPerWeek != null;
    }

    public boolean isMinimumWageCompliant(Double hourlyWage) {
        return hourlyWage != null &&
                minimumWageHourly != null &&
                hourlyWage >= minimumWageHourly;
    }

    public boolean isWithinProbationPeriod(LocalDate hireDate, LocalDate currentDate) {
        return hireDate != null &&
                probationPeriodDays != null &&
                currentDate != null &&
                hireDate.plusDays(probationPeriodDays).isAfter(currentDate);
    }
}
