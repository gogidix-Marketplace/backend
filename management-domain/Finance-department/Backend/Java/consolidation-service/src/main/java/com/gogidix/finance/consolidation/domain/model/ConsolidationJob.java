package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Consolidation Job Domain Entity
 * Tracks consolidation job execution status and results
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "consolidation_jobs")
public class ConsolidationJob extends BaseEntity {

    @Indexed
    private String jobId;

    @Indexed
    private String tenantId;

    private String jobName;

    private String description;

    private JobType jobType;

    private JobStatus status;

    private Integer progress;

    private Integer totalSteps;

    private Integer completedSteps;

    private Instant startedAt;

    private Instant completedAt;

    private Instant estimatedCompletionAt;

    private String initiatedBy;

    private String ruleId;

    private String ruleName;

    private LocalDate periodStart;

    private LocalDate periodEnd;

    private List<String> includedSubsidiaries;

    private List<String> includedDepartments;

    private List<String> includedCostCenters;

    private String baseCurrency;

    private Map<String, Object> parameters;

    private JobResult result;

    private ErrorInfo errorInfo;

    private String parentJobId;

    private List<String> childJobIds;

    @Builder.Default
    private List<JobStep> steps = new ArrayList<>();

    @Builder.Default
    private List<JobLog> logs = new ArrayList<>();

    private Map<String, Object> metrics;

    private String correlationId;

    private Long processingTimeMs;

    private Integer recordsProcessed;

    private Integer recordsFailed;

    public enum JobType {
        FULL_CONSOLIDATION,
        PARTIAL_CONSOLIDATION,
        TRIAL_BALANCE,
        FINANCIAL_STATEMENT,
        INTERCOMPANY_ELIMINATION,
        CURRENCY_CONVERSION,
        ADJUSTMENT_POSTING,
        VALIDATION,
        RECONCILIATION,
        SCHEDULED,
        AD_HOC
    }

