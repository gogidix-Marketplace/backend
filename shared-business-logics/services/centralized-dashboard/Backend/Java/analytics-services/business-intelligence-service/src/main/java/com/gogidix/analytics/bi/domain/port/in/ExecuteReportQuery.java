package com.gogidix.analytics.bi.domain.port.in;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Input port: Query to execute a report.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteReportQuery {

    @NotNull(message = "Report ID is required")
    private String reportId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Map<String, Object> parameters;

    private com.gogidix.analytics.bi.domain.model.ReportDefinition.OutputFormat outputFormat;

    private String requestedBy;

    @Builder.Default
    private Boolean async = true;
}
