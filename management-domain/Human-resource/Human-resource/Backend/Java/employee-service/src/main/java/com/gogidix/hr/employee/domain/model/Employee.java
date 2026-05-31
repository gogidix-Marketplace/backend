package com.gogidix.hr.employee.domain.model;

import com.gogidix.hr.employee.domain.event.DomainEvent;
import com.gogidix.hr.employee.domain.event.EmployeeCreatedEvent;
import com.gogidix.hr.employee.domain.event.EmployeeTerminatedEvent;
import com.gogidix.hr.employee.shared.base.BaseEntity;
import com.gogidix.hr.employee.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Employee Domain Entity
 * Multi-tenant employee management with comprehensive HR data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "employees")
public class Employee extends BaseEntity {

    @Indexed(unique = true)
    private String employeeNumber;

    @Indexed
    private String personnelNumber;

    private String firstName;
    private String lastName;
    private String middleName;
    private String preferredName;

    @Indexed
    private String email;

    private String workEmail;
    private String personalEmail;
    private String phone;
    private String mobile;

    @Indexed
    private String countryCode;

    private String countryName;
    private String region;
    private String city;

    @Indexed
    private String department;

    @Indexed
    private String departmentId;

    private String position;

    @Indexed
    private String positionId;

    private String positionTitle;

    @Indexed
    private EmployeeStatus status;

    @Indexed
    private EmploymentType employmentType;

    @Indexed
    private EmployeeLevel level;

    private LocalDate hireDate;
    private LocalDate startDate;
    private LocalDate probationEndDate;
    private LocalDate terminationDate;

    private String terminationReason;
    private String terminationCategory;

    @Indexed
    private String managerId;

    private String managerName;

    @Builder.Default
    private List<String> directReportIds = new ArrayList<>();

    private String location;
    private String costCenter;
    private String currency;

    private Double salary;
    private Double hourlyRate;

    private SalaryFrequency salaryFrequency;

    private String workSchedule;
    private String workHours;
    private String timeZone;

    private String profileImage;
    private String avatar;
    private String bio;

    private String linkedInUrl;

    @Builder.Default
    private List<String> skills = new ArrayList<>();

    @Builder.Default
    private List<String> certifications = new ArrayList<>();

    @Builder.Default
    private List<String> languages = new ArrayList<>();

    private String educationLevel;
    private String degree;
    private String institution;

    private LocalDate birthDate;
    private String gender;
    private String maritalStatus;

    private String nationality;
    private String nationalId;
    private String passportNumber;
    private String passportExpiry;

    private String workPermitType;
    private String workPermitExpiry;

    private Boolean verified;
    private Boolean active;

    private LocalDate lastWorkingDay;
    private String exitReason;
    private String exitCategory;
    private String rehireEligibility;

    @Builder.Default
    private Map<String, Object> customFields = new HashMap<>();

    @Builder.Default
    private List<DomainEvent> domainEvents = new ArrayList<>();

    @Indexed
    private String tenantId;

    // Enums
    public enum EmployeeStatus {
        ACTIVE,
        INACTIVE,
        ON_LEAVE,
        TERMINATED,
        RESIGNED,
        PENDING_ONBOARDING,
        PENDING_TERMINATION
    }

    public enum EmploymentType {
        PERMANENT,
        CONTRACT,
        INTERN,
        CONSULTANT,
        TEMPORARY,
        FREELANCE
    }

    public enum EmployeeLevel {
        ENTRY,
        JUNIOR,
        MID_LEVEL,
        SENIOR,
        LEAD,
        MANAGER,
        DIRECTOR,
        VP,
        EXECUTIVE
    }

    public enum SalaryFrequency {
        HOURLY,
        DAILY,
        WEEKLY,
        BI_WEEKLY,
        MONTHLY,
        ANNUALLY
    }

