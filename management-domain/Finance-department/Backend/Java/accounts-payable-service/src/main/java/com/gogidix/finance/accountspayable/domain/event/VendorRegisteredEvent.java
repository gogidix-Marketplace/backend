package com.gogidix.finance.accountspayable.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorRegisteredEvent {

    private String eventId;
    private String vendorId;
    private String tenantId;
    private String vendorCode;
    private String vendorName;
    private String vendorType;
    private Instant timestamp;
    private String eventType;
}
