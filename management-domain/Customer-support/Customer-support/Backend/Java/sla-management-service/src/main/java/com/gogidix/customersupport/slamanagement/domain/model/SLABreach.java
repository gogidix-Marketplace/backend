package com.gogidix.customersupport.slamanagement.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "sla_breaches")
public class SLABreach extends BaseEntity {

    @Field("ticket_id")
    @Indexed
    private String ticketId;

    @Field("ticket_number")
    private String ticketNumber;

    @Field("sla_policy_id")
    @Indexed
    private String slaPolicyId;

    @Field("sla_policy_name")
    private String slaPolicyName;

    @Field("breach_type")
    @Indexed
    private BreachType breachType;

    @Field("breach_datetime")
    @Indexed
    private Instant breachDateTime;

    @Field("due_datetime")
    private Instant dueDateTime;

    @Field("actual_response_time_minutes")
    private Long actualResponseTimeMinutes;

    @Field("actual_resolution_time_minutes")
    private Long actualResolutionTimeMinutes;

    @Field("target_time_minutes")
    private Integer targetTimeMinutes;

    @Field("overdue_by_minutes")
    private Long overdueByMinutes;

    @Field("severity")
    private String severity;

    @Field("assigned_agent_id")
    private String assignedAgentId;

    @Field("assigned_agent_name")
    private String assignedAgentName;

    @Field("assigned_team")
    private String assignedTeam;

    @Field("customer_id")
    private String customerId;

    @Field("category")
    private String category;

    @Field("priority")
    private String priority;

    @Field("channel")
    private String channel;

    @Field("is_notified")
    private Boolean isNotified;

    @Field("notified_at")
    private Instant notifiedAt;

    @Field("escalation_triggered")
    private Boolean escalationTriggered;

    @Field("escalation_level")
    private Integer escalationLevel;

    @Field("resolution_notes")
    private String resolutionNotes;

    @Field("resolved_at")
    private Instant resolvedAt;

    @Field("impact_score")
    private Double impactScore;

    @Field("preventive_actions")
    private String preventiveActions;

    @Field("metadata")
    private Map<String, Object> metadata;

    public static SLABreach create(String tenantId, String ticketId, String ticketNumber,
                                    String slaPolicyId, String slaPolicyName, BreachType breachType,
                                    Instant breachDateTime, Instant dueDateTime, Integer targetTimeMinutes) {
        SLABreach breach = new SLABreach();
        breach.setId(java.util.UUID.randomUUID().toString());
        breach.setTenantId(tenantId);
        breach.setTicketId(ticketId);
        breach.setTicketNumber(ticketNumber);
        breach.setSlaPolicyId(slaPolicyId);
        breach.setSlaPolicyName(slaPolicyName);
        breach.setBreachType(breachType);
        breach.setBreachDateTime(breachDateTime);
        breach.setDueDateTime(dueDateTime);
        breach.setTargetTimeMinutes(targetTimeMinutes);
        breach.setIsNotified(false);
        breach.setEscalationTriggered(false);
        breach.setCreatedAt(Instant.now());
        breach.setUpdatedAt(Instant.now());
        return breach;
    }

    public void markAsNotified() {
        this.isNotified = true;
        this.notifiedAt = Instant.now();
        this.updateTimestamp();
    }

    public void triggerEscalation(Integer level) {
        this.escalationTriggered = true;
        this.escalationLevel = level;
        this.updateTimestamp();
    }

    public void resolve(String notes) {
        this.resolvedAt = Instant.now();
        this.resolutionNotes = notes;
        this.updateTimestamp();
    }

    public enum BreachType {
        RESPONSE_TIME, RESOLUTION_TIME
    }
}
