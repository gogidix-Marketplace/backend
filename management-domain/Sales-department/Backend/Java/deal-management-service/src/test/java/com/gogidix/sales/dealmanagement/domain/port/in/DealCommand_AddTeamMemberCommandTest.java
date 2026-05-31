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
class DealCommand_AddTeamMemberCommandTest {

        @Test
    void testSettersAndGetters() {
        DealCommand.AddTeamMemberCommand dto = new DealCommand.AddTeamMemberCommand();
        dto.setTenantId("val-tenantId");
        dto.setDealId("val-dealId");
        dto.setUserId("val-userId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dealId", dto.getDealId());
        assertEquals("val-userId", dto.getUserId());
    }

    @Test
    void testEqualsAndHashCode() {
        DealCommand.AddTeamMemberCommand dto1 = new DealCommand.AddTeamMemberCommand();
        DealCommand.AddTeamMemberCommand dto2 = new DealCommand.AddTeamMemberCommand();
        dto1.setTenantId("test");
        dto1.setDealId("test");
        dto1.setUserId("test");
        dto2.setTenantId("test");
        dto2.setDealId("test");
        dto2.setUserId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealCommand.AddTeamMemberCommand dto = new DealCommand.AddTeamMemberCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setUserId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealCommand.AddTeamMemberCommand dto = new DealCommand.AddTeamMemberCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setUserId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}