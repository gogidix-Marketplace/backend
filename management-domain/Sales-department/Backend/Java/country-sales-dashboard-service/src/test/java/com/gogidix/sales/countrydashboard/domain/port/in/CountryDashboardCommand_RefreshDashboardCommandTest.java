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
class CountryDashboardCommand_RefreshDashboardCommandTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardCommand.RefreshDashboardCommand dto = new CountryDashboardCommand.RefreshDashboardCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setUserId("val-userId");
        dto.setForceRefresh(true);
        dto.setCalculateYoY(true);
        dto.setCalculateMoM(true);
        dto.setCalculateQoQ(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-userId", dto.getUserId());
        assertTrue(dto.getForceRefresh());
        assertTrue(dto.getCalculateYoY());
        assertTrue(dto.getCalculateMoM());
        assertTrue(dto.getCalculateQoQ());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardCommand.RefreshDashboardCommand dto1 = new CountryDashboardCommand.RefreshDashboardCommand();
        CountryDashboardCommand.RefreshDashboardCommand dto2 = new CountryDashboardCommand.RefreshDashboardCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setUserId("test");
        dto1.setForceRefresh(true);
        dto1.setCalculateYoY(true);
        dto1.setCalculateMoM(true);
        dto1.setCalculateQoQ(true);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setUserId("test");
        dto2.setForceRefresh(true);
        dto2.setCalculateYoY(true);
        dto2.setCalculateMoM(true);
        dto2.setCalculateQoQ(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardCommand.RefreshDashboardCommand dto = new CountryDashboardCommand.RefreshDashboardCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setUserId("test");
        dto.setForceRefresh(true);
        dto.setCalculateYoY(true);
        dto.setCalculateMoM(true);
        dto.setCalculateQoQ(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardCommand.RefreshDashboardCommand dto = new CountryDashboardCommand.RefreshDashboardCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setUserId("test");
        dto.setForceRefresh(true);
        dto.setCalculateYoY(true);
        dto.setCalculateMoM(true);
        dto.setCalculateQoQ(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}