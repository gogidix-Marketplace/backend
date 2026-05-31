package com.gogidix.sales.leadmanagement.domain.port.in;

import com.gogidix.sales.leadmanagement.domain.port.in.LeadCommand;
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
class LeadCommand_UpdateScoreCommandTest {

        @Test
    void testSettersAndGetters() {
        LeadCommand.UpdateScoreCommand dto = new LeadCommand.UpdateScoreCommand();
        dto.setTenantId("val-tenantId");
        dto.setLeadId("val-leadId");
        dto.setScore(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-leadId", dto.getLeadId());
        assertEquals(99, dto.getScore());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadCommand.UpdateScoreCommand dto1 = new LeadCommand.UpdateScoreCommand();
        LeadCommand.UpdateScoreCommand dto2 = new LeadCommand.UpdateScoreCommand();
        dto1.setTenantId("test");
        dto1.setLeadId("test");
        dto1.setScore(42);
        dto2.setTenantId("test");
        dto2.setLeadId("test");
        dto2.setScore(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadCommand.UpdateScoreCommand dto = new LeadCommand.UpdateScoreCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setScore(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadCommand.UpdateScoreCommand dto = new LeadCommand.UpdateScoreCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setScore(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}