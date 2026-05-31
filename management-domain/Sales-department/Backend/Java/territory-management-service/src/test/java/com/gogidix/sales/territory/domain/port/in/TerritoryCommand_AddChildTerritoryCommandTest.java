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
class TerritoryCommand_AddChildTerritoryCommandTest {

        @Test
    void testSettersAndGetters() {
        TerritoryCommand.AddChildTerritoryCommand dto = new TerritoryCommand.AddChildTerritoryCommand();
        dto.setTenantId("val-tenantId");
        dto.setParentTerritoryId("val-parentTerritoryId");
        dto.setChildTerritoryId("val-childTerritoryId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-parentTerritoryId", dto.getParentTerritoryId());
        assertEquals("val-childTerritoryId", dto.getChildTerritoryId());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryCommand.AddChildTerritoryCommand dto1 = new TerritoryCommand.AddChildTerritoryCommand();
        TerritoryCommand.AddChildTerritoryCommand dto2 = new TerritoryCommand.AddChildTerritoryCommand();
        dto1.setTenantId("test");
        dto1.setParentTerritoryId("test");
        dto1.setChildTerritoryId("test");
        dto2.setTenantId("test");
        dto2.setParentTerritoryId("test");
        dto2.setChildTerritoryId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryCommand.AddChildTerritoryCommand dto = new TerritoryCommand.AddChildTerritoryCommand();
        dto.setTenantId("test");
        dto.setParentTerritoryId("test");
        dto.setChildTerritoryId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TerritoryCommand.AddChildTerritoryCommand dto = new TerritoryCommand.AddChildTerritoryCommand();
        dto.setTenantId("test");
        dto.setParentTerritoryId("test");
        dto.setChildTerritoryId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}