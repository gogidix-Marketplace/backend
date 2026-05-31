package com.gogidix.sales.communication.application.service;

import com.gogidix.sales.communication.application.service.ConversationQueryService;
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
class ConversationQueryService_ConversationSummaryTest {

        @Test
    void testBuilder() {
        ConversationQueryService.ConversationSummary dto = ConversationQueryService.ConversationSummary.builder()
                        .totalConversations(42L)
            .activeConversations(42L)
            .archivedConversations(42L)
            .resolvedConversations(42L)
            .unreadConversations(42L)
            .slaBreachingConversations(42L)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalConversations());
        assertEquals(42L, dto.getActiveConversations());
        assertEquals(42L, dto.getArchivedConversations());
        assertEquals(42L, dto.getResolvedConversations());
        assertEquals(42L, dto.getUnreadConversations());
        assertEquals(42L, dto.getSlaBreachingConversations());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationQueryService.ConversationSummary dto1 = ConversationQueryService.ConversationSummary.builder()
                        .totalConversations(42L)
            .activeConversations(42L)
            .archivedConversations(42L)
            .resolvedConversations(42L)
            .unreadConversations(42L)
            .slaBreachingConversations(42L)
            .build();
        ConversationQueryService.ConversationSummary dto2 = ConversationQueryService.ConversationSummary.builder()
                        .totalConversations(42L)
            .activeConversations(42L)
            .archivedConversations(42L)
            .resolvedConversations(42L)
            .unreadConversations(42L)
            .slaBreachingConversations(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConversationQueryService.ConversationSummary dto = ConversationQueryService.ConversationSummary.builder()
                        .totalConversations(42L)
            .activeConversations(42L)
            .archivedConversations(42L)
            .resolvedConversations(42L)
            .unreadConversations(42L)
            .slaBreachingConversations(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}