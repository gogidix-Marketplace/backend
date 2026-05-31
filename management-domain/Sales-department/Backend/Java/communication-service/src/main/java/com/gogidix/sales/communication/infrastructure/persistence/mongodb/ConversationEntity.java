package com.gogidix.sales.communication.infrastructure.persistence.mongodb;

import com.gogidix.sales.communication.domain.model.Conversation;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MongoDB Entity for Conversation
 * Separate from domain model for clean architecture
 */
@Document(collection = "conversations")
public class ConversationEntity {

    @Id
    private String id;

    @Indexed
    private String conversationId;

    @Indexed
    private String tenantId;

    private String title;
    private String description;
    private Conversation.ConversationType type;

    @Indexed
    private String ownerId;

    private String ownerName;

    @Indexed
    private Set<String> participantIds;

    private List<ParticipantDetails> participants;

    @Indexed
    private Conversation.ConversationStatus status;

    private Conversation.ChannelType defaultChannel;

    private Integer unreadCount;

    private Instant lastMessageAt;

    private String lastMessageId;

    private String lastMessagePreview;

    private Boolean isPinned;

    private Boolean isArchived;

    private Instant archivedAt;

    private String archivedBy;

    private List<String> tags;

    private String assignedTo;

    private Integer priority;

    private String relatedEntityType;

    private String relatedEntityId;

    private Instant resolvedAt;

    private String resolvedBy;

    private String resolutionNotes;

    private Instant slaDeadline;

    private Boolean slaBreach;

    private Set<String> adminIds;

    private Boolean isMuted;

    private Instant mutedUntil;

    private Instant createdAt;
    private Instant updatedAt;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Conversation.ConversationType getType() { return type; }
    public void setType(Conversation.ConversationType type) { this.type = type; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public Set<String> getParticipantIds() { return participantIds; }
    public void setParticipantIds(Set<String> participantIds) { this.participantIds = participantIds; }

    public List<ParticipantDetails> getParticipants() { return participants; }
    public void setParticipants(List<ParticipantDetails> participants) { this.participants = participants; }

    public Conversation.ConversationStatus getStatus() { return status; }
    public void setStatus(Conversation.ConversationStatus status) { this.status = status; }

    public Conversation.ChannelType getDefaultChannel() { return defaultChannel; }
    public void setDefaultChannel(Conversation.ChannelType defaultChannel) { this.defaultChannel = defaultChannel; }

    public Integer getUnreadCount() { return unreadCount; }
    public void setUnreadCount(Integer unreadCount) { this.unreadCount = unreadCount; }

    public Instant getLastMessageAt() { return lastMessageAt; }
    public void setLastMessageAt(Instant lastMessageAt) { this.lastMessageAt = lastMessageAt; }

    public String getLastMessageId() { return lastMessageId; }
    public void setLastMessageId(String lastMessageId) { this.lastMessageId = lastMessageId; }

    public String getLastMessagePreview() { return lastMessagePreview; }
    public void setLastMessagePreview(String lastMessagePreview) { this.lastMessagePreview = lastMessagePreview; }

    public Boolean getIsPinned() { return isPinned; }
    public void setIsPinned(Boolean isPinned) { this.isPinned = isPinned; }

    public Boolean getIsArchived() { return isArchived; }
    public void setIsArchived(Boolean isArchived) { this.isArchived = isArchived; }

    public Instant getArchivedAt() { return archivedAt; }
    public void setArchivedAt(Instant archivedAt) { this.archivedAt = archivedAt; }

    public String getArchivedBy() { return archivedBy; }
    public void setArchivedBy(String archivedBy) { this.archivedBy = archivedBy; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public String getRelatedEntityType() { return relatedEntityType; }
    public void setRelatedEntityType(String relatedEntityType) { this.relatedEntityType = relatedEntityType; }

    public String getRelatedEntityId() { return relatedEntityId; }
    public void setRelatedEntityId(String relatedEntityId) { this.relatedEntityId = relatedEntityId; }

    public Instant getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Instant resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public Instant getSlaDeadline() { return slaDeadline; }
    public void setSlaDeadline(Instant slaDeadline) { this.slaDeadline = slaDeadline; }

    public Boolean getSlaBreach() { return slaBreach; }
    public void setSlaBreach(Boolean slaBreach) { this.slaBreach = slaBreach; }

    public Set<String> getAdminIds() { return adminIds; }
    public void setAdminIds(Set<String> adminIds) { this.adminIds = adminIds; }

    public Boolean getIsMuted() { return isMuted; }
    public void setIsMuted(Boolean isMuted) { this.isMuted = isMuted; }

    public Instant getMutedUntil() { return mutedUntil; }
    public void setMutedUntil(Instant mutedUntil) { this.mutedUntil = mutedUntil; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Embedded classes for MongoDB
    public static class ParticipantDetails {
        private String participantId;
        private String participantName;
        private String participantType;
        private Conversation.ParticipantDetails.ParticipantRole role;
        private Instant joinedAt;
        private Boolean isActive;
        private Instant lastReadAt;

        // Getters and setters
        public String getParticipantId() { return participantId; }
        public void setParticipantId(String participantId) { this.participantId = participantId; }

        public String getParticipantName() { return participantName; }
        public void setParticipantName(String participantName) { this.participantName = participantName; }

        public String getParticipantType() { return participantType; }
        public void setParticipantType(String participantType) { this.participantType = participantType; }

        public Conversation.ParticipantDetails.ParticipantRole getRole() { return role; }
        public void setRole(Conversation.ParticipantDetails.ParticipantRole role) { this.role = role; }

        public Instant getJoinedAt() { return joinedAt; }
        public void setJoinedAt(Instant joinedAt) { this.joinedAt = joinedAt; }

        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean isActive) { this.isActive = isActive; }

        public Instant getLastReadAt() { return lastReadAt; }
        public void setLastReadAt(Instant lastReadAt) { this.lastReadAt = lastReadAt; }
    }
}
