package com.gogidix.sales.countrydashboard.domain.port.in;

import com.gogidix.sales.countrydashboard.domain.port.in.CountryDashboardCommand;
import com.gogidix.sales.countrydashboard.domain.valueobject.TimePeriod;
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
class CountryDashboardCommand_GenerateComparisonCommandTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardCommand.GenerateComparisonCommand dto = new CountryDashboardCommand.GenerateComparisonCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setComparisonType("val-comparisonType");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-comparisonType", dto.getComparisonType());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardCommand.GenerateComparisonCommand dto1 = new CountryDashboardCommand.GenerateComparisonCommand();
        CountryDashboardCommand.GenerateComparisonCommand dto2 = new CountryDashboardCommand.GenerateComparisonCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setComparisonType("test");
        dto1.setCompareToCountries(Collections.emptyList());
        dto1.setPeriodType(TimePeriod.PeriodType.DAILY);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setComparisonType("test");
        dto2.setCompareToCountries(Collections.emptyList());
        dto2.setPeriodType(TimePeriod.PeriodType.DAILY);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardCommand.GenerateComparisonCommand dto = new CountryDashboardCommand.GenerateComparisonCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setComparisonType("test");
        dto.setCompareToCountries(Collections.emptyList());
        dto.setPeriodType(TimePeriod.PeriodType.DAILY);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardCommand.GenerateComparisonCommand dto = new CountryDashboardCommand.GenerateComparisonCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setComparisonType("test");
        dto.setCompareToCountries(Collections.emptyList());
        dto.setPeriodType(TimePeriod.PeriodType.DAILY);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}