package com.gogidix.customersupport.slamanagement.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.customersupport.slamanagement.domain.model.SLABreach;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SLABreachResponseDto {

    private String id;
    private String tenantId;
    private String ticketId;
    private String ticketNumber;
    private String slaPolicyId;
    private String slaPolicyName;
    private BreachTypeDto breachType;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant breachDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant dueDateTime;

    private Long actualResponseTimeMinutes;
    private Long actualResolutionTimeMinutes;
    private Integer targetTimeMinutes;
    private Long overdueByMinutes;
    private String severity;
    private String assignedAgentId;
    private String assignedAgentName;
    private String assignedTeam;
    private String customerId;
    private String category;
    private String priority;
    private String channel;
    private Boolean isNotified;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant notifiedAt;

    private Boolean escalationTriggered;
    private Integer escalationLevel;
    private String resolutionNotes;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant resolvedAt;

    private Double impactScore;
    private String preventiveActions;
    private Map<String, Object> metadata;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum BreachTypeDto {
        RESPONSE_TIME, RESOLUTION_TIME
    }

    public static BreachTypeDto fromEntityBreachType(SLABreach.BreachType breachType) {
        return BreachTypeDto.valueOf(breachType.name());
    }
}
