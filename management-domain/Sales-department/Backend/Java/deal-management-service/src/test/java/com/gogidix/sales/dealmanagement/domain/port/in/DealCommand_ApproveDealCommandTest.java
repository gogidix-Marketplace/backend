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
class DealCommand_ApproveDealCommandTest {

        @Test
    void testSettersAndGetters() {
        DealCommand.ApproveDealCommand dto = new DealCommand.ApproveDealCommand();
        dto.setTenantId("val-tenantId");
        dto.setDealId("val-dealId");
        dto.setApprover("val-approver");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dealId", dto.getDealId());
        assertEquals("val-approver", dto.getApprover());
    }

    @Test
    void testEqualsAndHashCode() {
        DealCommand.ApproveDealCommand dto1 = new DealCommand.ApproveDealCommand();
        DealCommand.ApproveDealCommand dto2 = new DealCommand.ApproveDealCommand();
        dto1.setTenantId("test");
        dto1.setDealId("test");
        dto1.setApprover("test");
        dto2.setTenantId("test");
        dto2.setDealId("test");
        dto2.setApprover("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealCommand.ApproveDealCommand dto = new DealCommand.ApproveDealCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setApprover("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealCommand.ApproveDealCommand dto = new DealCommand.ApproveDealCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setApprover("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}