    /**
     * Creates a new employee
     */
    public static Employee create(String tenantId, String firstName, String lastName,
                                   String email, String department, String position,
                                   EmployeeLevel level, EmploymentType employmentType,
                                   LocalDate hireDate, String hiredBy) {
        String employeeNumber = generateEmployeeNumber(tenantId);

        Employee employee = Employee.builder()
                .tenantId(tenantId)
                .employeeNumber(employeeNumber)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .department(department)
                .position(position)
                .level(level)
                .employmentType(employmentType)
                .hireDate(hireDate)
                .startDate(hireDate)
                .status(EmployeeStatus.PENDING_ONBOARDING)
                .active(true)
                .verified(false)
                .salaryFrequency(SalaryFrequency.MONTHLY)
                .workSchedule("FULL_TIME")
                .directReportIds(new ArrayList<>())
                .skills(new ArrayList<>())
                .certifications(new ArrayList<>())
                .languages(new ArrayList<>())
                .customFields(new HashMap<>())
                .domainEvents(new ArrayList<>())
                .build();

        employee.addDomainEvent(EmployeeCreatedEvent.builder()
                .employeeId(employee.getId())
                .tenantId(tenantId)
                .employeeNumber(employeeNumber)
                .employeeName(firstName + " " + lastName)
                .eventType("EMPLOYEE_CREATED")
                .build());

        return employee;
    }

    /**
     * Activates the employee
     */
    public void activate() {
        if (this.status != EmployeeStatus.PENDING_ONBOARDING) {
            throw new IllegalStateException("Can only activate pending onboarding employees");
        }
        this.status = EmployeeStatus.ACTIVE;
        this.active = true;
        this.verified = true;
    }

    /**
     * Deactivates the employee
     */
    public void deactivate() {
        if (this.status == EmployeeStatus.TERMINATED || this.status == EmployeeStatus.RESIGNED) {
            throw new IllegalStateException("Cannot deactivate terminated or resigned employees");
        }
        this.status = EmployeeStatus.INACTIVE;
        this.active = false;
    }

    /**
     * Puts the employee on leave
     */
    public void onLeave() {
        if (this.status != EmployeeStatus.ACTIVE) {
            throw new IllegalStateException("Can only put active employees on leave");
        }
        this.status = EmployeeStatus.ON_LEAVE;
    }

    /**
     * Returns from leave
     */
    public void returnFromLeave() {
        if (this.status != EmployeeStatus.ON_LEAVE) {
            throw new IllegalStateException("Employee is not on leave");
        }
        this.status = EmployeeStatus.ACTIVE;
    }

    /**
     * Terminates the employee
     */
    public void terminate(String reason, String category, LocalDate lastWorkingDay) {
        if (this.status == EmployeeStatus.TERMINATED || this.status == EmployeeStatus.RESIGNED) {
            throw new IllegalStateException("Employee is already terminated or resigned");
        }

        this.status = EmployeeStatus.TERMINATED;
        this.active = false;
        this.terminationReason = reason;
        this.terminationCategory = category;
        this.terminationDate = LocalDate.now();
        this.lastWorkingDay = lastWorkingDay;
        this.exitReason = reason;
        this.exitCategory = category;

        addDomainEvent(EmployeeTerminatedEvent.builder()
                .employeeId(this.getId())
                .tenantId(this.tenantId)
                .employeeNumber(this.employeeNumber)
                .employeeName(this.getFullName())
                .terminationReason(reason)
                .terminationCategory(category)
                .lastWorkingDay(lastWorkingDay)
                .eventType("EMPLOYEE_TERMINATED")
                .build());
    }

    /**
     * Processes resignation
     */
    public void resign(String reason, LocalDate lastWorkingDay, String rehireEligibility) {
        if (this.status == EmployeeStatus.TERMINATED || this.status == EmployeeStatus.RESIGNED) {
            throw new IllegalStateException("Employee is already terminated or resigned");
        }

        this.status = EmployeeStatus.RESIGNED;
        this.active = false;
        this.exitReason = reason;
        this.exitCategory = "RESIGNATION";
        this.terminationDate = LocalDate.now();
        this.lastWorkingDay = lastWorkingDay;
        this.rehireEligibility = rehireEligibility;

        addDomainEvent(EmployeeTerminatedEvent.builder()
                .employeeId(this.getId())
                .tenantId(this.tenantId)
                .employeeNumber(this.employeeNumber)
                .employeeName(this.getFullName())
                .terminationReason(reason)
                .terminationCategory("RESIGNATION")
                .lastWorkingDay(lastWorkingDay)
                .rehireEligible(rehireEligibility != null && rehireEligibility.equalsIgnoreCase("YES"))
                .eventType("EMPLOYEE_RESIGNED")
                .build());
    }

