package com.gogidix.sales.dealmanagement.domain.port.in;

import com.gogidix.sales.dealmanagement.domain.port.in.DealCommand;
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
class DealCommand_AdvanceStageCommandTest {

        @Test
    void testSettersAndGetters() {
        DealCommand.AdvanceStageCommand dto = new DealCommand.AdvanceStageCommand();
        dto.setTenantId("val-tenantId");
        dto.setDealId("val-dealId");
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dealId", dto.getDealId());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        DealCommand.AdvanceStageCommand dto1 = new DealCommand.AdvanceStageCommand();
        DealCommand.AdvanceStageCommand dto2 = new DealCommand.AdvanceStageCommand();
        dto1.setTenantId("test");
        dto1.setDealId("test");
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setDealId("test");
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealCommand.AdvanceStageCommand dto = new DealCommand.AdvanceStageCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealCommand.AdvanceStageCommand dto = new DealCommand.AdvanceStageCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}