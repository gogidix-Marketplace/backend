package com.gogidix.customersupport.ticketmanagement.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.customersupport.ticketmanagement.domain.model.Ticket;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class TicketRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Priority is required")
    private TicketPriorityDto priority;

    private String category;

    private String subCategory;

    private List<String> tags;

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;

    private String customerPhone;

    private String assignedAgentId;

    private String assignedAgentName;

    private String assignedTeam;

    @NotNull(message = "Channel is required")
    private TicketChannelDto channel;

    private String source;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant dueDate;

    private String slaPolicyId;

    private List<String> watchers;

    private List<String> relatedTicketIds;

    private String parentTicketId;

    public enum TicketPriorityDto {
        CRITICAL, HIGH, MEDIUM, LOW
    }

    public enum TicketChannelDto {
        EMAIL, PHONE, LIVE_CHAT, WEB_PORTAL, MOBILE_APP, SOCIAL_MEDIA, SMS, WHATSAPP, API
    }

    public Ticket.TicketPriority toEntityPriority() {
        return Ticket.TicketPriority.valueOf(priority.name());
    }

    public Ticket.TicketChannel toEntityChannel() {
        return Ticket.TicketChannel.valueOf(channel.name());
    }
}
