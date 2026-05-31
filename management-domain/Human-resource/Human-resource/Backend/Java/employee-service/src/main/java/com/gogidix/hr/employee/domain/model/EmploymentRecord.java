package com.gogidix.hr.employee.domain.model;

import com.gogidix.hr.employee.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Employment Record Domain Entity
 * Tracks all employment events and changes throughout employee lifecycle
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "employment_records")
public class EmploymentRecord extends BaseEntity {

    @Indexed
    private String tenantId;

    @Indexed
    private String employeeId;

    @Indexed
    private String eventType;

    private LocalDate eventDate;

    private String eventReason;

    // Previous values
    private String previousValue;
    private String newValue;

    private String previousDepartment;
    private String newDepartment;

    private String previousPosition;
    private String newPosition;

    private Double previousSalary;
    private Double newSalary;

    private String previousStatus;
    private String newStatus;

    @Indexed
    private String approvedBy;

    private String comments;

    @Builder.Default
    private List<String> supportingDocuments = new ArrayList<>();

    // Event Types
    public static final String EVENT_HIRE = "HIRE";
    public static final String EVENT_TERMINATION = "TERMINATION";
    public static final String EVENT_PROMOTION = "PROMOTION";
    public static final String EVENT_DEMOTION = "DEMOTE";
    public static final String EVENT_TRANSFER = "TRANSFER";
    public static final String EVENT_SALARY_CHANGE = "SALARY_CHANGE";
    public static final String EVENT_STATUS_CHANGE = "STATUS_CHANGE";
    public static final String EVENT_DEPARTMENT_CHANGE = "DEPARTMENT_CHANGE";
    public static final String EVENT_POSITION_CHANGE = "POSITION_CHANGE";
    public static final String EVENT_MANAGER_CHANGE = "MANAGER_CHANGE";
    public static final String EVENT_LEAVING_RETURNING = "LEAVING_RETURNING";
    public static final String EVENT_PROBATION_COMPLETION = "PROBATION_COMPLETION";
    public static final String EVENT_CONTRACT_RENEWAL = "CONTRACT_RENEWAL";
    public static final String EVENT_BENEFIT_ENROLLMENT = "BENEFIT_ENROLLMENT";
    public static final String EVENT_BENEFIT_TERMINATION = "BENEFIT_TERMINATION";

    /**
     * Creates an initial record for a new employee
     */
    public static EmploymentRecord createInitialRecord(String employeeId, String tenantId,
                                                       String department, String position,
                                                       Employee.EmployeeLevel level, LocalDate hireDate) {
        return EmploymentRecord.builder()
                .tenantId(tenantId)
                .employeeId(employeeId)
                .eventType(EVENT_HIRE)
                .eventDate(hireDate)
                .newDepartment(department)
                .newPosition(position)
                .newValue(level != null ? level.name() : null)
                .newStatus(Employee.EmployeeStatus.PENDING_ONBOARDING.name())
                .comments("Initial employment record created")
                .supportingDocuments(new ArrayList<>())
                .build();
    }

    /**
     * Creates a hire record
     */
    public static EmploymentRecord createHireRecord(String tenantId, String employeeId,
                                                     LocalDate hireDate, String position,
                                                     String department, Double salary,
                                                     String approvedBy) {
        return EmploymentRecord.builder()
                .tenantId(tenantId)
                .employeeId(employeeId)
                .eventType(EVENT_HIRE)
                .eventDate(hireDate)
                .newValue(position)
                .newDepartment(department)
                .newSalary(salary)
                .newStatus(Employee.EmployeeStatus.PENDING_ONBOARDING.name())
                .approvedBy(approvedBy)
                .comments("Employee hired")
                .supportingDocuments(new ArrayList<>())
                .build();
    }

