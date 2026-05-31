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
class TerritoryCommand_CompleteRealignmentCommandTest {

        @Test
    void testSettersAndGetters() {
        TerritoryCommand.CompleteRealignmentCommand dto = new TerritoryCommand.CompleteRealignmentCommand();
        dto.setTenantId("val-tenantId");
        dto.setTerritoryId("val-territoryId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-territoryId", dto.getTerritoryId());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryCommand.CompleteRealignmentCommand dto1 = new TerritoryCommand.CompleteRealignmentCommand();
        TerritoryCommand.CompleteRealignmentCommand dto2 = new TerritoryCommand.CompleteRealignmentCommand();
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
        TerritoryCommand.CompleteRealignmentCommand dto = new TerritoryCommand.CompleteRealignmentCommand();
        dto.setTenantId("test");
        dto.setTerritoryId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TerritoryCommand.CompleteRealignmentCommand dto = new TerritoryCommand.CompleteRealignmentCommand();
        dto.setTenantId("test");
        dto.setTerritoryId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}