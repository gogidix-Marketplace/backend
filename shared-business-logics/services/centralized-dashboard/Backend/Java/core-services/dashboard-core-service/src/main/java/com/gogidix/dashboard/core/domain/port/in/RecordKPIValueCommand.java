package com.gogidix.dashboard.core.domain.port.in;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Input port command for recording a KPI value.
 */
@Data
@Builder
public class RecordKPIValueCommand {

    @NotNull(message = "KPI ID is required")
    private String kpiId;

    @NotNull(message = "Value is required")
    private Double value;

    private LocalDateTime recordedAt;

    private String metadata;
}
