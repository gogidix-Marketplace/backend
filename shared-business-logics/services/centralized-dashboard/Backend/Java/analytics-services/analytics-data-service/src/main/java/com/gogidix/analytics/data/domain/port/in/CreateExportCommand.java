package com.gogidix.analytics.data.domain.port.in;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Input port: Command to create a data export.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExportCommand {

    private String exportName;

    private String queryId;

    private String queryDefinition;

    @NotNull(message = "Export format is required")
    private com.gogidix.analytics.data.domain.model.DataExport.ExportFormat exportFormat;

    @Builder.Default
    private com.gogidix.analytics.data.domain.model.DataExport.CompressionType compressionType =
        com.gogidix.analytics.data.domain.model.DataExport.CompressionType.NONE;

    @Builder.Default
    private Boolean includeHeaders = true;

    private Map<String, Object> filters;

    private String requestedBy;

    private Integer expiresInHours;
}
