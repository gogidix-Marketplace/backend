package com.gogidix.aiservices.aifrauddetectionservice.application.command;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.ThresholdDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * CQRS Command for updating fraud detection thresholds.
 * Used to modify risk threshold values and violation actions.
 */
@Getter
@Builder
@AllArgsConstructor
public class UpdateThresholdCommand implements Command<ThresholdDTO> {

    @NotBlank(message = "Threshold name is required")
    private final String thresholdName;

    private final Double minValue;

    private final Double maxValue;

    @NotBlank(message = "Action on violation is required")
    private final String actionOnViolation;
}
