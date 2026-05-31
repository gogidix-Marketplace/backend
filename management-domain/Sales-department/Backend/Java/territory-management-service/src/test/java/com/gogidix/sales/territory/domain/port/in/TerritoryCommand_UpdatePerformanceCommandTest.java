package com.gogidix.sales.territory.domain.port.in;

import com.gogidix.sales.territory.domain.port.in.TerritoryCommand;
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
class TerritoryCommand_UpdatePerformanceCommandTest {

        @Test
    void testSettersAndGetters() {
        TerritoryCommand.UpdatePerformanceCommand dto = new TerritoryCommand.UpdatePerformanceCommand();
        dto.setTenantId("val-tenantId");
        dto.setTerritoryId("val-territoryId");
        dto.setAccountsCount(99);
        dto.setDealsCount(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-territoryId", dto.getTerritoryId());
        assertEquals(99, dto.getAccountsCount());
        assertEquals(99, dto.getDealsCount());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryCommand.UpdatePerformanceCommand dto1 = new TerritoryCommand.UpdatePerformanceCommand();
        TerritoryCommand.UpdatePerformanceCommand dto2 = new TerritoryCommand.UpdatePerformanceCommand();
        dto1.setTenantId("test");
        dto1.setTerritoryId("test");
        dto1.setCurrentSales(null);
        dto1.setQuota(null);
        dto1.setAccountsCount(42);
        dto1.setDealsCount(42);
        dto2.setTenantId("test");
        dto2.setTerritoryId("test");
        dto2.setCurrentSales(null);
        dto2.setQuota(null);
        dto2.setAccountsCount(42);
        dto2.setDealsCount(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryCommand.UpdatePerformanceCommand dto = new TerritoryCommand.UpdatePerformanceCommand();
        dto.setTenantId("test");
        dto.setTerritoryId("test");
        dto.setCurrentSales(null);
        dto.setQuota(null);
        dto.setAccountsCount(42);
        dto.setDealsCount(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TerritoryCommand.UpdatePerformanceCommand dto = new TerritoryCommand.UpdatePerformanceCommand();
        dto.setTenantId("test");
        dto.setTerritoryId("test");
        dto.setCurrentSales(null);
        dto.setQuota(null);
        dto.setAccountsCount(42);
        dto.setDealsCount(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}