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
class QuotaCommand_DeleteQuotaCommandTest {

        @Test
    void testSettersAndGetters() {
        QuotaCommand.DeleteQuotaCommand dto = new QuotaCommand.DeleteQuotaCommand();
        dto.setTenantId("val-tenantId");
        dto.setQuotaId("val-quotaId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-quotaId", dto.getQuotaId());
    }

    @Test
    void testEqualsAndHashCode() {
        QuotaCommand.DeleteQuotaCommand dto1 = new QuotaCommand.DeleteQuotaCommand();
        QuotaCommand.DeleteQuotaCommand dto2 = new QuotaCommand.DeleteQuotaCommand();
        dto1.setTenantId("test");
        dto1.setQuotaId("test");
        dto2.setTenantId("test");
        dto2.setQuotaId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        QuotaCommand.DeleteQuotaCommand dto = new QuotaCommand.DeleteQuotaCommand();
        dto.setTenantId("test");
        dto.setQuotaId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        QuotaCommand.DeleteQuotaCommand dto = new QuotaCommand.DeleteQuotaCommand();
        dto.setTenantId("test");
        dto.setQuotaId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}