    /**
     * Promotes the employee
     */
    public void promote(EmployeeLevel newLevel, String newPosition, Double newSalary) {
        if (this.status != EmployeeStatus.ACTIVE) {
            throw new IllegalStateException("Can only promote active employees");
        }
        if (newLevel == null) {
            throw new ValidationException("level", "New level is required for promotion");
        }

        this.level = newLevel;
        if (newPosition != null) {
            this.position = newPosition;
        }
        if (newSalary != null) {
            this.salary = newSalary;
        }
    }

    /**
     * Transfers the employee to a new department
     */
    public void transfer(String newDepartmentId, String newDepartment,
                         String newPosition, String newManagerId) {
        if (this.status != EmployeeStatus.ACTIVE) {
            throw new IllegalStateException("Can only transfer active employees");
        }

        if (newDepartmentId != null) {
            this.departmentId = newDepartmentId;
        }
        if (newDepartment != null) {
            this.department = newDepartment;
        }
        if (newPosition != null) {
            this.position = newPosition;
        }
        if (newManagerId != null) {
            this.managerId = newManagerId;
        }
    }

    /**
     * Updates salary
     */
    public void updateSalary(Double newSalary, String reason) {
        if (this.status != EmployeeStatus.ACTIVE) {
            throw new IllegalStateException("Can only update salary for active employees");
        }
        if (newSalary == null || newSalary <= 0) {
            throw new ValidationException("salary", "Salary must be positive");
        }

        this.salary = newSalary;
    }

    /**
     * Sets hourly rate
     */
    public void setHourlyRate(Double rate, SalaryFrequency frequency) {
        if (rate == null || rate <= 0) {
            throw new ValidationException("hourlyRate", "Hourly rate must be positive");
        }

        this.hourlyRate = rate;
        if (frequency != null) {
            this.salaryFrequency = frequency;
        }
    }

    /**
     * Adds a direct report
     */
    public void addDirectReport(String employeeId) {
        if (this.directReportIds == null) {
            this.directReportIds = new ArrayList<>();
        }
        if (!this.directReportIds.contains(employeeId)) {
            this.directReportIds.add(employeeId);
        }
    }

    /**
     * Removes a direct report
     */
    public void removeDirectReport(String employeeId) {
        if (this.directReportIds != null) {
            this.directReportIds.remove(employeeId);
        }
    }

    /**
     * Adds a skill
     */
    public void addSkill(String skill) {
        if (this.skills == null) {
            this.skills = new ArrayList<>();
        }
        if (!this.skills.contains(skill)) {
            this.skills.add(skill);
        }
    }

    /**
     * Removes a skill
     */
    public void removeSkill(String skill) {
        if (this.skills != null) {
            this.skills.remove(skill);
        }
    }

    /**
     * Adds a certification
     */
    public void addCertification(String certification) {
        if (this.certifications == null) {
            this.certifications = new ArrayList<>();
        }
        if (!this.certifications.contains(certification)) {
            this.certifications.add(certification);
        }
    }

    /**
     * Removes a certification
     */
    public void removeCertification(String certification) {
        if (this.certifications != null) {
            this.certifications.remove(certification);
        }
    }

    /**
     * Adds a language
     */
    public void addLanguage(String language) {
        if (this.languages == null) {
            this.languages = new ArrayList<>();
        }
        if (!this.languages.contains(language)) {
            this.languages.add(language);
        }
    }

    /**
     * Sets custom field
     */
    public void setCustomField(String key, Object value) {
        if (this.customFields == null) {
            this.customFields = new HashMap<>();
        }
        this.customFields.put(key, value);
    }

    /**
     * Gets custom field
     */
    public Object getCustomField(String key) {
        if (this.customFields == null) {
            return null;
        }
        return this.customFields.get(key);
    }

