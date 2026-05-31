package com.gogidix.finance.ledger.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryEvent {

    private String journalEntryId;
    private String tenantId;
    private String eventType;
    private String createdBy;
    private LocalDate entryDate;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private String reversalReason;
    private String cancellationReason;
    private LocalDateTime timestamp;
    private String eventId;
}
