package com.gogidix.digitalmarketing.marketingautomation.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "automation_campaigns")
public class Campaign {

    @Id
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

    public Campaign(String tenantId, String name , String type , String status , String triggerType , String triggerCondition , String startDate , String endDate , String isActive) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.name = name;
         this.type = type;
         this.status = status;
         this.triggerType = triggerType;
         this.triggerCondition = triggerCondition;
         this.startDate = startDate;
         this.endDate = endDate;
         this.isActive = isActive;

        this.status = "ACTIVE";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}