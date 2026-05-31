package com.gogidix.sales.leadmanagement.application.dto.response;

import com.gogidix.sales.leadmanagement.domain.model.LeadActivity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Lead Activity Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadActivityResponseDto {

    private String id;
    private String activityId;
    private String leadId;
    private String tenantId;

    private String activityType;
    private String subject;
    private String description;

    private String createdBy;
    private String createdByName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant dueDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant completedAt;

    private String priority;
    private String status;

    private Integer durationMinutes;
    private String outcome;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    /**
     * Converts LeadActivity entity to DTO
     */
    public static LeadActivityResponseDto fromEntity(LeadActivity activity) {
        return LeadActivityResponseDto.builder()
                .id(activity.getId())
                .activityId(activity.getActivityId())
                .leadId(activity.getLeadId())
                .tenantId(activity.getTenantId())
                .activityType(activity.getActivityType() != null ? activity.getActivityType().name() : null)
                .subject(activity.getSubject())
                .description(activity.getDescription())
                .createdBy(activity.getCreatedBy())
                .createdByName(activity.getCreatedByName())
                .dueDate(activity.getDueDate())
                .completedAt(activity.getCompletedAt())
                .priority(activity.getPriority() != null ? activity.getPriority().name() : null)
                .status(activity.getStatus() != null ? activity.getStatus().name() : null)
                .durationMinutes(activity.getDurationMinutes())
                .outcome(activity.getOutcome())
                .createdAt(activity.getCreatedAt())
                .updatedAt(activity.getUpdatedAt())
                .build();
    }
}
