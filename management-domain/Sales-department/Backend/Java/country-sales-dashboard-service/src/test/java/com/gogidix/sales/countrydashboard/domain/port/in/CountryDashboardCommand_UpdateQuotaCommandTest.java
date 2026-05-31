package com.gogidix.sales.countrydashboard.domain.port.in;

import com.gogidix.sales.countrydashboard.domain.port.in.CountryDashboardCommand;
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
class CountryDashboardCommand_UpdateQuotaCommandTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardCommand.UpdateQuotaCommand dto = new CountryDashboardCommand.UpdateQuotaCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setAnnualQuota(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals(BigDecimal.ONE, dto.getAnnualQuota());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardCommand.UpdateQuotaCommand dto1 = new CountryDashboardCommand.UpdateQuotaCommand();
        CountryDashboardCommand.UpdateQuotaCommand dto2 = new CountryDashboardCommand.UpdateQuotaCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setAnnualQuota(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setAnnualQuota(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardCommand.UpdateQuotaCommand dto = new CountryDashboardCommand.UpdateQuotaCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setAnnualQuota(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardCommand.UpdateQuotaCommand dto = new CountryDashboardCommand.UpdateQuotaCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setAnnualQuota(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}