    /**
     * Gets full name
     */
    public String getFullName() {
        StringBuilder name = new StringBuilder();
        if (firstName != null) {
            name.append(firstName);
        }
        if (middleName != null && !middleName.isBlank()) {
            name.append(" ").append(middleName);
        }
        if (lastName != null) {
            name.append(" ").append(lastName);
        }
        return name.toString().trim();
    }

    /**
     * Gets display name (preferred name or first name)
     */
    public String getDisplayName() {
        if (preferredName != null && !preferredName.isBlank()) {
            return preferredName + " " + (lastName != null ? lastName : "");
        }
        return getFullName();
    }

    /**
     * Gets years of service
     */
    public int getYearsOfService() {
        if (hireDate == null) {
            return 0;
        }
        LocalDate endDate = terminationDate != null ? terminationDate : LocalDate.now();
        return Period.between(hireDate, endDate).getYears();
    }

    /**
     * Gets months of service
     */
    public int getMonthsOfService() {
        if (hireDate == null) {
            return 0;
        }
        LocalDate endDate = terminationDate != null ? terminationDate : LocalDate.now();
        Period period = Period.between(hireDate, endDate);
        return period.getYears() * 12 + period.getMonths();
    }

    /**
     * Checks if employee is on probation
     */
    public boolean isOnProbation() {
        if (probationEndDate == null) {
            return false;
        }
        return LocalDate.now().isBefore(probationEndDate);
    }

    /**
     * Checks if employee's probation period has ended
     */
    public boolean hasProbationEnded() {
        if (probationEndDate == null) {
            return true;
        }
        return LocalDate.now().isAfter(probationEndDate);
    }

    /**
     * Gets age
     */
    public Integer getAge() {
        if (birthDate == null) {
            return null;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * Checks if work permit is valid
     */
    public boolean isWorkPermitValid() {
        if (workPermitExpiry == null) {
            return true;
        }
        try {
            LocalDate expiry = LocalDate.parse(workPermitExpiry);
            return LocalDate.now().isBefore(expiry);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if passport is valid
     */
    public boolean isPassportValid() {
        if (passportExpiry == null) {
            return true;
        }
        try {
            LocalDate expiry = LocalDate.parse(passportExpiry);
            return LocalDate.now().isBefore(expiry);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Generates employee number
     */
    private static String generateEmployeeNumber(String tenantId) {
        String prefix = "EMP";
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefix + "-" + uniqueId;
    }

    /**
     * Validates employee email
     */
    public boolean hasValidEmail() {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Gets work location
     */
    public String getWorkLocation() {
        List<String> parts = new ArrayList<>();
        if (location != null && !location.isBlank()) {
            parts.add(location);
        } else {
            if (city != null && !city.isBlank()) parts.add(city);
            if (region != null && !region.isBlank()) parts.add(region);
            if (countryName != null && !countryName.isBlank()) parts.add(countryName);
        }
        return String.join(", ", parts);
    }

    /**
     * Checks if can manage other employees
     */
    public boolean canManageOthers() {
        return level == EmployeeLevel.MANAGER ||
                level == EmployeeLevel.DIRECTOR ||
                level == EmployeeLevel.VP ||
                level == EmployeeLevel.EXECUTIVE;
    }

    /**
     * Gets compensation summary
     */
    public String getCompensationSummary() {
        if (salary != null) {
            return String.format("%s %.2f %s", currency != null ? currency : "USD",
                    salary, salaryFrequency != null ? salaryFrequency.name().toLowerCase() : "");
        } else if (hourlyRate != null) {
            return String.format("%s %.2f/hour", currency != null ? currency : "USD", hourlyRate);
        }
        return "Not specified";
    }

    /**
     * Checks if eligible for benefits
     */
    public boolean isEligibleForBenefits() {
        return employmentType == EmploymentType.PERMANENT &&
                (status == EmployeeStatus.ACTIVE || status == EmployeeStatus.ON_LEAVE) &&
                getMonthsOfService() >= 3;
    }

    public void addDomainEvent(DomainEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
