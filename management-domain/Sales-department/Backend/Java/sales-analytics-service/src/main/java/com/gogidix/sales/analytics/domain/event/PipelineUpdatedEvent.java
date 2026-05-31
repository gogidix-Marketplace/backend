package com.gogidix.sales.analytics.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineUpdatedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private String entityType;
    private String entityId;
    private String entityName;
    private Instant periodStart;
    private Instant periodEnd;
    private BigDecimal totalPipelineValue;
    private BigDecimal pipelineVelocity;
    private Integer dealsWon;
    private BigDecimal pipelineCoverage;
    private String healthStatus;
    private Integer healthScore;
    private Map<String, Object> metadata;
    private Instant occurredAt;
    private String correlationId;

    public static PipelineUpdatedEvent create(String tenantId, String entityType, String entityId,
                                               String entityName, Instant periodStart, Instant periodEnd,
                                               BigDecimal totalPipelineValue, BigDecimal pipelineVelocity,
                                               Integer dealsWon, BigDecimal pipelineCoverage,
                                               String healthStatus, Integer healthScore,
                                               Map<String, Object> metadata) {
        return PipelineUpdatedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("PIPELINE_UPDATED")
                .tenantId(tenantId)
                .entityType(entityType)
                .entityId(entityId)
                .entityName(entityName)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .totalPipelineValue(totalPipelineValue)
                .pipelineVelocity(pipelineVelocity)
                .dealsWon(dealsWon)
                .pipelineCoverage(pipelineCoverage)
                .healthStatus(healthStatus)
                .healthScore(healthScore)
                .metadata(metadata)
                .occurredAt(Instant.now())
                .correlationId(java.util.UUID.randomUUID().toString())
                .build();
    }
}
