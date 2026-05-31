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
public class WinLossAnalysisEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private String entityType;
    private String entityId;
    private String entityName;
    private Instant periodStart;
    private Instant periodEnd;
    private BigDecimal winRate;
    private BigDecimal lossRate;
    private Integer dealsWon;
    private Integer dealsLost;
    private String primaryLossReason;
    private String topCompetitor;
    private BigDecimal winRateTrend;
    private Map<String, Object> metadata;
    private Instant occurredAt;
    private String correlationId;

    public static WinLossAnalysisEvent create(String tenantId, String entityType, String entityId,
                                               String entityName, Instant periodStart, Instant periodEnd,
                                               BigDecimal winRate, BigDecimal lossRate,
                                               Integer dealsWon, Integer dealsLost,
                                               String primaryLossReason, String topCompetitor,
                                               BigDecimal winRateTrend, Map<String, Object> metadata) {
        return WinLossAnalysisEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("WIN_LOSS_ANALYSIS")
                .tenantId(tenantId)
                .entityType(entityType)
                .entityId(entityId)
                .entityName(entityName)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .winRate(winRate)
                .lossRate(lossRate)
                .dealsWon(dealsWon)
                .dealsLost(dealsLost)
                .primaryLossReason(primaryLossReason)
                .topCompetitor(topCompetitor)
                .winRateTrend(winRateTrend)
                .metadata(metadata)
                .occurredAt(Instant.now())
                .correlationId(java.util.UUID.randomUUID().toString())
                .build();
    }
}
