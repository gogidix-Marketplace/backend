package com.gogidix.shared.warehousing.ecommerce.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FulfillmentPackedEvent {
    private String fulfillmentId;
    private String orderId;
    private String subOrderId;
    private Double packageWeight;
    private Map<String, Double> packageDimensions;
    private LocalDateTime packedAt;
}
