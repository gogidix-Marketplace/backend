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
class DealCommand_RejectApprovalCommandTest {

        @Test
    void testSettersAndGetters() {
        DealCommand.RejectApprovalCommand dto = new DealCommand.RejectApprovalCommand();
        dto.setTenantId("val-tenantId");
        dto.setDealId("val-dealId");
        dto.setRejecter("val-rejecter");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dealId", dto.getDealId());
        assertEquals("val-rejecter", dto.getRejecter());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        DealCommand.RejectApprovalCommand dto1 = new DealCommand.RejectApprovalCommand();
        DealCommand.RejectApprovalCommand dto2 = new DealCommand.RejectApprovalCommand();
        dto1.setTenantId("test");
        dto1.setDealId("test");
        dto1.setRejecter("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setDealId("test");
        dto2.setRejecter("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealCommand.RejectApprovalCommand dto = new DealCommand.RejectApprovalCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setRejecter("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealCommand.RejectApprovalCommand dto = new DealCommand.RejectApprovalCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setRejecter("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}