package com.gogidix.aiservices.aifrauddetectionservice.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for fraud analysis responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudAnalysisResponseDTO {

    private String analysisId;

    private String result;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private RiskLevel riskLevel;

    private Double confidenceScore;

    private List<String> fraudReasons;

    private Boolean actionRequired;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private String message;
}
