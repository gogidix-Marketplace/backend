package com.gogidix.customersupport.customerportal.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketHistoryResponseDto {

    private String id;
    private String tenantId;
    private String customerId;
    private String ticketId;
    private String ticketNumber;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String category;
    private String channel;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant resolvedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant closedAt;

    private String assignedAgentId;
    private String assignedAgentName;
    private Integer satisfactionRating;
    private String feedback;
    private String resolutionNotes;
    private List<AttachmentInfoDto> attachments;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentInfoDto {
        private String fileName;
        private String fileUrl;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant uploadedAt;
    }
}
