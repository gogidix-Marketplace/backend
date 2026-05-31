package com.gogidix.hr.payroll.domain.model;

import com.gogidix.hr.payroll.domain.enums.SalaryFrequency;
import com.gogidix.hr.payroll.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Payroll Run
 * Represents a payroll run configuration and execution history
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PayrollRun extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String runId;
    private String runName;
    private RunType runType;
    private SalaryFrequency frequency;
    private YearMonth payrollPeriod;
    private LocalDate scheduledDate;
    private LocalDate actualRunDate;
    private String runStatus;
    private String initiatedBy;
    private String approvedBy;
    private LocalDate approvalDate;
    private String processedBy;
    private LocalDate processDate;
    private Integer totalEmployees;
    private Integer successfulEmployees;
    private Integer failedEmployees;
    private Integer pendingEmployees;
    private String batchId;
    private Boolean isRecurring;
    private String recurrencePattern;
    private LocalDate nextRunDate;
    private String notes;
    private List<String> processingSteps = new ArrayList<>();
    private List<String> completedSteps = new ArrayList<>();
    private List<RunError> errors = new ArrayList<>();
    private String payrollId;
    private Long processingStartTime;
    private Long processingEndTime;
    private Long totalProcessingTime;
    private String currency;
    private Boolean isFinalized;

    /**
     * Start payroll run
     */
    public void startRun(String userId) {
        this.runStatus = "IN_PROGRESS";
        this.actualRunDate = LocalDate.now();
        this.initiatedBy = userId;
        this.processingStartTime = System.currentTimeMillis();
    }

    /**
     * Complete processing step
     */
    public void completeStep(String step) {
        if (!completedSteps.contains(step)) {
            completedSteps.add(step);
        }
    }

    /**
     * Add error to run
     */
    public void addError(String employeeId, String errorCode, String errorMessage) {
        RunError error = RunError.builder()
                .employeeId(employeeId)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .timestamp(System.currentTimeMillis())
                .build();
        this.errors.add(error);
    }

    /**
     * Increment employee count
     */
    public void incrementSuccessfulCount() {
        this.successfulEmployees = (this.successfulEmployees == null ? 0 : this.successfulEmployees) + 1;
    }

    /**
     * Increment failed count
     */
    public void incrementFailedCount() {
        this.failedEmployees = (this.failedEmployees == null ? 0 : this.failedEmployees) + 1;
    }

    /**
     * Complete payroll run
     */
    public void completeRun(String userId) {
        this.runStatus = "COMPLETED";
        this.processedBy = userId;
        this.processDate = LocalDate.now();
        this.processingEndTime = System.currentTimeMillis();
        this.totalProcessingTime = this.processingEndTime - this.processingStartTime;
    }

    /**
     * Fail payroll run
     */
    public void failRun(String reason) {
        this.runStatus = "FAILED";
        this.processingEndTime = System.currentTimeMillis();
        this.totalProcessingTime = this.processingEndTime - this.processingStartTime;
        addError("SYSTEM", "RUN_FAILED", reason);
    }

    /**
     * Approve payroll run
     */
    public void approveRun(String userId) {
        this.approvedBy = userId;
        this.approvalDate = LocalDate.now();
        this.runStatus = "APPROVED";
    }

    /**
     * Check if run is completed
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(runStatus) || "FAILED".equals(runStatus);
    }

    /**
     * Check if run can be finalized
     */
    public boolean canFinalize() {
        return "COMPLETED".equals(runStatus) && !isFinalized
                && successfulEmployees.equals(totalEmployees);
    }

    /**
     * Get success percentage
     */
    public double getSuccessPercentage() {
        if (totalEmployees == null || totalEmployees == 0) {
            return 0.0;
        }
        return (successfulEmployees * 100.0) / totalEmployees;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RunError {
        private String employeeId;
        private String errorCode;
        private String errorMessage;
        private Long timestamp;
        private String stackTrace;
    }

    public enum RunType {
        REGULAR,
        OFF_CYCLE,
        BONUS,
        FINAL_SETTLEMENT,
        BACK_PAY,
        CORRECTION
    }
}
