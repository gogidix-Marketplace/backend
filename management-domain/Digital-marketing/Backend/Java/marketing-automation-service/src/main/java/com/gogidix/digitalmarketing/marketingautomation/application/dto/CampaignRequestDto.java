package com.gogidix.digitalmarketing.marketingautomation.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignRequestDto {
    private String tenantId;
    private String name;
     private String type;
     private String status;
     private String triggerType;
     private String triggerCondition;
     private String startDate;
     private String endDate;
     private String isActive;

}