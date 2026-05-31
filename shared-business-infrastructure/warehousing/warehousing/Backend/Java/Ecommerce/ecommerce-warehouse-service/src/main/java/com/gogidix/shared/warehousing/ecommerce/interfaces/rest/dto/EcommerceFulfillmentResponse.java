package com.gogidix.shared.warehousing.ecommerce.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EcommerceFulfillmentResponse {

    private String fulfillmentId;
    private String orderId;
    private String subOrderId;
    private String warehouseId;
    private String status;
    private LocalDateTime estimatedCompletion;
    private List<StageInfo> stages;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StageInfo {
        private String stage;
        private String status;
        private LocalDateTime completedAt;
    }
}
