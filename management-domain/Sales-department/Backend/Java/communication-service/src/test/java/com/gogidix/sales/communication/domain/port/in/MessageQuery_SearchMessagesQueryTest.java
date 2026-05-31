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
class MessageQuery_SearchMessagesQueryTest {

        @Test
    void testSettersAndGetters() {
        MessageQuery.SearchMessagesQuery dto = new MessageQuery.SearchMessagesQuery();
        dto.setSearchTerm("val-searchTerm");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-searchTerm", dto.getSearchTerm());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageQuery.SearchMessagesQuery dto1 = new MessageQuery.SearchMessagesQuery();
        MessageQuery.SearchMessagesQuery dto2 = new MessageQuery.SearchMessagesQuery();
        dto1.setSearchTerm("test");
        dto1.setStartDate(null);
        dto1.setEndDate(null);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setSearchTerm("test");
        dto2.setStartDate(null);
        dto2.setEndDate(null);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setSearchTerm(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        MessageQuery.SearchMessagesQuery dto = new MessageQuery.SearchMessagesQuery();
        dto.setSearchTerm("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        MessageQuery.SearchMessagesQuery dto = new MessageQuery.SearchMessagesQuery();
        dto.setSearchTerm("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}