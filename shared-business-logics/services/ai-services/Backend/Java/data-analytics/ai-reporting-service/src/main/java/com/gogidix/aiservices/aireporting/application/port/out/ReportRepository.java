package com.gogidix.aiservices.aireporting.application.port.out;

import com.gogidix.aiservices.aireporting.domain.Report;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for reports.
 */
public interface ReportRepository {

    /**
     * Saves a report.
     */
    Report save(Report report);

    /**
     * Finds a report by ID.
     */
    Optional<Report> findById(String reportId);

    /**
     * Deletes a report by ID.
     */
    void deleteById(String reportId);

    /**
     * Finds reports by status.
     */
    List<Report> findByStatus(Report.ReportStatus status);
}
