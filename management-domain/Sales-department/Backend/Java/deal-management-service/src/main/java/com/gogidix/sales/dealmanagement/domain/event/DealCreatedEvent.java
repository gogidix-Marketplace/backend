package com.gogidix.sales.dealmanagement.domain.event;

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
public class DealCreatedEvent {

    private String eventId;
    private String dealId;
    private String tenantId;
    private String dealName;
    private BigDecimal amount;
    private String currency;
    private String stage;
    private String ownerId;
    private String eventType;
    private Instant timestamp;
}
