package com.gogidix.dashboard.core.domain.port.in;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Input port command for calculating KPI values.
 */
@Data
@Builder
public class CalculateKPICommand {

    @NotBlank(message = "KPI code is required")
    private String kpiCode;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Map<String, Object> parameters;

    @Builder.Default
    private Boolean forceRecalculation = false;
}
