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
class DealCommand_MarkAsLostCommandTest {

        @Test
    void testSettersAndGetters() {
        DealCommand.MarkAsLostCommand dto = new DealCommand.MarkAsLostCommand();
        dto.setTenantId("val-tenantId");
        dto.setDealId("val-dealId");
        dto.setLossReason("val-lossReason");
        dto.setLossDetails("val-lossDetails");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dealId", dto.getDealId());
        assertEquals("val-lossReason", dto.getLossReason());
        assertEquals("val-lossDetails", dto.getLossDetails());
    }

    @Test
    void testEqualsAndHashCode() {
        DealCommand.MarkAsLostCommand dto1 = new DealCommand.MarkAsLostCommand();
        DealCommand.MarkAsLostCommand dto2 = new DealCommand.MarkAsLostCommand();
        dto1.setTenantId("test");
        dto1.setDealId("test");
        dto1.setLossReason("test");
        dto1.setLossDetails("test");
        dto2.setTenantId("test");
        dto2.setDealId("test");
        dto2.setLossReason("test");
        dto2.setLossDetails("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealCommand.MarkAsLostCommand dto = new DealCommand.MarkAsLostCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setLossReason("test");
        dto.setLossDetails("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealCommand.MarkAsLostCommand dto = new DealCommand.MarkAsLostCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setLossReason("test");
        dto.setLossDetails("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}