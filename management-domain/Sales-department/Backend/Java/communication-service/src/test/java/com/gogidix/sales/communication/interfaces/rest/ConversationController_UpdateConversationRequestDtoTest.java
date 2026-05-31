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
class ConversationController_UpdateConversationRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ConversationController.UpdateConversationRequestDto dto = new ConversationController.UpdateConversationRequestDto();
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setAssignedTo("val-assignedTo");
        dto.setPriority(99);
        dto.setRelatedEntityType("val-relatedEntityType");
        dto.setRelatedEntityId("val-relatedEntityId");
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals(99, dto.getPriority());
        assertEquals("val-relatedEntityType", dto.getRelatedEntityType());
        assertEquals("val-relatedEntityId", dto.getRelatedEntityId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationController.UpdateConversationRequestDto dto1 = new ConversationController.UpdateConversationRequestDto();
        ConversationController.UpdateConversationRequestDto dto2 = new ConversationController.UpdateConversationRequestDto();
        dto1.setTitle("test");
        dto1.setDescription("test");
        dto1.setAssignedTo("test");
        dto1.setPriority(42);
        dto1.setRelatedEntityType("test");
        dto1.setRelatedEntityId("test");
        dto2.setTitle("test");
        dto2.setDescription("test");
        dto2.setAssignedTo("test");
        dto2.setPriority(42);
        dto2.setRelatedEntityType("test");
        dto2.setRelatedEntityId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTitle(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversationController.UpdateConversationRequestDto dto = new ConversationController.UpdateConversationRequestDto();
        dto.setTitle("test");
        dto.setDescription("test");
        dto.setAssignedTo("test");
        dto.setPriority(42);
        dto.setRelatedEntityType("test");
        dto.setRelatedEntityId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversationController.UpdateConversationRequestDto dto = new ConversationController.UpdateConversationRequestDto();
        dto.setTitle("test");
        dto.setDescription("test");
        dto.setAssignedTo("test");
        dto.setPriority(42);
        dto.setRelatedEntityType("test");
        dto.setRelatedEntityId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}