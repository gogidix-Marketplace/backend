package com.gogidix.dashboard.core.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for recording a KPI value.
 */
@Data
@Schema(description = "Request to record a value for a KPI")
public class RecordKPIValueRequestDto {

    @NotNull(message = "Value is required")
    @Schema(description = "The value to record", example = "1234.56")
    private Double value;

    @Schema(description = "When the value was recorded (defaults to now)")
    private LocalDateTime recordedAt;

    @Schema(description = "Additional metadata as JSON")
    private String metadata;
}
