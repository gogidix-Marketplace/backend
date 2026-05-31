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
class ConversationController_AssignConversationRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ConversationController.AssignConversationRequestDto dto = new ConversationController.AssignConversationRequestDto();
        dto.setAssignedTo("val-assignedTo");
        assertEquals("val-assignedTo", dto.getAssignedTo());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationController.AssignConversationRequestDto dto1 = new ConversationController.AssignConversationRequestDto();
        ConversationController.AssignConversationRequestDto dto2 = new ConversationController.AssignConversationRequestDto();
        dto1.setAssignedTo("test");
        dto2.setAssignedTo("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setAssignedTo(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversationController.AssignConversationRequestDto dto = new ConversationController.AssignConversationRequestDto();
        dto.setAssignedTo("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversationController.AssignConversationRequestDto dto = new ConversationController.AssignConversationRequestDto();
        dto.setAssignedTo("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}