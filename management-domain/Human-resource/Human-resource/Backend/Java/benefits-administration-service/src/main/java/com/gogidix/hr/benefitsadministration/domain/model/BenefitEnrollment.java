package com.gogidix.hr.benefitsadministration.domain.model;

import com.gogidix.hr.benefitsadministration.domain.enums.EnrollmentStatus;
import com.gogidix.hr.benefitsadministration.shared.base.BaseEntity;
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
 * Domain model for Benefit Enrollment
 * Represents an employee's enrollment in a benefit plan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BenefitEnrollment extends BaseEntity {

    private String tenantId;
    private String employeeId;
    private String planId;
    private String planName;
    private String coverageLevel;
    private String coverageOptionCode;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private EnrollmentStatus status;
    private List<DependentInfo> dependents = new ArrayList<>();
    private BigDecimal employeePremium;
    private BigDecimal employerPremium;
    private BigDecimal totalPremium;
    private String currency;
    private String notes;
    private Boolean waiveCoverage;
    private String waiverReason;
    private List<String> evidenceDocuments = new ArrayList<>();

    // Getters and Setters
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getPlanId() { return planId; }
    public void setPlanId(String planId) { this.planId = planId; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public String getCoverageLevel() { return coverageLevel; }
    public void setCoverageLevel(String coverageLevel) { this.coverageLevel = coverageLevel; }

    public String getCoverageOptionCode() { return coverageOptionCode; }
    public void setCoverageOptionCode(String coverageOptionCode) { this.coverageOptionCode = coverageOptionCode; }

    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }

    public List<DependentInfo> getDependents() { return dependents; }
    public void setDependents(List<DependentInfo> dependents) { this.dependents = dependents; }

    public BigDecimal getEmployeePremium() { return employeePremium; }
    public void setEmployeePremium(BigDecimal employeePremium) { this.employeePremium = employeePremium; }

    public BigDecimal getEmployerPremium() { return employerPremium; }
    public void setEmployerPremium(BigDecimal employerPremium) { this.employerPremium = employerPremium; }

    public BigDecimal getTotalPremium() { return totalPremium; }
    public void setTotalPremium(BigDecimal totalPremium) { this.totalPremium = totalPremium; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Boolean getWaiveCoverage() { return waiveCoverage; }
    public void setWaiveCoverage(Boolean waiveCoverage) { this.waiveCoverage = waiveCoverage; }

    public String getWaiverReason() { return waiverReason; }
    public void setWaiverReason(String waiverReason) { this.waiverReason = waiverReason; }

    public List<String> getEvidenceDocuments() { return evidenceDocuments; }
    public void setEvidenceDocuments(List<String> evidenceDocuments) { this.evidenceDocuments = evidenceDocuments; }

    public boolean isActive() {
        return status == EnrollmentStatus.ACTIVE &&
                (effectiveDate == null || !LocalDate.now().isBefore(effectiveDate)) &&
                (expiryDate == null || !LocalDate.now().isAfter(expiryDate));
    }

    public boolean isPending() {
        return status == EnrollmentStatus.PENDING;
    }

    public boolean canBeCancelled() {
        return status != EnrollmentStatus.CANCELLED &&
                status != EnrollmentStatus.TERMINATED &&
                (effectiveDate == null || effectiveDate.isAfter(LocalDate.now()));
    }

    public static class DependentInfo {
        private String id;
        private String firstName;
        private String lastName;
        private String relationship;
        private LocalDate dateOfBirth;
        private String gender;
        private String address;
        private Boolean isStudent;
        private Boolean isDisabled;
        private String ssnLast4;
        private LocalDate effectiveDate;
        private LocalDate endDate;
        private String notes;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getRelationship() { return relationship; }
        public void setRelationship(String relationship) { this.relationship = relationship; }

        public LocalDate getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public Boolean getIsStudent() { return isStudent; }
        public void setIsStudent(Boolean isStudent) { this.isStudent = isStudent; }

        public Boolean getIsDisabled() { return isDisabled; }
        public void setIsDisabled(Boolean isDisabled) { this.isDisabled = isDisabled; }

        public String getSsnLast4() { return ssnLast4; }
        public void setSsnLast4(String ssnLast4) { this.ssnLast4 = ssnLast4; }

        public LocalDate getEffectiveDate() { return effectiveDate; }
        public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }

        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}
