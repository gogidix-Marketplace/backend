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
class DealCommand_RequestApprovalCommandTest {

        @Test
    void testSettersAndGetters() {
        DealCommand.RequestApprovalCommand dto = new DealCommand.RequestApprovalCommand();
        dto.setTenantId("val-tenantId");
        dto.setDealId("val-dealId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dealId", dto.getDealId());
    }

    @Test
    void testEqualsAndHashCode() {
        DealCommand.RequestApprovalCommand dto1 = new DealCommand.RequestApprovalCommand();
        DealCommand.RequestApprovalCommand dto2 = new DealCommand.RequestApprovalCommand();
        dto1.setTenantId("test");
        dto1.setDealId("test");
        dto2.setTenantId("test");
        dto2.setDealId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealCommand.RequestApprovalCommand dto = new DealCommand.RequestApprovalCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealCommand.RequestApprovalCommand dto = new DealCommand.RequestApprovalCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}