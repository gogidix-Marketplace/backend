package com.gogidix.sales.crm.interfaces.rest;

import com.gogidix.sales.crm.interfaces.rest.InteractionController;
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
class InteractionController_AddParticipantRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        InteractionController.AddParticipantRequestDto dto = new InteractionController.AddParticipantRequestDto();
        dto.setContactId("val-contactId");
        assertEquals("val-contactId", dto.getContactId());
    }

    @Test
    void testEqualsAndHashCode() {
        InteractionController.AddParticipantRequestDto dto1 = new InteractionController.AddParticipantRequestDto();
        InteractionController.AddParticipantRequestDto dto2 = new InteractionController.AddParticipantRequestDto();
        dto1.setContactId("test");
        dto2.setContactId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setContactId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InteractionController.AddParticipantRequestDto dto = new InteractionController.AddParticipantRequestDto();
        dto.setContactId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InteractionController.AddParticipantRequestDto dto = new InteractionController.AddParticipantRequestDto();
        dto.setContactId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}