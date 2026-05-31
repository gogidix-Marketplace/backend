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
public class TicketUpdateRequestDto {

    private String title;

    private String description;

    private TicketStatusDto status;

    private TicketPriorityDto priority;

    private String category;

    private String subCategory;

    private List<String> tags;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private String assignedAgentId;

    private String assignedAgentName;

    private String assignedTeam;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant dueDate;

    private String escalationReason;

    private String resolutionNotes;

    private List<String> watchers;

    private List<String> relatedTicketIds;

    private String parentTicketId;

    private Integer customerRating;

    private String customerFeedback;

    public enum TicketStatusDto {
        OPEN, IN_PROGRESS, PENDING_CUSTOMER, RESOLVED, CLOSED, ESCALATED, ON_HOLD
    }

    public enum TicketPriorityDto {
        CRITICAL, HIGH, MEDIUM, LOW
    }

    public Ticket.TicketStatus toEntityStatus() {
        return status != null ? Ticket.TicketStatus.valueOf(status.name()) : null;
    }

    public Ticket.TicketPriority toEntityPriority() {
        return priority != null ? Ticket.TicketPriority.valueOf(priority.name()) : null;
    }
}
