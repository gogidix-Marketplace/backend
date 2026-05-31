package com.gogidix.finance.accountsreceivable.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegisteredEvent {

    private String customerId;
    private String tenantId;
    private String customerCode;
    private String customerName;
    private String customerType;
    private Instant timestamp;
    private String eventType;
}
