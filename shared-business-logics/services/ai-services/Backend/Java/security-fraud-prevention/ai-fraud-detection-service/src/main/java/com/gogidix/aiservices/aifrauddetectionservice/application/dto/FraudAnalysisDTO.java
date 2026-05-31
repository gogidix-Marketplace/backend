package com.gogidix.aiservices.aifrauddetectionservice.application.dto;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;

/**
 * DTO for Fraud Analysis results.
 * Contains fraud detection analysis information.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FraudAnalysisDTO extends BaseDTO {

    @NotBlank(message = "Analysis ID is required")
    private String analysisId;

    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    private String userId;

    private String tenantId;

    @DecimalMin(value = "0.0", message = "Fraud score must be at least 0.0")
    @DecimalMax(value = "1.0", message = "Fraud score must be at most 1.0")
    private Double fraudScore;

    private RiskLevel riskLevel;

    private FraudAction recommendedAction;

    private List<String> reasons;

    private Instant timestamp;

    private String modelVersion;

    private String status;

    private String reviewedBy;

    private Instant reviewedAt;

    private String reviewNotes;
}
