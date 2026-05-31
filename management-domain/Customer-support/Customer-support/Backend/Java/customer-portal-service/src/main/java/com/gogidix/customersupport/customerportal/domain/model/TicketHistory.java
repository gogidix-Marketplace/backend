package com.gogidix.customersupport.customerportal.domain.model;

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
@Document(collection = "ticket_histories")
public class TicketHistory extends BaseEntity {

    @Field("customer_id")
    @Indexed
    private String customerId;

    @Field("ticket_id")
    @Indexed
    private String ticketId;

    @Field("ticket_number")
    private String ticketNumber;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("status")
    private String status;

    @Field("priority")
    private String priority;

    @Field("category")
    private String category;

    @Field("channel")
    private String channel;

    @Field("created_at")
    private Instant createdAt;

    @Field("resolved_at")
    private Instant resolvedAt;

    @Field("closed_at")
    private Instant closedAt;

    @Field("assigned_agent_id")
    private String assignedAgentId;

    @Field("assigned_agent_name")
    private String assignedAgentName;

    @Field("satisfaction_rating")
    private Integer satisfactionRating;

    @Field("feedback")
    private String feedback;

    @Field("resolution_notes")
    private String resolutionNotes;

    @Field("tags")
    private List<String> tags;

    @Field("attachments")
    private List<AttachmentInfo> attachments;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentInfo {
        private String fileName;
        private String fileUrl;
        private Instant uploadedAt;
    }
}
