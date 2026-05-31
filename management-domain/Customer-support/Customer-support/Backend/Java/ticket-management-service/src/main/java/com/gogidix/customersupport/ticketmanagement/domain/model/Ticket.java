package com.gogidix.customersupport.ticketmanagement.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "tickets")
public class Ticket extends BaseEntity {

    @Field("ticket_number")
    @Indexed(unique = true)
    private String ticketNumber;

    @Field("title")
    @Indexed
    private String title;

    @Field("description")
    private String description;

    @Field("status")
    @Indexed
    private TicketStatus status;

    @Field("priority")
    @Indexed
    private TicketPriority priority;

    @Field("category")
    private String category;

    @Field("sub_category")
    private String subCategory;

    @Field("tags")
    private List<String> tags;

    @Field("customer_id")
    @Indexed
    private String customerId;

    @Field("customer_name")
    private String customerName;

    @Field("customer_email")
    private String customerEmail;

    @Field("customer_phone")
    private String customerPhone;

    @Field("assigned_agent_id")
    @Indexed
    private String assignedAgentId;

    @Field("assigned_agent_name")
    private String assignedAgentName;

    @Field("assigned_team")
    private String assignedTeam;

    @Field("channel")
    private TicketChannel channel;

    @Field("source")
    private String source;

    @Field("due_date")
    private Instant dueDate;

    @Field("resolved_at")
    private Instant resolvedAt;

    @Field("closed_at")
    private Instant closedAt;

    @Field("escalated_at")
    private Instant escalatedAt;

    @Field("escalation_reason")
    private String escalationReason;

    @Field("sla_policy_id")
    private String slaPolicyId;

    @Field("sla_due_date")
    private Instant slaDueDate;

    @Field("sla_breached")
    private Boolean slaBreached;

    @Field("first_response_at")
    private Instant firstResponseAt;

    @Field("resolution_notes")
    private String resolutionNotes;

    @Field("attachments")
    private List<Attachment> attachments;

    @Field("watchers")
    private List<String> watchers;

    @Field("related_ticket_ids")
    private List<String> relatedTicketIds;

    @Field("parent_ticket_id")
    private String parentTicketId;

    @Field("customer_rating")
    private Integer customerRating;

    @Field("customer_feedback")
    private String customerFeedback;

    @Field("reopened_count")
    private Integer reopenedCount;

    @Field("last_reopened_at")
    private Instant lastReopenedAt;

    public static Ticket create(String tenantId, String title, String description,
                                 TicketPriority priority, String customerId, String customerEmail,
                                 TicketChannel channel, String category) {
        Ticket ticket = new Ticket();
        ticket.setId(java.util.UUID.randomUUID().toString());
        ticket.setTenantId(tenantId);
        ticket.setTicketNumber(generateTicketNumber());
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(priority);
        ticket.setCustomerId(customerId);
        ticket.setCustomerEmail(customerEmail);
        ticket.setChannel(channel);
        ticket.setCategory(category);
        ticket.setSlaBreached(false);
        ticket.setReopenedCount(0);
        ticket.setCreatedAt(Instant.now());
        ticket.setUpdatedAt(Instant.now());
        return ticket;
    }

    private static String generateTicketNumber() {
        return "TKT-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    public void assignToAgent(String agentId, String agentName) {
        this.assignedAgentId = agentId;
        this.assignedAgentName = agentName;
        this.updateTimestamp();
    }

    public void assignToTeam(String team) {
        this.assignedTeam = team;
        this.updateTimestamp();
    }

    public void updateStatus(TicketStatus newStatus, String agentId) {
        this.status = newStatus;
        if (newStatus == TicketStatus.IN_PROGRESS && this.firstResponseAt == null) {
            this.firstResponseAt = Instant.now();
        }
        if (newStatus == TicketStatus.RESOLVED) {
            this.resolvedAt = Instant.now();
        }
        if (newStatus == TicketStatus.CLOSED) {
            this.closedAt = Instant.now();
        }
        if (newStatus == TicketStatus.ESCALATED) {
            this.escalatedAt = Instant.now();
        }
        this.updateTimestamp();
    }

    public void escalate(String reason) {
        this.status = TicketStatus.ESCALATED;
        this.escalatedAt = Instant.now();
        this.escalationReason = reason;
        this.updateTimestamp();
    }

    public void reopen() {
        this.status = TicketStatus.OPEN;
        this.reopenedCount = (this.reopenedCount != null ? this.reopenedCount : 0) + 1;
        this.lastReopenedAt = Instant.now();
        this.resolvedAt = null;
        this.closedAt = null;
        this.updateTimestamp();
    }

    public void addResolutionNotes(String notes) {
        this.resolutionNotes = notes;
        this.updateTimestamp();
    }

    public enum TicketStatus {
        OPEN, IN_PROGRESS, PENDING_CUSTOMER, RESOLVED, CLOSED, ESCALATED, ON_HOLD
    }

    public enum TicketPriority {
        CRITICAL, HIGH, MEDIUM, LOW
    }

    public enum TicketChannel {
        EMAIL, PHONE, LIVE_CHAT, WEB_PORTAL, MOBILE_APP, SOCIAL_MEDIA, SMS, WHATSAPP, API
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attachment {
        private String fileName;
        private String fileUrl;
        private String fileSize;
        private String contentType;
        private Instant uploadedAt;
        private String uploadedBy;
    }
}
