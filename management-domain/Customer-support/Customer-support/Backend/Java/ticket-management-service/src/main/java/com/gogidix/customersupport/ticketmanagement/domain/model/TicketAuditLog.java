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
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ticket_audit_logs")
public class TicketAuditLog extends BaseEntity {

    @Field("ticket_id")
    @Indexed
    private String ticketId;

    @Field("ticket_number")
    private String ticketNumber;

    @Field("action_type")
    @Indexed
    private ActionType actionType;

    @Field("field_changed")
    private String fieldChanged;

    @Field("old_value")
    private String oldValue;

    @Field("new_value")
    private String newValue;

    @Field("performed_by")
    private String performedBy;

    @Field("performed_by_role")
    private String performedByRole;

    @Field("performed_at")
    private Instant performedAt;

    @Field("additional_data")
    private Map<String, Object> additionalData;

    @Field("ip_address")
    private String ipAddress;

    @Field("user_agent")
    private String userAgent;

    public static TicketAuditLog create(String tenantId, String ticketId, String ticketNumber,
                                         ActionType actionType, String performedBy, String performedByRole) {
        TicketAuditLog log = new TicketAuditLog();
        log.setId(java.util.UUID.randomUUID().toString());
        log.setTenantId(tenantId);
        log.setTicketId(ticketId);
        log.setTicketNumber(ticketNumber);
        log.setActionType(actionType);
        log.setPerformedBy(performedBy);
        log.setPerformedByRole(performedByRole);
        log.setPerformedAt(Instant.now());
        log.setCreatedAt(Instant.now());
        log.setUpdatedAt(Instant.now());
        return log;
    }

    public enum ActionType {
        TICKET_CREATED,
        TICKET_UPDATED,
        TICKET_ASSIGNED,
        TICKET_REASSIGNED,
        STATUS_CHANGED,
        PRIORITY_CHANGED,
        TICKET_ESCALATED,
        TICKET_RESOLVED,
        TICKET_CLOSED,
        TICKET_REOPENED,
        COMMENT_ADDED,
        ATTACHMENT_ADDED,
        CUSTOMER_RESPONDED,
        AGENT_RESPONDED,
        NOTE_ADDED,
        TAG_ADDED,
        TAG_REMOVED
    }
}
