package com.gogidix.customersupport.livechat.domain.model;

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
@Document(collection = "chat_sessions")
public class ChatSession extends BaseEntity {

    @Field("session_id")
    @Indexed(unique = true)
    private String sessionId;

    @Field("customer_id")
    @Indexed
    private String customerId;

    @Field("customer_name")
    private String customerName;

    @Field("customer_email")
    private String customerEmail;

    @Field("assigned_agent_id")
    @Indexed
    private String assignedAgentId;

    @Field("assigned_agent_name")
    private String assignedAgentName;

    @Field("status")
    @Indexed
    private ChatStatus status;

    @Field("channel")
    private String channel;

    @Field("queue_position")
    private Integer queuePosition;

    @Field("started_at")
    private Instant startedAt;

    @Field("ended_at")
    private Instant endedAt;

    @Field("duration_seconds")
    private Long durationSeconds;

    @Field("first_response_at")
    private Instant firstResponseAt;

    @Field("waiting_time_seconds")
    private Long waitingTimeSeconds;

    @Field("rating")
    private Integer rating;

    @Field("feedback")
    private String feedback;

    @Field("tags")
    private List<String> tags;

    @Field("category")
    private String category;

    @Field("related_ticket_id")
    private String relatedTicketId;

    @Field("message_count")
    private Integer messageCount;

    @Field("customer_message_count")
    private Integer customerMessageCount;

    @Field("agent_message_count")
    private Integer agentMessageCount;

    @Field("ip_address")
    private String ipAddress;

    @Field("user_agent")
    private String userAgent;

    @Field("referrer")
    private String referrer;

    @Field("location")
    private String location;

    @Field("pre_chat_survey_data")
    private PreChatSurveyData preChatSurveyData;

    public static ChatSession create(String tenantId, String customerId, String customerName, String customerEmail) {
        ChatSession session = new ChatSession();
        session.setId(java.util.UUID.randomUUID().toString());
        session.setTenantId(tenantId);
        session.setSessionId(java.util.UUID.randomUUID().toString());
        session.setCustomerId(customerId);
        session.setCustomerName(customerName);
        session.setCustomerEmail(customerEmail);
        session.setStatus(ChatStatus.WAITING);
        session.setQueuePosition(0);
        session.setStartedAt(Instant.now());
        session.setMessageCount(0);
        session.setCustomerMessageCount(0);
        session.setAgentMessageCount(0);
        session.setCreatedAt(Instant.now());
        session.setUpdatedAt(Instant.now());
        return session;
    }

    public void assignToAgent(String agentId, String agentName) {
        this.assignedAgentId = agentId;
        this.assignedAgentName = agentName;
        this.status = ChatStatus.ACTIVE;
        this.firstResponseAt = Instant.now();
        this.waitingTimeSeconds = java.time.Duration.between(this.startedAt, Instant.now()).getSeconds();
        this.updateTimestamp();
    }

    public void endChat() {
        this.status = ChatStatus.ENDED;
        this.endedAt = Instant.now();
        if (this.startedAt != null) {
            this.durationSeconds = java.time.Duration.between(this.startedAt, this.endedAt).getSeconds();
        }
        this.updateTimestamp();
    }

    public void addCustomerMessage() {
        this.messageCount = (this.messageCount != null ? this.messageCount : 0) + 1;
        this.customerMessageCount = (this.customerMessageCount != null ? this.customerMessageCount : 0) + 1;
        this.updateTimestamp();
    }

    public void addAgentMessage() {
        this.messageCount = (this.messageCount != null ? this.messageCount : 0) + 1;
        this.agentMessageCount = (this.agentMessageCount != null ? this.agentMessageCount : 0) + 1;
        this.updateTimestamp();
    }

    public enum ChatStatus {
        WAITING, ACTIVE, ENDED, TRANSFERRED, TIMED_OUT
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreChatSurveyData {
        private String question1;
        private String answer1;
        private String question2;
        private String answer2;
        private String question3;
        private String answer3;
    }
}
