package com.gogidix.shared.ai.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatMessageTest {

    @Test
    void builder_createsMessage() {
        ChatMessage msg = ChatMessage.builder()
                .role(ChatMessage.Role.USER)
                .content("Hello")
                .build();
        assertEquals(ChatMessage.Role.USER, msg.getRole());
        assertEquals("Hello", msg.getContent());
    }

    @Test
    void roles_allPresent() {
        assertArrayEquals(
                new ChatMessage.Role[]{ChatMessage.Role.SYSTEM, ChatMessage.Role.USER, ChatMessage.Role.ASSISTANT, ChatMessage.Role.FUNCTION},
                ChatMessage.Role.values()
        );
    }
}
