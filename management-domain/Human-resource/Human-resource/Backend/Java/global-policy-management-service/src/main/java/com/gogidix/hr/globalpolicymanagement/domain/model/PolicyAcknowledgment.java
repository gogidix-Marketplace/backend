package com.gogidix.hr.globalpolicymanagement.domain.model;

import com.gogidix.hr.globalpolicymanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * PolicyAcknowledgment Domain Entity
 * Tracks employee acknowledgments of HR policies
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "policy_acknowledgments")
public class PolicyAcknowledgment extends BaseEntity {

    @Indexed(unique = true)
    private String acknowledgmentCode;

    @Indexed
    private String tenantId;

    @Indexed
    private String policyId;

    private String policyCode;
    private String policyName;
    private Integer policyVersion;

    @Indexed
    private String employeeId;

    private String employeeName;
    private String employeeEmail;

    @Indexed
    private AcknowledgmentStatus status;

    @Indexed
    private LocalDate sentDate;

    @Indexed
    private LocalDate dueDate;

    private LocalDate acknowledgedDate;
    private LocalDate reminderDate;

    @Indexed
    private String ipAddress;

    private String userAgent;
    private String deviceInfo;

    private Boolean isMandatory = false;

    private String comments;
    private String declineReason;

    @Indexed
    private String sentBy;

    private Integer reminderCount = 0;

    private String method; // EMAIL, PORTAL, IN_PERSON

    private Boolean legallyBinding;
    private String signature;
    private LocalDate signatureDate;

    public enum AcknowledgmentStatus {
        PENDING,
        SENT,
        ACKNOWLEDGED,
        DECLINED,
        EXPIRED,
        OVERDUE
    }

    /**
     * Creates a new policy acknowledgment
     */
    public static PolicyAcknowledgment create(String tenantId, String policyId, String policyCode,
                                               String policyName, Integer policyVersion,
                                               String employeeId, String employeeName, String employeeEmail,
                                               LocalDate dueDate, String sentBy) {
        String acknowledgmentCode = generateAcknowledgmentCode(policyCode, employeeId);

        PolicyAcknowledgment acknowledgment = new PolicyAcknowledgment();
        acknowledgment.setTenantId(tenantId);
        acknowledgment.setPolicyId(policyId);
        acknowledgment.setPolicyCode(policyCode);
        acknowledgment.setPolicyName(policyName);
        acknowledgment.setPolicyVersion(policyVersion);
        acknowledgment.setEmployeeId(employeeId);
        acknowledgment.setEmployeeName(employeeName);
        acknowledgment.setEmployeeEmail(employeeEmail);
        acknowledgment.setAcknowledgmentCode(acknowledgmentCode);
        acknowledgment.setStatus(AcknowledgmentStatus.PENDING);
        acknowledgment.setDueDate(dueDate);
        acknowledgment.setSentBy(sentBy);
        acknowledgment.setReminderCount(0);
        acknowledgment.setIsMandatory(false);

        return acknowledgment;
    }

    /**
     * Sends acknowledgment request
     */
    public void send(String method) {
        if (this.status != AcknowledgmentStatus.PENDING) {
            throw new IllegalStateException("Can only send pending acknowledgments");
        }
        this.status = AcknowledgmentStatus.SENT;
        this.sentDate = LocalDate.now();
        this.method = method;
    }

    /**
     * Acknowledges policy
     */
    public void acknowledge(String ipAddress, String userAgent, String comments) {
        if (this.status != AcknowledgmentStatus.SENT && this.status != AcknowledgmentStatus.OVERDUE) {
            throw new IllegalStateException("Can only acknowledge sent or overdue acknowledgments");
        }
        this.status = AcknowledgmentStatus.ACKNOWLEDGED;
        this.acknowledgedDate = LocalDate.now();
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.comments = comments;
    }

    /**
     * Declines policy
     */
    public void decline(String declineReason) {
        if (this.status != AcknowledgmentStatus.SENT && this.status != AcknowledgmentStatus.OVERDUE) {
            throw new IllegalStateException("Can only decline sent or overdue acknowledgments");
        }
        this.status = AcknowledgmentStatus.DECLINED;
        this.acknowledgedDate = LocalDate.now();
        this.declineReason = declineReason;
    }

    /**
     * Marks as overdue
     */
    public void markAsOverdue() {
        if (this.status == AcknowledgmentStatus.SENT && LocalDate.now().isAfter(this.dueDate)) {
            this.status = AcknowledgmentStatus.OVERDUE;
        }
    }

    /**
     * Marks as expired
     */
    public void markAsExpired() {
        if (this.status == AcknowledgmentStatus.SENT || this.status == AcknowledgmentStatus.OVERDUE) {
            this.status = AcknowledgmentStatus.EXPIRED;
        }
    }

    /**
     * Sends reminder
     */
    public void sendReminder() {
        if (this.status != AcknowledgmentStatus.SENT && this.status != AcknowledgmentStatus.OVERDUE) {
            throw new IllegalStateException("Can only send reminder for sent or overdue acknowledgments");
        }
        this.reminderDate = LocalDate.now();
        this.reminderCount = (this.reminderCount != null ? this.reminderCount : 0) + 1;
    }

    /**
     * Checks if acknowledgment is overdue
     */
    public boolean isOverdue() {
        return this.status == AcknowledgmentStatus.OVERDUE ||
               (this.status == AcknowledgmentStatus.SENT && LocalDate.now().isAfter(this.dueDate));
    }

    /**
     * Checks if acknowledgment is complete
     */
    public boolean isComplete() {
        return this.status == AcknowledgmentStatus.ACKNOWLEDGED ||
               this.status == AcknowledgmentStatus.DECLINED;
    }

    /**
     * Generates acknowledgment code
     */
    private static String generateAcknowledgmentCode(String policyCode, String employeeId) {
        return "ACK-" + policyCode + "-" + employeeId.substring(0, 8);
    }

    private List<Object> domainEvents = new ArrayList<>();

    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
