package com.gogidix.sales.communication.application.dto.response;

import com.gogidix.sales.communication.domain.model.Conversation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Conversation Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponseDto {

    private String id;
    private String conversationId;
    private String tenantId;
    private String title;
    private String description;
    private ConversationTypeDto type;
    private String ownerId;
    private String ownerName;
    private List<String> participantIds;
    private List<ParticipantDetailsDto> participants;
    private ConversationStatusDto status;
    private ChannelTypeDto defaultChannel;
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
    private Boolean isMuted;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantDetailsDto {
        private String participantId;
        private String participantName;
        private String participantType;
        private ParticipantRoleDto role;
        private Instant joinedAt;
        private Boolean isActive;
        private Instant lastReadAt;
    }

    public enum ConversationTypeDto {
        DIRECT,
        GROUP,
        CHANNEL,
        SUPPORT_TICKET,
        SALES_CONVERSATION,
        MARKETING_CAMPAIGN
    }

    public enum ConversationStatusDto {
        ACTIVE,
        ARCHIVED,
        CLOSED,
        RESOLVED,
        ON_HOLD,
        PENDING
    }

    public enum ChannelTypeDto {
        EMAIL,
        SMS,
        IN_APP,
        WHATSAPP,
        PHONE_CALL,
        WEBCHAT
    }

    public enum ParticipantRoleDto {
        OWNER,
        ADMIN,
        MODERATOR,
        MEMBER,
        GUEST
    }

    public static ConversationTypeDto mapType(Conversation.ConversationType type) {
        return type != null ? ConversationTypeDto.valueOf(type.name()) : null;
    }

    public static ConversationStatusDto mapStatus(Conversation.ConversationStatus status) {
        return status != null ? ConversationStatusDto.valueOf(status.name()) : null;
    }

    public static ChannelTypeDto mapChannelType(Conversation.ChannelType channelType) {
        return channelType != null ? ChannelTypeDto.valueOf(channelType.name()) : null;
    }

    public static ParticipantRoleDto mapParticipantRole(Conversation.ParticipantDetails.ParticipantRole role) {
        return role != null ? ParticipantRoleDto.valueOf(role.name()) : null;
    }
}
