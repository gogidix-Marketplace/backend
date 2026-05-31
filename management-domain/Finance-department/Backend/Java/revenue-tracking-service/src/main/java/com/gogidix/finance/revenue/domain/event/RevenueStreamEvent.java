package com.gogidix.finance.revenue.domain.event;

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
public class RevenueStreamEvent {

    private String streamId;
    private String tenantId;
    private String streamName;
    private String type;
    private String customerId;
    private BigDecimal recurringAmount;
    private String currency;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
