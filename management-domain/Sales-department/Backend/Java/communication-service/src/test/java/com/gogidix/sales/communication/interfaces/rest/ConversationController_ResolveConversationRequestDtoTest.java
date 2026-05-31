package com.gogidix.sales.communication.interfaces.rest;

import com.gogidix.sales.communication.interfaces.rest.ConversationController;
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
class ConversationController_ResolveConversationRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ConversationController.ResolveConversationRequestDto dto = new ConversationController.ResolveConversationRequestDto();
        dto.setResolutionNotes("val-resolutionNotes");
        assertEquals("val-resolutionNotes", dto.getResolutionNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationController.ResolveConversationRequestDto dto1 = new ConversationController.ResolveConversationRequestDto();
        ConversationController.ResolveConversationRequestDto dto2 = new ConversationController.ResolveConversationRequestDto();
        dto1.setResolutionNotes("test");
        dto2.setResolutionNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setResolutionNotes(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversationController.ResolveConversationRequestDto dto = new ConversationController.ResolveConversationRequestDto();
        dto.setResolutionNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversationController.ResolveConversationRequestDto dto = new ConversationController.ResolveConversationRequestDto();
        dto.setResolutionNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}