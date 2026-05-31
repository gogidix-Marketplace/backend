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
class DealCommand_MarkAsWonCommandTest {

        @Test
    void testSettersAndGetters() {
        DealCommand.MarkAsWonCommand dto = new DealCommand.MarkAsWonCommand();
        dto.setTenantId("val-tenantId");
        dto.setDealId("val-dealId");
        dto.setFinalAmount(BigDecimal.ONE);
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dealId", dto.getDealId());
        assertEquals(BigDecimal.ONE, dto.getFinalAmount());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        DealCommand.MarkAsWonCommand dto1 = new DealCommand.MarkAsWonCommand();
        DealCommand.MarkAsWonCommand dto2 = new DealCommand.MarkAsWonCommand();
        dto1.setTenantId("test");
        dto1.setDealId("test");
        dto1.setFinalAmount(BigDecimal.TEN);
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setDealId("test");
        dto2.setFinalAmount(BigDecimal.TEN);
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealCommand.MarkAsWonCommand dto = new DealCommand.MarkAsWonCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setFinalAmount(BigDecimal.TEN);
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealCommand.MarkAsWonCommand dto = new DealCommand.MarkAsWonCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setFinalAmount(BigDecimal.TEN);
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}