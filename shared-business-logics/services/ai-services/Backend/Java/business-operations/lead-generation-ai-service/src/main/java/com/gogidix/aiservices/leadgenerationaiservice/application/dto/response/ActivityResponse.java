package com.gogidix.aiservices.leadgenerationaiservice.application.dto.response;

import com.gogidix.aiservices.leadgenerationaiservice.domain.model.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {
    private String activityId;
    private String leadId;
    private ActivityType type;
    private String description;
    private Instant timestamp;
}
