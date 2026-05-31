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
class TerritoryCommand_UpdateTerritoryCommandTest {

        @Test
    void testSettersAndGetters() {
        TerritoryCommand.UpdateTerritoryCommand dto = new TerritoryCommand.UpdateTerritoryCommand();
        dto.setTenantId("val-tenantId");
        dto.setTerritoryId("val-territoryId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setRegionId("val-regionId");
        dto.setManagerId("val-managerId");
        dto.setPriority(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-territoryId", dto.getTerritoryId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-regionId", dto.getRegionId());
        assertEquals("val-managerId", dto.getManagerId());
        assertEquals(99, dto.getPriority());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryCommand.UpdateTerritoryCommand dto1 = new TerritoryCommand.UpdateTerritoryCommand();
        TerritoryCommand.UpdateTerritoryCommand dto2 = new TerritoryCommand.UpdateTerritoryCommand();
        dto1.setTenantId("test");
        dto1.setTerritoryId("test");
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setRegionId("test");
        dto1.setManagerId("test");
        dto1.setPriority(42);
        dto1.setGeographicBoundary(null);
        dto1.setProductCategories(Collections.emptyList());
        dto1.setProductIds(Collections.emptyList());
        dto1.setCustomerSegments(Collections.emptyList());
        dto1.setCustomerTierIds(Collections.emptyList());
        dto2.setTenantId("test");
        dto2.setTerritoryId("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setRegionId("test");
        dto2.setManagerId("test");
        dto2.setPriority(42);
        dto2.setGeographicBoundary(null);
        dto2.setProductCategories(Collections.emptyList());
        dto2.setProductIds(Collections.emptyList());
        dto2.setCustomerSegments(Collections.emptyList());
        dto2.setCustomerTierIds(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryCommand.UpdateTerritoryCommand dto = new TerritoryCommand.UpdateTerritoryCommand();
        dto.setTenantId("test");
        dto.setTerritoryId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setRegionId("test");
        dto.setManagerId("test");
        dto.setPriority(42);
        dto.setGeographicBoundary(null);
        dto.setProductCategories(Collections.emptyList());
        dto.setProductIds(Collections.emptyList());
        dto.setCustomerSegments(Collections.emptyList());
        dto.setCustomerTierIds(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TerritoryCommand.UpdateTerritoryCommand dto = new TerritoryCommand.UpdateTerritoryCommand();
        dto.setTenantId("test");
        dto.setTerritoryId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setRegionId("test");
        dto.setManagerId("test");
        dto.setPriority(42);
        dto.setGeographicBoundary(null);
        dto.setProductCategories(Collections.emptyList());
        dto.setProductIds(Collections.emptyList());
        dto.setCustomerSegments(Collections.emptyList());
        dto.setCustomerTierIds(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}