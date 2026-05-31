package com.gogidix.finance.ledger.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LedgerAccountEvent {

    private String accountId;
    private String tenantId;
    private String accountNumber;
    private String accountName;
    private Object accountType;
    private Object accountSubType;
    private String currency;
    private BigDecimal balance;
    private String reason;
    private String eventType;
    private LocalDateTime timestamp;
    private String eventId;
}
