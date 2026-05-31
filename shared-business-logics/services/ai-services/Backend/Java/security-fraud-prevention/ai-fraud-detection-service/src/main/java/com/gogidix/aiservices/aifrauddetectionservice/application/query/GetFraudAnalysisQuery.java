package com.gogidix.aiservices.aifrauddetectionservice.application.query;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * CQRS Query for retrieving a specific fraud analysis.
 * Used to fetch fraud analysis details with optional history.
 */
@Getter
@Builder
@AllArgsConstructor
public class GetFraudAnalysisQuery implements Query<FraudAnalysisQueryResult> {

    @NotBlank(message = "Analysis ID is required")
    private final String analysisId;

    @Builder.Default
    private final boolean includeHistory = false;

    @Builder.Default
    private final boolean includeAuditTrail = false;
}
