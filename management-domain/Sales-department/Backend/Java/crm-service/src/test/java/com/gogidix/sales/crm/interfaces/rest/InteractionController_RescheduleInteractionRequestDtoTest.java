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
class InteractionController_RescheduleInteractionRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        InteractionController.RescheduleInteractionRequestDto dto = new InteractionController.RescheduleInteractionRequestDto();
        dto.setReason("val-reason");
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        InteractionController.RescheduleInteractionRequestDto dto1 = new InteractionController.RescheduleInteractionRequestDto();
        InteractionController.RescheduleInteractionRequestDto dto2 = new InteractionController.RescheduleInteractionRequestDto();
        dto1.setNewDate(LocalDateTime.of(2025,1,1,10,0));
        dto1.setReason("test");
        dto2.setNewDate(LocalDateTime.of(2025,1,1,10,0));
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setNewDate(LocalDateTime.of(2099,12,31,23,59));
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InteractionController.RescheduleInteractionRequestDto dto = new InteractionController.RescheduleInteractionRequestDto();
        dto.setNewDate(LocalDateTime.of(2025,1,1,10,0));
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InteractionController.RescheduleInteractionRequestDto dto = new InteractionController.RescheduleInteractionRequestDto();
        dto.setNewDate(LocalDateTime.of(2025,1,1,10,0));
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}