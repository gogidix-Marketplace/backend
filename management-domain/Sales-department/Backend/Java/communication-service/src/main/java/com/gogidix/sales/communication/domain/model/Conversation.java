package com.gogidix.sales.communication.domain.model;

import com.gogidix.sales.communication.domain.event.ConversationCreatedEvent;
import com.gogidix.sales.communication.shared.base.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Conversation Domain Entity
 * Multi-tenant conversation for organizing messages
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "conversations")
public class Conversation extends AuditableEntity {

    @Indexed
    private String conversationId;

    @Indexed
    private String tenantId;

    private String title;

    private String description;

    private ConversationType type;

    @Indexed
    private String ownerId;

    private String ownerName;

    @Indexed
    private Set<String> participantIds;

    private List<ParticipantDetails> participants;

    @Indexed
    private ConversationStatus status;

    private ChannelType defaultChannel;

    private Integer unreadCount;

    private Instant lastMessageAt;

    private String lastMessageId;

    private String lastMessagePreview;

    private Boolean isPinned;

    private Boolean isArchived;

    private Instant archivedAt;

    private String archivedBy;

    private List<String> tags;

    private String assignedTo; // User or team assigned

    private Integer priority; // 1=low, 2=normal, 3=high

    private String relatedEntityType; // LEAD, OPPORTUNITY, TICKET, ORDER

    private String relatedEntityId;

    private Instant resolvedAt;

    private String resolvedBy;

    private String resolutionNotes;

    private Instant slaDeadline;

    private Boolean slaBreach;

    @Builder.Default
    private Set<String> adminIds = new HashSet<>();

    private Boolean isMuted;

    private Instant mutedUntil;

    public enum ConversationType {
        DIRECT,
        GROUP,
        CHANNEL,
        SUPPORT_TICKET,
        SALES_CONVERSATION,
        MARKETING_CAMPAIGN
    }

    public enum ConversationStatus {
        ACTIVE,
        ARCHIVED,
        CLOSED,
        RESOLVED,
        ON_HOLD,
        PENDING
    }

    public enum ChannelType {
        EMAIL,
        SMS,
        IN_APP,
        WHATSAPP,
        PHONE_CALL,
        WEBCHAT
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantDetails {
        private String participantId;
        private String participantName;
        private String participantType; // USER, BOT, SYSTEM
        private ParticipantRole role;
        private Instant joinedAt;
        private Boolean isActive;
        private Instant lastReadAt;

        public enum ParticipantRole {
            OWNER,
            ADMIN,
            MODERATOR,
            MEMBER,
            GUEST
        }
    }

    /**
     * Creates a new conversation
     */
    public static Conversation create(String tenantId, String title, ConversationType type,
                                       String ownerId, String ownerName,
                                       List<ParticipantDetails> participants,
                                       ChannelType defaultChannel) {
        Conversation conversation = Conversation.builder()
                .tenantId(tenantId)
                .title(title)
                .type(type)
                .ownerId(ownerId)
                .ownerName(ownerName)
                .participants(participants != null ? participants : new ArrayList<>())
                .status(ConversationStatus.ACTIVE)
                .defaultChannel(defaultChannel)
                .unreadCount(0)
                .isPinned(false)
                .isArchived(false)
                .isMuted(false)
                .priority(2)
                .slaBreach(false)
                .tags(new ArrayList<>())
                .adminIds(new HashSet<>())
                .build();

        // Extract participant IDs
        Set<String> participantIds = new HashSet<>();
        if (participants != null) {
            participants.forEach(p -> participantIds.add(p.getParticipantId()));
            // Add owner as admin
            participantIds.add(ownerId);
            conversation.getAdminIds().add(ownerId);
        }
        conversation.setParticipantIds(participantIds);

        conversation.addDomainEvent(ConversationCreatedEvent.builder()
                .conversationId(conversation.getConversationId())
                .tenantId(tenantId)
                .title(title)
                .type(type.name())
                .ownerId(ownerId)
                .timestamp(Instant.now())
                .eventType("CONVERSATION_CREATED")
                .build());

        return conversation;
    }

    /**
     * Adds a participant to the conversation
     */
    public void addParticipant(ParticipantDetails participant) {
        if (this.participants == null) {
            this.participants = new ArrayList<>();
        }
        this.participants.add(participant);

        if (this.participantIds == null) {
            this.participantIds = new HashSet<>();
        }
        this.participantIds.add(participant.getParticipantId());
    }

    /**
     * Removes a participant from the conversation
     */
    public void removeParticipant(String participantId) {
        if (this.participants != null) {
            this.participants.removeIf(p -> p.getParticipantId().equals(participantId));
        }
        if (this.participantIds != null) {
            this.participantIds.remove(participantId);
        }

        // Remove from admin if applicable
        if (this.adminIds != null) {
            this.adminIds.remove(participantId);
        }
    }

