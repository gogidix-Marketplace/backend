package com.gogidix.aiservices.aifrauddetectionservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

public class AddPatternRequest {
    @NotBlank(message = "Pattern name is required")
    private String patternName;

    @NotBlank(message = "Description is required")
    private String description;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private Double confidenceScore;

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }
}
