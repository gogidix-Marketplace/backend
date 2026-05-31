package com.gogidix.analytics.bi.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Input port: Command to create a report definition.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReportCommand {

    @NotBlank(message = "Report name is required")
    private String reportName;

    private String description;

    @NotNull(message = "Report type is required")
    private com.gogidix.analytics.bi.domain.model.ReportDefinition.ReportType reportType;

    private com.gogidix.analytics.bi.domain.model.ReportDefinition.ScheduleType scheduleType;

    private String scheduleConfig;

    private String dataSource;

    private String queryDefinition;

    @Builder.Default
    private com.gogidix.analytics.bi.domain.model.ReportDefinition.OutputFormat outputFormat =
        com.gogidix.analytics.bi.domain.model.ReportDefinition.OutputFormat.PDF;

    private String templateConfig;

    private String recipients;

    @NotNull(message = "Owner ID is required")
    private String ownerId;

    @Builder.Default
    private Boolean enabled = true;
}