    /**
     * Updates the last message information
     */
    public void updateLastMessage(String messageId, Instant messageAt, String preview) {
        this.lastMessageId = messageId;
        this.lastMessageAt = messageAt;
        this.lastMessagePreview = preview != null && preview.length() > 100
                ? preview.substring(0, 100) + "..."
                : preview;
    }

    /**
     * Increments the unread count
     */
    public void incrementUnreadCount() {
        if (this.unreadCount == null) {
            this.unreadCount = 0;
        }
        this.unreadCount++;
    }

    /**
     * Resets the unread count for a user
     */
    public void markAsRead(String userId) {
        this.unreadCount = 0;

        // Update participant's last read time
        if (this.participants != null) {
            this.participants.stream()
                    .filter(p -> p.getParticipantId().equals(userId))
                    .forEach(p -> p.setLastReadAt(Instant.now()));
        }
    }

    /**
     * Archives the conversation
     */
    public void archive(String archivedBy) {
        this.isArchived = true;
        this.archivedAt = Instant.now();
        this.archivedBy = archivedBy;
        this.status = ConversationStatus.ARCHIVED;
    }

    /**
     * Unarchives the conversation
     */
    public void unarchive() {
        this.isArchived = false;
        this.archivedAt = null;
        this.archivedBy = null;
        this.status = ConversationStatus.ACTIVE;
    }

    /**
     * Pins the conversation
     */
    public void pin() {
        this.isPinned = true;
    }

    /**
     * Unpins the conversation
     */
    public void unpin() {
        this.isPinned = false;
    }

    /**
     * Mutes the conversation
     */
    public void mute(Instant mutedUntil) {
        this.isMuted = true;
        this.mutedUntil = mutedUntil;
    }

    /**
     * Unmutes the conversation
     */
    public void unmute() {
        this.isMuted = false;
        this.mutedUntil = null;
    }

    /**
     * Resolves the conversation
     */
    public void resolve(String resolvedBy, String resolutionNotes) {
        this.status = ConversationStatus.RESOLVED;
        this.resolvedAt = Instant.now();
        this.resolvedBy = resolvedBy;
        this.resolutionNotes = resolutionNotes;
    }

    /**
     * Reopens the conversation
     */
    public void reopen() {
        this.status = ConversationStatus.ACTIVE;
        this.resolvedAt = null;
        this.resolvedBy = null;
        this.resolutionNotes = null;
    }

    /**
     * Adds a tag to the conversation
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Removes a tag from the conversation
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Assigns the conversation to a user or team
     */
    public void assignTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     * Links the conversation to an entity
     */
    public void linkToEntity(String entityType, String entityId) {
        this.relatedEntityType = entityType;
        this.relatedEntityId = entityId;
    }

    /**
     * Sets the SLA deadline
     */
    public void setSlaDeadline(Instant deadline) {
        this.slaDeadline = deadline;
        this.slaBreach = false;
    }

    /**
     * Checks if SLA is breached
     */
    public void checkSlaBreach() {
        if (this.slaDeadline != null && Instant.now().isAfter(this.slaDeadline)) {
            this.slaBreach = true;
        }
    }

    /**
     * Adds an admin to the conversation
     */
    public void addAdmin(String userId) {
        if (this.adminIds == null) {
            this.adminIds = new HashSet<>();
        }
        this.adminIds.add(userId);

        // Update participant role if exists
        if (this.participants != null) {
            this.participants.stream()
                    .filter(p -> p.getParticipantId().equals(userId))
                    .forEach(p -> p.setRole(ParticipantDetails.ParticipantRole.ADMIN));
        }
    }

    /**
     * Removes an admin from the conversation
     */
    public void removeAdmin(String userId) {
        if (this.adminIds != null) {
            this.adminIds.remove(userId);
        }

        // Update participant role if exists
        if (this.participants != null) {
            this.participants.stream()
                    .filter(p -> p.getParticipantId().equals(userId))
                    .forEach(p -> p.setRole(ParticipantDetails.ParticipantRole.MEMBER));
        }
    }

    /**
     * Checks if a user is a participant
     */
    public boolean isParticipant(String userId) {
        return this.participantIds != null && this.participantIds.contains(userId);
    }

    /**
     * Checks if a user is an admin
     */
    public boolean isAdmin(String userId) {
        return this.adminIds != null && this.adminIds.contains(userId);
    }

    private final List<ConversationCreatedEvent> domainEvents = new ArrayList<>();

    public void addDomainEvent(ConversationCreatedEvent event) {
        this.domainEvents.add(event);
    }

    public List<ConversationCreatedEvent> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}
