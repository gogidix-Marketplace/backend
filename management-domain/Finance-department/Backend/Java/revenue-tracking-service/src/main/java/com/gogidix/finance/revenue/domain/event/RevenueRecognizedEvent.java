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
public class RevenueRecognizedEvent {

    private String revenueId;
    private String tenantId;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private String type;
    private String reason;
    private String recognizedBy;
    private String milestoneId;
    private BigDecimal totalRecognized;
    private Integer periods;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
