package com.gogidix.sales.crm.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interaction Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionResponseDto {

    private String id;
    private String interactionId;
    private String tenantId;
    private String customerId;
    private String customerName;
    private String contactId;
    private String contactName;
    private InteractionTypeDto type;
    private InteractionDirectionDto direction;
    private LocalDateTime interactionDate;
    private Integer durationMinutes;
    private String subject;
    private String description;
    private String outcome;
    private String notes;
    private String location;
    private Boolean hasFollowUp;
    private LocalDate followUpDate;
    private String followUpNotes;
    private String assignedTo;
    private String assignedToName;
    private InteractionStatusDto status;
    private String recordingUrl;
    private List<String> attachmentUrls;
    private List<String> participantContactIds;
    private String campaignId;
    private String dealId;
    private Double dealValue;
    private Integer probability;
    private String nextStep;
    private LocalDate nextStepDate;
    private Boolean isHighPriority;
    private String tags;
    private List<String> relatedInteractions;
    private Instant createdAt;
    private Instant updatedAt;

    public enum InteractionTypeDto {
        CALL,
        EMAIL,
        MEETING,
        SMS,
        CHAT,
        SOCIAL_MEDIA,
        WEBINAR,
        EVENT,
        NOTE
    }

    public enum InteractionDirectionDto {
        INBOUND,
        OUTBOUND
    }

    public enum InteractionStatusDto {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        NO_SHOW,
        RESCHEDULED
    }
}
