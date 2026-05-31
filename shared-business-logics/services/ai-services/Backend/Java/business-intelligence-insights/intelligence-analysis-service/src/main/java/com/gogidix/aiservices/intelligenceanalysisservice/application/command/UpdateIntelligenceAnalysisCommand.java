package com.gogidix.aiservices.intelligenceanalysisservice.application.command;

import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisCriteria;

/**
 * Command for updating an existing customer segment.
 */
public record UpdateIntelligenceAnalysisCommand(
        String segmentId,
        String tenantId,
        String userId,
        String name,
        String description,
        AnalysisCriteria criteria,
        String status
) {
    public UpdateIntelligenceAnalysisCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (name != null && name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
    }
}
