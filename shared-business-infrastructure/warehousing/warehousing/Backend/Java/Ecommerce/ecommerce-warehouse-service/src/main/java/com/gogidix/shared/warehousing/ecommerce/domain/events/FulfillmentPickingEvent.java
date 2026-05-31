package com.gogidix.shared.warehousing.ecommerce.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FulfillmentPickingEvent {
    private String fulfillmentId;
    private String orderId;
    private String subOrderId;
    private String staffId;
    private String staffName;
    private LocalDateTime startedAt;
}
