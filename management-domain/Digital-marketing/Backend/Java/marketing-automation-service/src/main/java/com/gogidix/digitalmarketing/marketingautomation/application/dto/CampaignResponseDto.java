package com.gogidix.digitalmarketing.marketingautomation.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignResponseDto {
    private String id;
    private String tenantId;
    private String name;
     private String type;
     private String status;
     private String triggerType;
     private String triggerCondition;
     private String startDate;
     private String endDate;
     private String isActive;

    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}