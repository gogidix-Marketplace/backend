package com.gogidix.sales.dealmanagement.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealWonEvent {

    private String eventId;
    private String dealId;
    private String tenantId;
    private String dealName;
    private BigDecimal amount;
    private String currency;
    private String closedBy;
    private LocalDate closeDate;
    private String eventType;
    private Instant timestamp;
}
