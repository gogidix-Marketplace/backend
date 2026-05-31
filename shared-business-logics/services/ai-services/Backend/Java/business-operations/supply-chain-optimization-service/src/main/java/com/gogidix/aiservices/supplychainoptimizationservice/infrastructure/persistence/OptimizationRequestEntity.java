package com.gogidix.aiservices.supplychainoptimizationservice.infrastructure.persistence;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationStatus;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class OptimizationRequestEntity {
    private String id;
    private UUID requestId;
    private String tenantId;
    private OptimizationType type;
    private Map<String, Object> parameters;
    private OptimizationStatus status;
    private int priority;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant completedAt;
    private String errorMessage;

    public OptimizationRequestEntity() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
