package com.gogidix.sales.communication.application.dto.response;

import com.gogidix.sales.communication.application.dto.response.ConversationResponseDto;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ConversationResponseDtoTest {

        @Test
    void testBuilder() {
        ConversationResponseDto dto = ConversationResponseDto.builder()
                        .id("test-id")
            .conversationId("test-conversationId")
            .tenantId("test-tenantId")
            .title("test-title")
            .description("test-description")
            .type(ConversationResponseDto.ConversationTypeDto.DIRECT)
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .participantIds(Collections.emptyList())
            .participants(Collections.emptyList())
            .status(ConversationResponseDto.ConversationStatusDto.ACTIVE)
            .defaultChannel(ConversationResponseDto.ChannelTypeDto.EMAIL)
            .unreadCount(42)
            .lastMessageAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastMessageId("test-lastMessageId")
            .lastMessagePreview("test-lastMessagePreview")
            .isPinned(true)
            .isArchived(true)
            .archivedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .archivedBy("test-archivedBy")
            .tags(Collections.emptyList())
            .assignedTo("test-assignedTo")
            .priority(42)
            .relatedEntityType("test-relatedEntityType")
            .relatedEntityId("test-relatedEntityId")
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .resolvedBy("test-resolvedBy")
            .resolutionNotes("test-resolutionNotes")
            .slaDeadline(Instant.parse("2025-01-15T10:00:00Z"))
            .slaBreach(true)
            .isMuted(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-conversationId", dto.getConversationId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertEquals(ConversationResponseDto.ConversationTypeDto.DIRECT, dto.getType());
        assertEquals("test-ownerId", dto.getOwnerId());
        assertEquals("test-ownerName", dto.getOwnerName());
        assertEquals(ConversationResponseDto.ConversationStatusDto.ACTIVE, dto.getStatus());
        assertEquals(ConversationResponseDto.ChannelTypeDto.EMAIL, dto.getDefaultChannel());
        assertEquals(42, dto.getUnreadCount());
        assertEquals("test-lastMessageId", dto.getLastMessageId());
        assertEquals("test-lastMessagePreview", dto.getLastMessagePreview());
        assertTrue(dto.getIsPinned());
        assertTrue(dto.getIsArchived());
        assertEquals("test-archivedBy", dto.getArchivedBy());
        assertEquals("test-assignedTo", dto.getAssignedTo());
        assertEquals(42, dto.getPriority());
        assertEquals("test-relatedEntityType", dto.getRelatedEntityType());
        assertEquals("test-relatedEntityId", dto.getRelatedEntityId());
        assertEquals("test-resolvedBy", dto.getResolvedBy());
        assertEquals("test-resolutionNotes", dto.getResolutionNotes());
        assertTrue(dto.getSlaBreach());
        assertTrue(dto.getIsMuted());
    }

    @Test
    void testSettersAndGetters() {
        ConversationResponseDto dto = new ConversationResponseDto();
        dto.setId("val-id");
        dto.setConversationId("val-conversationId");
        dto.setTenantId("val-tenantId");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setType(ConversationResponseDto.ConversationTypeDto.DIRECT);
        dto.setOwnerId("val-ownerId");
        dto.setOwnerName("val-ownerName");
        dto.setStatus(ConversationResponseDto.ConversationStatusDto.ACTIVE);
        dto.setDefaultChannel(ConversationResponseDto.ChannelTypeDto.EMAIL);
        dto.setUnreadCount(99);
        dto.setLastMessageId("val-lastMessageId");
        dto.setLastMessagePreview("val-lastMessagePreview");
        dto.setIsPinned(true);
        dto.setIsArchived(true);
        dto.setArchivedBy("val-archivedBy");
        dto.setAssignedTo("val-assignedTo");
        dto.setPriority(99);
        dto.setRelatedEntityType("val-relatedEntityType");
        dto.setRelatedEntityId("val-relatedEntityId");
        dto.setResolvedBy("val-resolvedBy");
        dto.setResolutionNotes("val-resolutionNotes");
        dto.setSlaBreach(true);
        dto.setIsMuted(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-conversationId", dto.getConversationId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals(ConversationResponseDto.ConversationTypeDto.DIRECT, dto.getType());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals("val-ownerName", dto.getOwnerName());
        assertEquals(ConversationResponseDto.ConversationStatusDto.ACTIVE, dto.getStatus());
        assertEquals(ConversationResponseDto.ChannelTypeDto.EMAIL, dto.getDefaultChannel());
        assertEquals(99, dto.getUnreadCount());
        assertEquals("val-lastMessageId", dto.getLastMessageId());
        assertEquals("val-lastMessagePreview", dto.getLastMessagePreview());
        assertTrue(dto.getIsPinned());
        assertTrue(dto.getIsArchived());
        assertEquals("val-archivedBy", dto.getArchivedBy());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals(99, dto.getPriority());
        assertEquals("val-relatedEntityType", dto.getRelatedEntityType());
        assertEquals("val-relatedEntityId", dto.getRelatedEntityId());
        assertEquals("val-resolvedBy", dto.getResolvedBy());
        assertEquals("val-resolutionNotes", dto.getResolutionNotes());
        assertTrue(dto.getSlaBreach());
        assertTrue(dto.getIsMuted());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationResponseDto dto1 = ConversationResponseDto.builder()
                        .id("test-id")
            .conversationId("test-conversationId")
            .tenantId("test-tenantId")
            .title("test-title")
            .description("test-description")
            .type(ConversationResponseDto.ConversationTypeDto.DIRECT)
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .participantIds(Collections.emptyList())
            .participants(Collections.emptyList())
            .status(ConversationResponseDto.ConversationStatusDto.ACTIVE)
            .defaultChannel(ConversationResponseDto.ChannelTypeDto.EMAIL)
            .unreadCount(42)
            .lastMessageAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastMessageId("test-lastMessageId")
            .lastMessagePreview("test-lastMessagePreview")
            .isPinned(true)
            .isArchived(true)
            .archivedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .archivedBy("test-archivedBy")
            .tags(Collections.emptyList())
            .assignedTo("test-assignedTo")
            .priority(42)
            .relatedEntityType("test-relatedEntityType")
            .relatedEntityId("test-relatedEntityId")
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .resolvedBy("test-resolvedBy")
            .resolutionNotes("test-resolutionNotes")
            .slaDeadline(Instant.parse("2025-01-15T10:00:00Z"))
            .slaBreach(true)
            .isMuted(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ConversationResponseDto dto2 = ConversationResponseDto.builder()
                        .id("test-id")
            .conversationId("test-conversationId")
            .tenantId("test-tenantId")
            .title("test-title")
            .description("test-description")
            .type(ConversationResponseDto.ConversationTypeDto.DIRECT)
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .participantIds(Collections.emptyList())
            .participants(Collections.emptyList())
            .status(ConversationResponseDto.ConversationStatusDto.ACTIVE)
            .defaultChannel(ConversationResponseDto.ChannelTypeDto.EMAIL)
            .unreadCount(42)
            .lastMessageAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastMessageId("test-lastMessageId")
            .lastMessagePreview("test-lastMessagePreview")
            .isPinned(true)
            .isArchived(true)
            .archivedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .archivedBy("test-archivedBy")
            .tags(Collections.emptyList())
            .assignedTo("test-assignedTo")
            .priority(42)
            .relatedEntityType("test-relatedEntityType")
            .relatedEntityId("test-relatedEntityId")
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .resolvedBy("test-resolvedBy")
            .resolutionNotes("test-resolutionNotes")
            .slaDeadline(Instant.parse("2025-01-15T10:00:00Z"))
            .slaBreach(true)
            .isMuted(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConversationResponseDto dto = ConversationResponseDto.builder()
                        .id("test-id")
            .conversationId("test-conversationId")
            .tenantId("test-tenantId")
            .title("test-title")
            .description("test-description")
            .type(ConversationResponseDto.ConversationTypeDto.DIRECT)
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .participantIds(Collections.emptyList())
            .participants(Collections.emptyList())
            .status(ConversationResponseDto.ConversationStatusDto.ACTIVE)
            .defaultChannel(ConversationResponseDto.ChannelTypeDto.EMAIL)
            .unreadCount(42)
            .lastMessageAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastMessageId("test-lastMessageId")
            .lastMessagePreview("test-lastMessagePreview")
            .isPinned(true)
            .isArchived(true)
            .archivedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .archivedBy("test-archivedBy")
            .tags(Collections.emptyList())
            .assignedTo("test-assignedTo")
            .priority(42)
            .relatedEntityType("test-relatedEntityType")
            .relatedEntityId("test-relatedEntityId")
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .resolvedBy("test-resolvedBy")
            .resolutionNotes("test-resolutionNotes")
            .slaDeadline(Instant.parse("2025-01-15T10:00:00Z"))
            .slaBreach(true)
            .isMuted(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}