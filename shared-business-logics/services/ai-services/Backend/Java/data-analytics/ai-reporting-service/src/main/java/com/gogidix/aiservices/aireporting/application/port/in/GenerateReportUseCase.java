package com.gogidix.aiservices.aireporting.application.port.in;

import com.gogidix.aiservices.aireporting.domain.Report;

import java.util.Optional;

/**
 * Use case interface for generating reports.
 */
public interface GenerateReportUseCase {

    /**
     * Generates a report based on the command.
     */
    Report generateReport(GenerateReportCommand command);

    /**
     * Gets report status by ID.
     */
    Optional<Report> getReportStatus(String reportId);

    /**
     * Gets download URL for a report.
     */
    String getDownloadUrl(String reportId);
}
