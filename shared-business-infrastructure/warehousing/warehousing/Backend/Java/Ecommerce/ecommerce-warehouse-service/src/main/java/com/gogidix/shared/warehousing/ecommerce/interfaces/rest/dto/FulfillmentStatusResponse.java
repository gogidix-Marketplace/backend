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
public class FulfillmentStatusResponse {

    private String fulfillmentId;
    private String orderId;
    private String status;
    private String currentStage;
    private AssignedStaffInfo assignedStaff;
    private ProgressInfo progress;
    private List<StageDetail> stages;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignedStaffInfo {
        private String name;
        private String role;
        private LocalDateTime startedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProgressInfo {
        private Integer itemsPicked;
        private Integer totalItems;
        private Integer percentageComplete;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StageDetail {
        private String stage;
        private String status;
        private LocalDateTime completedAt;
        private LocalDateTime startedAt;
    }
}
