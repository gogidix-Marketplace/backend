package com.gogidix.hr.globalcompliance.domain.port.in;

import com.gogidix.hr.globalcompliance.domain.port.in.CheckCommand;
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
class CheckCommand_AddCommentCommandTest {

        @Test
    void testSettersAndGetters() {
        CheckCommand.AddCommentCommand dto = new CheckCommand.AddCommentCommand();
        dto.setTenantId("val-tenantId");
        dto.setCheckId("val-checkId");
        dto.setComment("val-comment");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-comment", dto.getComment());
    }

    @Test
    void testEqualsAndHashCode() {
        CheckCommand.AddCommentCommand dto1 = new CheckCommand.AddCommentCommand();
        CheckCommand.AddCommentCommand dto2 = new CheckCommand.AddCommentCommand();
        dto1.setTenantId("test");
        dto1.setCheckId("test");
        dto1.setComment("test");
        dto2.setTenantId("test");
        dto2.setCheckId("test");
        dto2.setComment("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CheckCommand.AddCommentCommand dto = new CheckCommand.AddCommentCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setComment("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CheckCommand.AddCommentCommand dto = new CheckCommand.AddCommentCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setComment("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}