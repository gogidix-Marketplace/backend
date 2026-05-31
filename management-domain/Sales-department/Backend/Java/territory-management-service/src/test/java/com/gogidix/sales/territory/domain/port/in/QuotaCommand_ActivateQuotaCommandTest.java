package com.gogidix.sales.territory.domain.port.in;

import com.gogidix.sales.territory.domain.port.in.QuotaCommand;
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
class QuotaCommand_ActivateQuotaCommandTest {

        @Test
    void testSettersAndGetters() {
        QuotaCommand.ActivateQuotaCommand dto = new QuotaCommand.ActivateQuotaCommand();
        dto.setTenantId("val-tenantId");
        dto.setQuotaId("val-quotaId");
        dto.setApprovedBy("val-approvedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-quotaId", dto.getQuotaId());
        assertEquals("val-approvedBy", dto.getApprovedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        QuotaCommand.ActivateQuotaCommand dto1 = new QuotaCommand.ActivateQuotaCommand();
        QuotaCommand.ActivateQuotaCommand dto2 = new QuotaCommand.ActivateQuotaCommand();
        dto1.setTenantId("test");
        dto1.setQuotaId("test");
        dto1.setApprovedBy("test");
        dto2.setTenantId("test");
        dto2.setQuotaId("test");
        dto2.setApprovedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        QuotaCommand.ActivateQuotaCommand dto = new QuotaCommand.ActivateQuotaCommand();
        dto.setTenantId("test");
        dto.setQuotaId("test");
        dto.setApprovedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        QuotaCommand.ActivateQuotaCommand dto = new QuotaCommand.ActivateQuotaCommand();
        dto.setTenantId("test");
        dto.setQuotaId("test");
        dto.setApprovedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}