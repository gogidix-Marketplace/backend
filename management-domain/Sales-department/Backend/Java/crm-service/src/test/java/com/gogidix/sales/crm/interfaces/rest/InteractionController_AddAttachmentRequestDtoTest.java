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
class InteractionController_AddAttachmentRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        InteractionController.AddAttachmentRequestDto dto = new InteractionController.AddAttachmentRequestDto();
        dto.setAttachmentUrl("val-attachmentUrl");
        assertEquals("val-attachmentUrl", dto.getAttachmentUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        InteractionController.AddAttachmentRequestDto dto1 = new InteractionController.AddAttachmentRequestDto();
        InteractionController.AddAttachmentRequestDto dto2 = new InteractionController.AddAttachmentRequestDto();
        dto1.setAttachmentUrl("test");
        dto2.setAttachmentUrl("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setAttachmentUrl(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InteractionController.AddAttachmentRequestDto dto = new InteractionController.AddAttachmentRequestDto();
        dto.setAttachmentUrl("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InteractionController.AddAttachmentRequestDto dto = new InteractionController.AddAttachmentRequestDto();
        dto.setAttachmentUrl("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}