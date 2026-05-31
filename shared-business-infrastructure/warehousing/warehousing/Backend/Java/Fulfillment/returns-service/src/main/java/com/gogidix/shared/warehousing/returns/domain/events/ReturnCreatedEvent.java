package com.gogidix.shared.warehousing.returns.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnCreatedEvent {

    private String eventId;
    private String returnId;
    private String rmaNumber;
    private String orderNumber;
    private String customerId;
    private String reason;
    private Double refundAmount;
    private String tenantId;
    private LocalDateTime timestamp;

    public static ReturnCreatedEventBuilder builder() {
        return new ReturnCreatedEventBuilder()
            .eventId(java.util.UUID.randomUUID().toString())
            .timestamp(LocalDateTime.now());
    }
}
