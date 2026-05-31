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
class MessageQuery_GetMessageByIdQueryTest {

        @Test
    void testSettersAndGetters() {
        MessageQuery.GetMessageByIdQuery dto = new MessageQuery.GetMessageByIdQuery();
        dto.setMessageId("val-messageId");
        assertEquals("val-messageId", dto.getMessageId());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageQuery.GetMessageByIdQuery dto1 = new MessageQuery.GetMessageByIdQuery();
        MessageQuery.GetMessageByIdQuery dto2 = new MessageQuery.GetMessageByIdQuery();
        dto1.setMessageId("test");
        dto2.setMessageId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setMessageId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        MessageQuery.GetMessageByIdQuery dto = new MessageQuery.GetMessageByIdQuery();
        dto.setMessageId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        MessageQuery.GetMessageByIdQuery dto = new MessageQuery.GetMessageByIdQuery();
        dto.setMessageId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}