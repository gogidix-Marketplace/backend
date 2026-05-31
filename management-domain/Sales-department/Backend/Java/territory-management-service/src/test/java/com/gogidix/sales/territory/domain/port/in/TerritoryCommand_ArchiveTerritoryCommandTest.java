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
class TerritoryCommand_ArchiveTerritoryCommandTest {

        @Test
    void testSettersAndGetters() {
        TerritoryCommand.ArchiveTerritoryCommand dto = new TerritoryCommand.ArchiveTerritoryCommand();
        dto.setTenantId("val-tenantId");
        dto.setTerritoryId("val-territoryId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-territoryId", dto.getTerritoryId());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryCommand.ArchiveTerritoryCommand dto1 = new TerritoryCommand.ArchiveTerritoryCommand();
        TerritoryCommand.ArchiveTerritoryCommand dto2 = new TerritoryCommand.ArchiveTerritoryCommand();
        dto1.setTenantId("test");
        dto1.setTerritoryId("test");
        dto2.setTenantId("test");
        dto2.setTerritoryId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryCommand.ArchiveTerritoryCommand dto = new TerritoryCommand.ArchiveTerritoryCommand();
        dto.setTenantId("test");
        dto.setTerritoryId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TerritoryCommand.ArchiveTerritoryCommand dto = new TerritoryCommand.ArchiveTerritoryCommand();
        dto.setTenantId("test");
        dto.setTerritoryId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}