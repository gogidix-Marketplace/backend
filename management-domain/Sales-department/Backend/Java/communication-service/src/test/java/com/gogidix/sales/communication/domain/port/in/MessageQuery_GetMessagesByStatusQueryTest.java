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
class MessageQuery_GetMessagesByStatusQueryTest {

        @Test
    void testSettersAndGetters() {
        MessageQuery.GetMessagesByStatusQuery dto = new MessageQuery.GetMessagesByStatusQuery();
        dto.setStatus("val-status");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-status", dto.getStatus());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageQuery.GetMessagesByStatusQuery dto1 = new MessageQuery.GetMessagesByStatusQuery();
        MessageQuery.GetMessagesByStatusQuery dto2 = new MessageQuery.GetMessagesByStatusQuery();
        dto1.setStatus("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setStatus("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setStatus(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        MessageQuery.GetMessagesByStatusQuery dto = new MessageQuery.GetMessagesByStatusQuery();
        dto.setStatus("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        MessageQuery.GetMessagesByStatusQuery dto = new MessageQuery.GetMessagesByStatusQuery();
        dto.setStatus("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}