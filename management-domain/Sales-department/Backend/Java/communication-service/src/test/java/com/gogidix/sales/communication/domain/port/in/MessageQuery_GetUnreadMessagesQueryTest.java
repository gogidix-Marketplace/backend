package com.gogidix.sales.communication.domain.port.in;

import com.gogidix.sales.communication.domain.port.in.MessageQuery;
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
class MessageQuery_GetUnreadMessagesQueryTest {

        @Test
    void testSettersAndGetters() {
        MessageQuery.GetUnreadMessagesQuery dto = new MessageQuery.GetUnreadMessagesQuery();
        dto.setUserId("val-userId");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-userId", dto.getUserId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageQuery.GetUnreadMessagesQuery dto1 = new MessageQuery.GetUnreadMessagesQuery();
        MessageQuery.GetUnreadMessagesQuery dto2 = new MessageQuery.GetUnreadMessagesQuery();
        dto1.setUserId("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setUserId("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setUserId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        MessageQuery.GetUnreadMessagesQuery dto = new MessageQuery.GetUnreadMessagesQuery();
        dto.setUserId("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        MessageQuery.GetUnreadMessagesQuery dto = new MessageQuery.GetUnreadMessagesQuery();
        dto.setUserId("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}