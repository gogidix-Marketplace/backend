package com.gogidix.dashboard.reporting.domain.port.out;

import com.gogidix.dashboard.reporting.domain.model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Report Repository Port
 * 
 * Output port for report persistence operations
 * Defines contract for report data storage and retrieval
 */
public interface ReportRepositoryPort {

    /**
     * Save a report
     */
    Report save(Report report);

    /**
     * Find report by ID
     */
    Optional<Report> findById(ReportId reportId);

    /**
     * Find reports by type
     */
    List<Report> findByType(ReportType type);

    /**
     * Find reports by domain
     */
    List<Report> findByDomain(String domain);

    /**
     * Find reports by status
     */
    List<Report> findByStatus(ReportStatus status);

    /**
     * Find reports created by user
     */
    List<Report> findByCreatedBy(String createdBy);

    /**
     * Find reports created in date range
     */
    List<Report> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find reports by name pattern
     */
    List<Report> findByNamePattern(String pattern);

    /**
     * Delete report
     */
    void delete(ReportId reportId);

    /**
     * Check if report exists
     */
    boolean exists(ReportId reportId);

    /**
     * Count reports by type
     */
    long countByType(ReportType type);

    /**
     * Count reports by status
     */
    long countByStatus(ReportStatus status);

    /**
     * Get recent reports (last 30 days)
     */
    List<Report> findRecentReports(int limit);

    /**
     * Get most generated report types
     */
    List<ReportTypeCount> getMostGeneratedTypes(int limit);

    /**
     * Get report generation statistics
     */
    ReportStatistics getStatistics();

    /**
     * Clean up old reports based on retention policy
     */
    void cleanupOldReports(LocalDateTime cutoffDate);

    // Supporting classes
    class ReportTypeCount {
        private final ReportType type;
        private final long count;

        public ReportTypeCount(ReportType type, long count) {
            this.type = type;
            this.count = count;
        }

        public ReportType getType() { return type; }
        public long getCount() { return count; }
    }

    class ReportStatistics {
        private final long totalReports;
        private final long completedReports;
        private final long failedReports;
        private final double averageGenerationTimeMinutes;
        private final LocalDateTime lastGenerated;

        public ReportStatistics(long totalReports, long completedReports, long failedReports, 
                              double averageGenerationTimeMinutes, LocalDateTime lastGenerated) {
            this.totalReports = totalReports;
            this.completedReports = completedReports;
            this.failedReports = failedReports;
            this.averageGenerationTimeMinutes = averageGenerationTimeMinutes;
            this.lastGenerated = lastGenerated;
        }

        public long getTotalReports() { return totalReports; }
        public long getCompletedReports() { return completedReports; }
        public long getFailedReports() { return failedReports; }
        public double getAverageGenerationTimeMinutes() { return averageGenerationTimeMinutes; }
        public LocalDateTime getLastGenerated() { return lastGenerated; }
        public double getSuccessRate() { 
            return totalReports > 0 ? (double) completedReports / totalReports * 100 : 0.0; 
        }
    }
}