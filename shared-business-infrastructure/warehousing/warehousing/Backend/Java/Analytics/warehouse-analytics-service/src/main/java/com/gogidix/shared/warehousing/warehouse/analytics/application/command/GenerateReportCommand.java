package com.gogidix.shared.warehousing.warehouse.analytics.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Command to generate utilization report
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateReportCommand {

    @NotBlank(message = "Warehouse ID is required")
    private String warehouseId;

    private String warehouseName;

    @NotNull(message = "Period start is required")
    private LocalDateTime periodStart;

    @NotNull(message = "Period end is required")
    private LocalDateTime periodEnd;

    private List<String> zoneIds;

    private Boolean includeRecommendations;

    private String generatedBy;
}
