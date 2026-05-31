package com.gogidix.sales.territory.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotaUpdatedEvent {

    private String quotaId;
    private String tenantId;
    private String territoryId;
    private BigDecimal amount;
    private String currency;
    private BigDecimal oldAmount;
    private String adjustedBy;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
