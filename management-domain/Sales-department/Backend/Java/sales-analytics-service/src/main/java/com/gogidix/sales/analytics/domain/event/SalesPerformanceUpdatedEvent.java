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
public class SalesPerformanceUpdatedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private String entityType;
    private String entityId;
    private String entityName;
    private Instant periodStart;
    private Instant periodEnd;
    private BigDecimal totalRevenue;
    private BigDecimal quotaAchievement;
    private BigDecimal winRate;
    private Integer dealsWon;
    private Map<String, Object> metadata;
    private String userId;
    private Instant occurredAt;
    private String correlationId;

    public static SalesPerformanceUpdatedEvent create(String tenantId, String entityType, String entityId,
                                                       String entityName, Instant periodStart, Instant periodEnd,
                                                       BigDecimal totalRevenue, BigDecimal quotaAchievement,
                                                       BigDecimal winRate, Integer dealsWon,
                                                       Map<String, Object> metadata, String userId,
                                                       String eventType) {
        return SalesPerformanceUpdatedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType(eventType)
                .tenantId(tenantId)
                .entityType(entityType)
                .entityId(entityId)
                .entityName(entityName)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .totalRevenue(totalRevenue)
                .quotaAchievement(quotaAchievement)
                .winRate(winRate)
                .dealsWon(dealsWon)
                .metadata(metadata)
                .userId(userId)
                .occurredAt(Instant.now())
                .correlationId(java.util.UUID.randomUUID().toString())
                .build();
    }
}
