package com.gogidix.aiservices.aireporting.application.port.in;

import com.gogidix.aiservices.aireporting.domain.model.ReportType;
import com.gogidix.aiservices.aireporting.domain.model.ExportFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Command for generating a report.
 */
public record GenerateReportCommand(
    ReportType type,
    ExportFormat format,
    LocalDateTime startDate,
    LocalDateTime endDate,
    List<String> includeMetrics,
    Map<String, Object> options
) {
    public GenerateReportCommand {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        if (format == null) {
            throw new IllegalArgumentException("format cannot be null");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("startDate cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("endDate cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate cannot be before startDate");
        }
    }
}