    public enum JobStatus {
        PENDING,
        QUEUED,
        RUNNING,
        PAUSED,
        COMPLETED,
        FAILED,
        CANCELLED,
        TIMEOUT,
        ROLLED_BACK
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobResult {
        private String resultId;
        private String reportId;
        private Boolean success;
        private String message;
        private Map<String, Object> summary;
        private List<String> warnings;
        private List<String> generatedReports;
        private Map<String, Object> consolidatedData;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorInfo {
        private String errorCode;
        private String errorMessage;
        private String errorType;
        private Instant errorTime;
        private String stackTrace;
        private String failedStep;
        private Map<String, Object> context;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobStep {
        private String stepId;
        private String stepName;
        private StepStatus status;
        private Instant startedAt;
        private Instant completedAt;
        private String message;
        private Map<String, Object> metadata;
    }

    public enum StepStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        SKIPPED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobLog {
        private Instant timestamp;
        private LogLevel level;
        private String message;
        private String step;
        private Map<String, Object> context;
    }

    public enum LogLevel {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    /**
     * Creates a new consolidation job
     */
    public static ConsolidationJob create(String tenantId, String jobName,
                                           JobType jobType, String initiatedBy,
                                           LocalDate periodStart, LocalDate periodEnd) {
        ConsolidationJob job = ConsolidationJob.builder()
            .jobId(generateJobId())
            .tenantId(tenantId)
            .jobName(jobName)
            .jobType(jobType)
            .status(JobStatus.PENDING)
            .progress(0)
            .totalSteps(0)
            .completedSteps(0)
            .initiatedBy(initiatedBy)
            .periodStart(periodStart)
            .periodEnd(periodEnd)
            .includedSubsidiaries(new ArrayList<>())
            .includedDepartments(new ArrayList<>())
            .includedCostCenters(new ArrayList<>())
            .parameters(new HashMap<>())
            .childJobIds(new ArrayList<>())
            .steps(new ArrayList<>())
            .logs(new ArrayList<>())
            .metrics(new HashMap<>())
            .recordsProcessed(0)
            .recordsFailed(0)
            .build();

        return job;
    }

    /**
     * Starts the job
     */
    public void start() {
        this.status = JobStatus.RUNNING;
        this.startedAt = Instant.now();
        addLog(LogLevel.INFO, "Job started", null);
    }

    /**
     * Completes the job successfully
     */
    public void complete(JobResult result) {
        this.status = JobStatus.COMPLETED;
        this.completedAt = Instant.now();
        this.progress = 100;
        this.result = result;

        if (this.startedAt != null) {
            this.processingTimeMs = java.time.Duration.between(startedAt, completedAt).toMillis();
        }

        addLog(LogLevel.INFO, "Job completed successfully", null);
    }

    /**
     * Fails the job
     */
    public void fail(String errorCode, String errorMessage, String failedStep) {
        this.status = JobStatus.FAILED;
        this.completedAt = Instant.now();

        ErrorInfo errorInfo = ErrorInfo.builder()
            .errorCode(errorCode)
            .errorMessage(errorMessage)
            .errorType("JobExecutionException")
            .errorTime(Instant.now())
            .failedStep(failedStep)
            .build();

        this.errorInfo = errorInfo;

        addLog(LogLevel.ERROR, "Job failed: " + errorMessage, failedStep);
    }

    /**
     * Pauses the job
     */
    public void pause() {
        if (this.status == JobStatus.RUNNING) {
            this.status = JobStatus.PAUSED;
            addLog(LogLevel.INFO, "Job paused", null);
        }
    }

    /**
     * Resumes the job
     */
    public void resume() {
        if (this.status == JobStatus.PAUSED) {
            this.status = JobStatus.RUNNING;
            addLog(LogLevel.INFO, "Job resumed", null);
        }
    }

    /**
     * Cancels the job
     */
    public void cancel() {
        if (this.status == JobStatus.RUNNING || this.status == JobStatus.PAUSED ||
            this.status == JobStatus.QUEUED || this.status == JobStatus.PENDING) {
            this.status = JobStatus.CANCELLED;
            this.completedAt = Instant.now();
            addLog(LogLevel.INFO, "Job cancelled", null);
        }
    }

    /**
     * Updates progress
     */
    public void updateProgress(Integer completedSteps) {
        this.completedSteps = completedSteps;
        if (this.totalSteps != null && this.totalSteps > 0) {
            this.progress = (completedSteps * 100) / this.totalSteps;
        }
    }

    /**
     * Adds a step to the job
     */
    public void addStep(String stepName, Map<String, Object> metadata) {
        JobStep step = JobStep.builder()
            .stepId(java.util.UUID.randomUUID().toString())
            .stepName(stepName)
            .status(StepStatus.PENDING)
            .metadata(metadata)
            .build();

        if (this.steps == null) {
            this.steps = new ArrayList<>();
        }
        this.steps.add(step);
        this.totalSteps = this.steps.size();
    }

    /**
     * Starts a step
     */
    public void startStep(String stepId) {
        this.steps.stream()
            .filter(s -> s.getStepId().equals(stepId))
            .findFirst()
            .ifPresent(step -> {
                step.setStatus(StepStatus.IN_PROGRESS);
                step.setStartedAt(Instant.now());
            });
    }

    /**
     * Completes a step
     */
    public void completeStep(String stepId, String message) {
        this.steps.stream()
            .filter(s -> s.getStepId().equals(stepId))
            .findFirst()
            .ifPresent(step -> {
                step.setStatus(StepStatus.COMPLETED);
                step.setCompletedAt(Instant.now());
                step.setMessage(message);
                this.completedSteps++;
                updateProgress(this.completedSteps);
            });
    }

    /**
     * Fails a step
     */
    public void failStep(String stepId, String errorMessage) {
        this.steps.stream()
            .filter(s -> s.getStepId().equals(stepId))
            .findFirst()
            .ifPresent(step -> {
                step.setStatus(StepStatus.FAILED);
                step.setCompletedAt(Instant.now());
                step.setMessage(errorMessage);
            });
    }

    /**
     * Adds a log entry
     */
    public void addLog(LogLevel level, String message, String step) {
        JobLog log = JobLog.builder()
            .timestamp(Instant.now())
            .level(level)
            .message(message)
            .step(step)
            .build();

        if (this.logs == null) {
            this.logs = new ArrayList<>();
        }
        this.logs.add(log);
    }

    /**
     * Adds a subsidiary to included subsidiaries
     */
    public void addSubsidiary(String subsidiaryId) {
        if (this.includedSubsidiaries == null) {
            this.includedSubsidiaries = new ArrayList<>();
        }
        if (!this.includedSubsidiaries.contains(subsidiaryId)) {
            this.includedSubsidiaries.add(subsidiaryId);
        }
    }

    /**
     * Adds a department to included departments
     */
    public void addDepartment(String departmentId) {
        if (this.includedDepartments == null) {
            this.includedDepartments = new ArrayList<>();
        }
        if (!this.includedDepartments.contains(departmentId)) {
            this.includedDepartments.add(departmentId);
        }
    }

    /**
     * Sets a parameter
     */
    public void setParameter(String key, Object value) {
        if (this.parameters == null) {
            this.parameters = new HashMap<>();
        }
        this.parameters.put(key, value);
    }

    /**
     * Sets a metric
     */
    public void setMetric(String key, Object value) {
        if (this.metrics == null) {
            this.metrics = new HashMap<>();
        }
        this.metrics.put(key, value);
    }

    /**
     * Increments records processed
     */
    public void incrementRecordsProcessed(int count) {
        this.recordsProcessed = (this.recordsProcessed != null ? this.recordsProcessed : 0) + count;
    }

    /**
     * Increments records failed
     */
    public void incrementRecordsFailed(int count) {
        this.recordsFailed = (this.recordsFailed != null ? this.recordsFailed : 0) + count;
    }

    /**
     * Calculates estimated completion time
     */
    public void calculateEstimatedCompletion() {
        if (this.startedAt != null && this.totalSteps != null && this.completedSteps != null &&
            this.completedSteps > 0 && this.totalSteps > this.completedSteps) {

            long elapsedMs = java.time.Duration.between(this.startedAt, Instant.now()).toMillis();
            long avgMsPerStep = elapsedMs / this.completedSteps;
            long remainingMs = avgMsPerStep * (this.totalSteps - this.completedSteps);
            this.estimatedCompletionAt = Instant.now().plusMillis(remainingMs);
        }
    }

    /**
     * Checks if job is in a terminal state
     */
    public boolean isTerminal() {
        return this.status == JobStatus.COMPLETED ||
               this.status == JobStatus.FAILED ||
               this.status == JobStatus.CANCELLED ||
               this.status == JobStatus.TIMEOUT ||
               this.status == JobStatus.ROLLED_BACK;
    }

    /**
     * Checks if job can be retried
     */
    public boolean canRetry() {
        return this.status == JobStatus.FAILED || this.status == JobStatus.TIMEOUT;
    }

    private static String generateJobId() {
        return "JOB-" + java.util.UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
}