    /**
     * Creates a termination record
     */
    public static EmploymentRecord createTerminationRecord(String tenantId, String employeeId,
                                                           LocalDate terminationDate, String reason,
                                                           String category, String approvedBy) {
        return EmploymentRecord.builder()
                .tenantId(tenantId)
                .employeeId(employeeId)
                .eventType(EVENT_TERMINATION)
                .eventDate(terminationDate)
                .eventReason(reason)
                .newValue(category)
                .newStatus(Employee.EmployeeStatus.TERMINATED.name())
                .approvedBy(approvedBy)
                .comments("Employee terminated: " + reason)
                .supportingDocuments(new ArrayList<>())
                .build();
    }

    /**
     * Creates a promotion record
     */
    public static EmploymentRecord createPromotionRecord(String tenantId, String employeeId,
                                                         LocalDate eventDate, String previousPosition,
                                                         String newPosition, Employee.EmployeeLevel previousLevel,
                                                         Employee.EmployeeLevel newLevel, Double previousSalary,
                                                         Double newSalary, String reason, String approvedBy) {
        StringBuilder comments = new StringBuilder("Promotion");
        if (previousLevel != null && newLevel != null) {
            comments.append(" from ").append(previousLevel).append(" to ").append(newLevel);
        }

        return EmploymentRecord.builder()
                .tenantId(tenantId)
                .employeeId(employeeId)
                .eventType(EVENT_PROMOTION)
                .eventDate(eventDate)
                .eventReason(reason)
                .previousValue(previousLevel != null ? previousLevel.name() : null)
                .newValue(newLevel != null ? newLevel.name() : null)
                .previousPosition(previousPosition)
                .newPosition(newPosition)
                .previousSalary(previousSalary)
                .newSalary(newSalary)
                .approvedBy(approvedBy)
                .comments(comments.toString())
                .supportingDocuments(new ArrayList<>())
                .build();
    }

    /**
     * Creates a transfer record
     */
    public static EmploymentRecord createTransferRecord(String tenantId, String employeeId,
                                                         LocalDate eventDate, String previousDepartment,
                                                         String newDepartment, String previousPosition,
                                                         String newPosition, String reason, String approvedBy) {
        return EmploymentRecord.builder()
                .tenantId(tenantId)
                .employeeId(employeeId)
                .eventType(EVENT_TRANSFER)
                .eventDate(eventDate)
                .eventReason(reason)
                .previousDepartment(previousDepartment)
                .newDepartment(newDepartment)
                .previousPosition(previousPosition)
                .newPosition(newPosition)
                .approvedBy(approvedBy)
                .comments("Transfer initiated")
                .supportingDocuments(new ArrayList<>())
                .build();
    }

    /**
     * Creates a salary change record
     */
    public static EmploymentRecord createSalaryChangeRecord(String tenantId, String employeeId,
                                                            LocalDate eventDate, Double previousSalary,
                                                            Double newSalary, String reason, String approvedBy) {
        double percentageChange = 0.0;
        if (previousSalary != null && newSalary != null && previousSalary > 0) {
            percentageChange = ((newSalary - previousSalary) / previousSalary) * 100;
        }

        String changeType = percentageChange > 0 ? "Increase" : percentageChange < 0 ? "Decrease" : "No Change";

        return EmploymentRecord.builder()
                .tenantId(tenantId)
                .employeeId(employeeId)
                .eventType(EVENT_SALARY_CHANGE)
                .eventDate(eventDate)
                .eventReason(reason)
                .previousSalary(previousSalary)
                .newSalary(newSalary)
                .newValue(String.format("%.2f%% %s", Math.abs(percentageChange), changeType))
                .approvedBy(approvedBy)
                .comments("Salary " + changeType.toLowerCase())
                .supportingDocuments(new ArrayList<>())
                .build();
    }

    /**
     * Creates a status change record
     */
    public static EmploymentRecord createStatusChangeRecord(String tenantId, String employeeId,
                                                            LocalDate eventDate, String previousStatus,
                                                            String newStatus, String reason, String approvedBy) {
        return EmploymentRecord.builder()
                .tenantId(tenantId)
                .employeeId(employeeId)
                .eventType(EVENT_STATUS_CHANGE)
                .eventDate(eventDate)
                .eventReason(reason)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .approvedBy(approvedBy)
                .comments("Status changed from " + previousStatus + " to " + newStatus)
                .supportingDocuments(new ArrayList<>())
                .build();
    }

