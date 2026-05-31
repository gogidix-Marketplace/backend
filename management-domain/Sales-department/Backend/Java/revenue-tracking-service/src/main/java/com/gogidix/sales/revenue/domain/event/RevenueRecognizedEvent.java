package com.gogidix.sales.revenue.domain.event;

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
    private String productId;
    private BigDecimal amount;
    private String currency;
    private String revenueType;
    private String negate;
    private BigDecimal recognizedAmount;
    private BigDecimal remainingAmount;
    private String eventType;
    private Instant timestamp;
}
