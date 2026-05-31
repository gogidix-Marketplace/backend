package com.gogidix.aiservices.aifrauddetectionservice.application.command;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.FraudPatternDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * CQRS Command for adding a new fraud pattern.
 * Used to register detected fraud patterns in the system.
 */
@Getter
@Builder
@AllArgsConstructor
public class AddFraudPatternCommand implements Command<FraudPatternDTO> {

    @NotBlank(message = "Pattern type is required")
    private final String patternType;

    @NotBlank(message = "Description is required")
    private final String description;

    private final Double threshold;

    @NotNull(message = "Confidence score is required")
    @DecimalMin(value = "0.0", message = "Confidence score must be at least 0.0")
    @DecimalMax(value = "1.0", message = "Confidence score must be at most 1.0")
    private final Double confidenceScore;
}
