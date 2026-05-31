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
class ConversationController_AddTagRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ConversationController.AddTagRequestDto dto = new ConversationController.AddTagRequestDto();
        dto.setTag("val-tag");
        assertEquals("val-tag", dto.getTag());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationController.AddTagRequestDto dto1 = new ConversationController.AddTagRequestDto();
        ConversationController.AddTagRequestDto dto2 = new ConversationController.AddTagRequestDto();
        dto1.setTag("test");
        dto2.setTag("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTag(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversationController.AddTagRequestDto dto = new ConversationController.AddTagRequestDto();
        dto.setTag("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversationController.AddTagRequestDto dto = new ConversationController.AddTagRequestDto();
        dto.setTag("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}