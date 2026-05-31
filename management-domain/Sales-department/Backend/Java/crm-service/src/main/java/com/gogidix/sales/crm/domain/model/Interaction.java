package com.gogidix.sales.crm.domain.model;

import com.gogidix.sales.crm.domain.event.InteractionLoggedEvent;
import com.gogidix.sales.crm.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Interaction Domain Entity
 * Tracks all customer interactions (calls, emails, meetings, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "interactions")
public class Interaction extends BaseEntity {

    private String interactionId;

    private String tenantId;

    private String customerId;

    private String customerName;

    private String contactId;

    private String contactName;

    private InteractionType type;

    private InteractionDirection direction;

    private LocalDateTime interactionDate;

    private Integer durationMinutes;

    private String subject;

    private String description;

    private String outcome;

    private String notes;

    private String location;

    private Boolean hasFollowUp;

    private LocalDate followUpDate;

    private String followUpNotes;

    private String assignedTo;

    private String assignedToName;

    private InteractionStatus status;

    private String recordingUrl;

    private List<String> attachmentUrls;

    private List<String> participantContactIds;

    private String campaignId;

    private String dealId;

    private Double dealValue;

    private Integer probability;

    private String nextStep;

    private LocalDate nextStepDate;

    private Boolean isHighPriority;

    private String tags;

    private List<String> relatedInteractions;

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    public enum InteractionType {
        CALL,
        EMAIL,
        MEETING,
        SMS,
        CHAT,
        SOCIAL_MEDIA,
        WEBINAR,
        EVENT,
        NOTE
    }

    public enum InteractionDirection {
        INBOUND,
        OUTBOUND
    }

    public enum InteractionStatus {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        NO_SHOW,
        RESCHEDULED
    }

    /**
     * Creates a new interaction
     */
    public static Interaction create(String tenantId, String customerId, String customerName,
                                      String contactId, String contactName,
                                      InteractionType type, InteractionDirection direction,
                                      LocalDateTime interactionDate, String subject) {
        Interaction interaction = Interaction.builder()
            .tenantId(tenantId)
            .customerId(customerId)
            .customerName(customerName)
            .contactId(contactId)
            .contactName(contactName)
            .type(type)
            .direction(direction)
            .interactionDate(interactionDate)
            .subject(subject)
            .status(InteractionStatus.SCHEDULED)
            .hasFollowUp(false)
            .isHighPriority(false)
            .attachmentUrls(new ArrayList<>())
            .participantContactIds(new ArrayList<>())
            .relatedInteractions(new ArrayList<>())
            .build();

        interaction.addDomainEvent(InteractionLoggedEvent.builder()
            .interactionId(interaction.getInteractionId())
            .tenantId(tenantId)
            .customerId(customerId)
            .contactId(contactId)
            .type(type.name())
            .direction(direction.name())
            .timestamp(Instant.now())
            .eventType("INTERACTION_CREATED")
            .build());

        return interaction;
    }

    /**
     * Completes the interaction
     */
    public void complete(String outcome, String notes, Integer durationMinutes) {
        if (this.status == InteractionStatus.COMPLETED) {
            throw new IllegalStateException("Interaction is already completed");
        }

        this.status = InteractionStatus.COMPLETED;
        this.outcome = outcome;
        if (notes != null) {
            this.notes = notes;
        }
        if (durationMinutes != null) {
            this.durationMinutes = durationMinutes;
        }

        addDomainEvent(InteractionLoggedEvent.builder()
            .interactionId(this.interactionId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .contactId(this.contactId)
            .type(this.type.name())
            .direction(this.direction.name())
            .outcome(outcome)
            .timestamp(Instant.now())
            .eventType("INTERACTION_COMPLETED")
            .build());
    }

    /**
     * Cancels the interaction
     */
    public void cancel(String reason) {
        if (this.status == InteractionStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed interaction");
        }

        this.status = InteractionStatus.CANCELLED;
        this.notes = reason;

        addDomainEvent(InteractionLoggedEvent.builder()
            .interactionId(this.interactionId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .type(this.type.name())
            .direction(this.direction.name())
            .outcome("CANCELLED")
            .timestamp(Instant.now())
            .eventType("INTERACTION_CANCELLED")
            .build());
    }

    /**
     * Reschedules the interaction
     */
    public void reschedule(LocalDateTime newDate, String reason) {
        if (this.status == InteractionStatus.COMPLETED) {
            throw new IllegalStateException("Cannot reschedule completed interaction");
        }

        LocalDateTime oldDate = this.interactionDate;
        this.interactionDate = newDate;
        this.status = InteractionStatus.RESCHEDULED;
        if (reason != null) {
            this.notes = reason;
        }

        addDomainEvent(InteractionLoggedEvent.builder()
            .interactionId(this.interactionId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .type(this.type.name())
            .direction(this.direction.name())
            .outcome("RESCHEDULED")
            .timestamp(Instant.now())
            .eventType("INTERACTION_RESCHEDULED")
            .build());
    }

    /**
     * Sets follow-up information
     */
    public void setFollowUp(LocalDate followUpDate, String followUpNotes) {
        this.hasFollowUp = true;
        this.followUpDate = followUpDate;
        this.followUpNotes = followUpNotes;
    }

    /**
     * Removes follow-up
     */
    public void removeFollowUp() {
        this.hasFollowUp = false;
        this.followUpDate = null;
        this.followUpNotes = null;
    }

    /**
     * Adds a participant
     */
    public void addParticipant(String contactId) {
        if (this.participantContactIds == null) {
            this.participantContactIds = new ArrayList<>();
        }
        if (!this.participantContactIds.contains(contactId)) {
            this.participantContactIds.add(contactId);
        }
    }

    /**
     * Adds an attachment
     */
    public void addAttachment(String attachmentUrl) {
        if (this.attachmentUrls == null) {
            this.attachmentUrls = new ArrayList<>();
        }
        this.attachmentUrls.add(attachmentUrl);
    }

    /**
     * Associates with a deal
     */
    public void associateWithDeal(String dealId, Double dealValue) {
        this.dealId = dealId;
        this.dealValue = dealValue;
    }

    /**
     * Sets next step
     */
    public void setNextStep(String nextStep, LocalDate nextStepDate) {
        this.nextStep = nextStep;
        this.nextStepDate = nextStepDate;
    }

    /**
     * Marks as high priority
     */
    public void markAsHighPriority() {
        this.isHighPriority = true;
    }

    /**
     * Removes high priority status
     */
    public void removeHighPriority() {
        this.isHighPriority = false;
    }

    /**
     * Assigns to a user
     */
    public void assignTo(String userId, String userName) {
        this.assignedTo = userId;
        this.assignedToName = userName;
    }

    /**
     * Adds a related interaction
     */
    public void addRelatedInteraction(String interactionId) {
        if (this.relatedInteractions == null) {
            this.relatedInteractions = new ArrayList<>();
        }
        if (!this.relatedInteractions.contains(interactionId)) {
            this.relatedInteractions.add(interactionId);
        }
    }

    /**
     * Checks if interaction is overdue
     */
    public boolean isOverdue() {
        return this.status == InteractionStatus.SCHEDULED &&
               this.interactionDate != null &&
               this.interactionDate.isBefore(LocalDateTime.now());
    }

    /**
     * Checks if interaction is upcoming
     */
    public boolean isUpcoming() {
        return this.status == InteractionStatus.SCHEDULED &&
               this.interactionDate != null &&
               this.interactionDate.isAfter(LocalDateTime.now());
    }

    @SuppressWarnings("unchecked")
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
