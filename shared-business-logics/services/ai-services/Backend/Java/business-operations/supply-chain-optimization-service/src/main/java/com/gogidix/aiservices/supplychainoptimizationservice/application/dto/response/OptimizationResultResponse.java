package com.gogidix.aiservices.supplychainoptimizationservice.application.dto.response;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationMetric;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationRecommendation;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Builder
public record OptimizationResultResponse(
        String resultId,
        String requestId,
        OptimizationType type,
        List<OptimizationMetric> metrics,
        List<OptimizationRecommendation> recommendations,
        Map<String, Object> data,
        double confidenceScore,
        Instant generatedAt,
        String summary
) {
}
