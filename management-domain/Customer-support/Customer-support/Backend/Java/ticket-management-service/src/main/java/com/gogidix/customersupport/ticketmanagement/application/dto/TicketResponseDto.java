package com.gogidix.customersupport.ticketmanagement.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.customersupport.ticketmanagement.domain.model.Ticket;
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
public class TicketResponseDto {

    private String id;
    private String tenantId;
    private String ticketNumber;
    private String title;
    private String description;
    private TicketStatusDto status;
    private TicketPriorityDto priority;
    private String category;
    private String subCategory;
    private List<String> tags;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String assignedAgentId;
    private String assignedAgentName;
    private String assignedTeam;
    private TicketChannelDto channel;
    private String source;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant dueDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant resolvedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant closedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant escalatedAt;

    private String escalationReason;

    private String slaPolicyId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant slaDueDate;

    private Boolean slaBreached;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant firstResponseAt;

    private String resolutionNotes;

    private List<AttachmentDto> attachments;

    private List<String> watchers;

    private List<String> relatedTicketIds;

    private String parentTicketId;

    private Integer customerRating;

    private String customerFeedback;

    private Integer reopenedCount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant lastReopenedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum TicketStatusDto {
        OPEN, IN_PROGRESS, PENDING_CUSTOMER, RESOLVED, CLOSED, ESCALATED, ON_HOLD
    }

    public enum TicketPriorityDto {
        CRITICAL, HIGH, MEDIUM, LOW
    }

    public enum TicketChannelDto {
        EMAIL, PHONE, LIVE_CHAT, WEB_PORTAL, MOBILE_APP, SOCIAL_MEDIA, SMS, WHATSAPP, API
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentDto {
        private String fileName;
        private String fileUrl;
        private String fileSize;
        private String contentType;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant uploadedAt;

        private String uploadedBy;
    }

    public static TicketStatusDto fromEntityStatus(Ticket.TicketStatus status) {
        return TicketStatusDto.valueOf(status.name());
    }

    public static TicketPriorityDto fromEntityPriority(Ticket.TicketPriority priority) {
        return TicketPriorityDto.valueOf(priority.name());
    }

    public static TicketChannelDto fromEntityChannel(Ticket.TicketChannel channel) {
        return TicketChannelDto.valueOf(channel.name());
    }
}
