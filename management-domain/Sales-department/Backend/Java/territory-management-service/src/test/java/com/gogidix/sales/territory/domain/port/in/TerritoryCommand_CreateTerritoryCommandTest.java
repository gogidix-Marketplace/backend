package com.gogidix.sales.territory.domain.port.in;

import com.gogidix.sales.territory.domain.model.Territory;
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
class TerritoryCommand_CreateTerritoryCommandTest {

        @Test
    void testSettersAndGetters() {
        TerritoryCommand.CreateTerritoryCommand dto = new TerritoryCommand.CreateTerritoryCommand();
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setCode("val-code");
        dto.setDescription("val-description");
        dto.setRegionId("val-regionId");
        dto.setManagerId("val-managerId");
        dto.setPriority(99);
        dto.setParentTerritoryId("val-parentTerritoryId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-code", dto.getCode());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-regionId", dto.getRegionId());
        assertEquals("val-managerId", dto.getManagerId());
        assertEquals(99, dto.getPriority());
        assertEquals("val-parentTerritoryId", dto.getParentTerritoryId());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryCommand.CreateTerritoryCommand dto1 = new TerritoryCommand.CreateTerritoryCommand();
        TerritoryCommand.CreateTerritoryCommand dto2 = new TerritoryCommand.CreateTerritoryCommand();
        dto1.setTenantId("test");
        dto1.setName("test");
        dto1.setCode("test");
        dto1.setType(Territory.TerritoryType.GEOGRAPHIC);
        dto1.setDescription("test");
        dto1.setRegionId("test");
        dto1.setManagerId("test");
        dto1.setPriority(42);
        dto1.setParentTerritoryId("test");
        dto1.setGeographicBoundary(null);
        dto1.setProductCategories(Collections.emptyList());
        dto1.setProductIds(Collections.emptyList());
        dto1.setCustomerSegments(Collections.emptyList());
        dto1.setCustomerTierIds(Collections.emptyList());
        dto2.setTenantId("test");
        dto2.setName("test");
        dto2.setCode("test");
        dto2.setType(Territory.TerritoryType.GEOGRAPHIC);
        dto2.setDescription("test");
        dto2.setRegionId("test");
        dto2.setManagerId("test");
        dto2.setPriority(42);
        dto2.setParentTerritoryId("test");
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
        TerritoryCommand.CreateTerritoryCommand dto = new TerritoryCommand.CreateTerritoryCommand();
        dto.setTenantId("test");
        dto.setName("test");
        dto.setCode("test");
        dto.setType(Territory.TerritoryType.GEOGRAPHIC);
        dto.setDescription("test");
        dto.setRegionId("test");
        dto.setManagerId("test");
        dto.setPriority(42);
        dto.setParentTerritoryId("test");
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
        TerritoryCommand.CreateTerritoryCommand dto = new TerritoryCommand.CreateTerritoryCommand();
        dto.setTenantId("test");
        dto.setName("test");
        dto.setCode("test");
        dto.setType(Territory.TerritoryType.GEOGRAPHIC);
        dto.setDescription("test");
        dto.setRegionId("test");
        dto.setManagerId("test");
        dto.setPriority(42);
        dto.setParentTerritoryId("test");
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