package com.gogidix.finance.currency.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyActivatedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private Instant timestamp;

    public CurrencyActivatedEvent(String arg1, String arg2, String arg3) {
        this.eventId = arg1;
        this.eventType = arg2;
        this.tenantId = arg3;
    }
}