    /**
     * Creates a department change record
     */
    public static EmploymentRecord createDepartmentChangeRecord(String tenantId, String employeeId,
                                                                 LocalDate eventDate, String previousDepartment,
                                                                 String newDepartment, String reason, String approvedBy) {
        return EmploymentRecord.builder()
                .tenantId(tenantId)
                .employeeId(employeeId)
                .eventType(EVENT_DEPARTMENT_CHANGE)
                .eventDate(eventDate)
                .eventReason(reason)
                .previousDepartment(previousDepartment)
                .newDepartment(newDepartment)
                .approvedBy(approvedBy)
                .comments("Department transfer")
                .supportingDocuments(new ArrayList<>())
                .build();
    }

    /**
     * Adds supporting document
     */
    public void addSupportingDocument(String documentUrl) {
        if (this.supportingDocuments == null) {
            this.supportingDocuments = new ArrayList<>();
        }
        if (!this.supportingDocuments.contains(documentUrl)) {
            this.supportingDocuments.add(documentUrl);
        }
    }

    /**
     * Removes supporting document
     */
    public void removeSupportingDocument(String documentUrl) {
        if (this.supportingDocuments != null) {
            this.supportingDocuments.remove(documentUrl);
        }
    }

    /**
     * Gets salary change percentage
     */
    public Double getSalaryChangePercentage() {
        if (previousSalary == null || newSalary == null || previousSalary == 0) {
            return null;
        }
        return ((newSalary - previousSalary) / previousSalary) * 100;
    }

    /**
     * Gets salary change amount
     */
    public Double getSalaryChangeAmount() {
        if (previousSalary == null || newSalary == null) {
            return null;
        }
        return newSalary - previousSalary;
    }

    /**
     * Checks if is salary increase
     */
    public boolean isSalaryIncrease() {
        Double change = getSalaryChangeAmount();
        return change != null && change > 0;
    }

    /**
     * Checks if is positive event (promotion, salary increase, etc.)
     */
    public boolean isPositiveEvent() {
        return EVENT_PROMOTION.equals(eventType) ||
                EVENT_HIRE.equals(eventType) ||
                (EVENT_SALARY_CHANGE.equals(eventType) && isSalaryIncrease()) ||
                EVENT_PROBATION_COMPLETION.equals(eventType) ||
                EVENT_CONTRACT_RENEWAL.equals(eventType) ||
                EVENT_BENEFIT_ENROLLMENT.equals(eventType);
    }

    /**
     * Checks if is negative event (termination, demotion, etc.)
     */
    public boolean isNegativeEvent() {
        return EVENT_TERMINATION.equals(eventType) ||
                EVENT_DEMOTION.equals(eventType) ||
                (EVENT_SALARY_CHANGE.equals(eventType) && !isSalaryIncrease()) ||
                EVENT_BENEFIT_TERMINATION.equals(eventType);
    }

    /**
     * Gets event description
     */
    public String getEventDescription() {
        if (comments != null && !comments.isBlank()) {
            return comments;
        }

        switch (eventType) {
            case EVENT_HIRE:
                return "Employee hired";
            case EVENT_TERMINATION:
                return "Employee terminated";
            case EVENT_PROMOTION:
                return "Employee promoted";
            case EVENT_DEMOTION:
                return "Employee demoted";
            case EVENT_TRANSFER:
                return "Employee transferred";
            case EVENT_SALARY_CHANGE:
                return "Salary changed";
            case EVENT_STATUS_CHANGE:
                return "Employment status changed";
            case EVENT_DEPARTMENT_CHANGE:
                return "Department changed";
            case EVENT_POSITION_CHANGE:
                return "Position changed";
            case EVENT_MANAGER_CHANGE:
                return "Manager changed";
            case EVENT_CONTRACT_RENEWAL:
                return "Contract renewed";
            default:
                return "Employment event recorded";
        }
    }
}
