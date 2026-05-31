package com.gogidix.shared.ai.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Map;

class ChatResponseTest {

    @Test
    void builder_defaults() {
        ChatResponse resp = ChatResponse.builder()
                .id("test")
                .content("hello")
                .build();
        assertEquals(Map.of(), resp.getMetadata());
        assertEquals(List.of(), resp.getStopReasons());
        assertNull(resp.getTokenUsage());
    }
}
