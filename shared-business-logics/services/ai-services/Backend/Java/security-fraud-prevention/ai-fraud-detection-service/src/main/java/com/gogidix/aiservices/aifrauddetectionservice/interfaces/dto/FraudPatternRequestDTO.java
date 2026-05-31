package com.gogidix.aiservices.aifrauddetectionservice.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for fraud pattern requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudPatternRequestDTO {

    @JsonProperty("patternType")
    @NotBlank(message = "Pattern type is required")
    private String patternType;

    @JsonProperty("description")
    @NotBlank(message = "Description is required")
    private String description;

    @JsonProperty("threshold")
    @NotNull(message = "Threshold is required")
    private Double threshold;

    @JsonProperty("confidenceScore")
    private Double confidenceScore;

    @JsonProperty("isActive")
    private Boolean isActive;
}
