package com.gogidix.dashboard.reporting.domain.port.out;

import com.gogidix.dashboard.reporting.domain.model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Report Scheduling Port
 * 
 * Output port for report scheduling operations
 * Defines contract for automated report generation scheduling
 */
public interface ReportSchedulingPort {

    /**
     * Schedule a report for automated generation
     */
    ScheduledReport schedule(ScheduledReport scheduledReport);

    /**
     * Update existing scheduled report
     */
    ScheduledReport update(ScheduledReport scheduledReport);

    /**
     * Find scheduled report by ID
     */
    Optional<ScheduledReport> findById(String scheduleId);

    /**
     * Get all scheduled reports
     */
    List<ScheduledReport> findAllScheduled();

    /**
     * Get scheduled reports by type
     */
    List<ScheduledReport> findByType(ReportType type);

    /**
     * Get scheduled reports by domain
     */
    List<ScheduledReport> findByDomain(String domain);

    /**
     * Get scheduled reports by creator
     */
    List<ScheduledReport> findByCreatedBy(String createdBy);

    /**
     * Get active scheduled reports
     */
    List<ScheduledReport> findActiveSchedules();

    /**
     * Get schedules due for execution
     */
    List<ScheduledReport> findDueSchedules(LocalDateTime currentTime);

    /**
     * Cancel scheduled report
     */
    void cancel(String scheduleId);

    /**
     * Pause scheduled report
     */
    void pause(String scheduleId);

    /**
     * Resume paused scheduled report
     */
    void resume(String scheduleId);

    /**
     * Update last execution time
     */
    void updateLastExecution(String scheduleId, LocalDateTime executionTime, boolean successful);

    /**
     * Get schedule execution history
     */
    List<ScheduleExecution> getExecutionHistory(String scheduleId, int limit);

    /**
     * Validate cron expression
     */
    boolean isValidCronExpression(String cronExpression);

    /**
     * Get next execution time for cron expression
     */
    LocalDateTime getNextExecutionTime(String cronExpression);

    /**
     * Get schedule health status
     */
    ScheduleHealthReport getHealthReport();

    // Supporting classes
    class ScheduleExecution {
        private final String scheduleId;
        private final LocalDateTime executionTime;
        private final boolean successful;
        private final String errorMessage;
        private final ReportId generatedReportId;

        public ScheduleExecution(String scheduleId, LocalDateTime executionTime, boolean successful,
                               String errorMessage, ReportId generatedReportId) {
            this.scheduleId = scheduleId;
            this.executionTime = executionTime;
            this.successful = successful;
            this.errorMessage = errorMessage;
            this.generatedReportId = generatedReportId;
        }

        public String getScheduleId() { return scheduleId; }
        public LocalDateTime getExecutionTime() { return executionTime; }
        public boolean isSuccessful() { return successful; }
        public String getErrorMessage() { return errorMessage; }
        public ReportId getGeneratedReportId() { return generatedReportId; }
    }

    class ScheduleHealthReport {
        private final int totalSchedules;
        private final int activeSchedules;
        private final int pausedSchedules;
        private final int failingSchedules;
        private final LocalDateTime lastCheckTime;

        public ScheduleHealthReport(int totalSchedules, int activeSchedules, int pausedSchedules,
                                  int failingSchedules, LocalDateTime lastCheckTime) {
            this.totalSchedules = totalSchedules;
            this.activeSchedules = activeSchedules;
            this.pausedSchedules = pausedSchedules;
            this.failingSchedules = failingSchedules;
            this.lastCheckTime = lastCheckTime;
        }

        public int getTotalSchedules() { return totalSchedules; }
        public int getActiveSchedules() { return activeSchedules; }
        public int getPausedSchedules() { return pausedSchedules; }
        public int getFailingSchedules() { return failingSchedules; }
        public LocalDateTime getLastCheckTime() { return lastCheckTime; }
        public double getHealthScore() {
            if (totalSchedules == 0) return 100.0;
            return (double) (activeSchedules - failingSchedules) / totalSchedules * 100.0;
        }
    }
}