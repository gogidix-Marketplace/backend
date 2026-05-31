package com.gogidix.aiservices.supplychainoptimizationservice.application.dto.response;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationStatus;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationType;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record OptimizationRequestResponse(
        String requestId,
        String tenantId,
        OptimizationType type,
        OptimizationStatus status,
        Map<String, Object> parameters,
        int priority,
        Instant createdAt,
        Instant updatedAt,
        Instant completedAt,
        String errorMessage
) {
}
