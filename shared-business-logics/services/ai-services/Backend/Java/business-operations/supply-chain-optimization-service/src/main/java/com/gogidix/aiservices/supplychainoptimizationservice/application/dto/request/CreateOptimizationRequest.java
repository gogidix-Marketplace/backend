package com.gogidix.aiservices.supplychainoptimizationservice.application.dto.request;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record CreateOptimizationRequest(
        @NotBlank(message = "Tenant ID is required")
        String tenantId,

        @NotNull(message = "Optimization type is required")
        OptimizationType type,

        Map<String, Object> parameters,

        int priority
) {
    public CreateOptimizationRequest {
        if (priority == 0) {
            priority = 5;
        }
    }

    public static CreateOptimizationRequest of(String tenantId, OptimizationType type, Map<String, Object> parameters) {
        return new CreateOptimizationRequest(tenantId, type, parameters, 5);
    }

    public static CreateOptimizationRequest of(String tenantId, OptimizationType type, Map<String, Object> parameters, int priority) {
        return new CreateOptimizationRequest(tenantId, type, parameters, priority);
    }
}
