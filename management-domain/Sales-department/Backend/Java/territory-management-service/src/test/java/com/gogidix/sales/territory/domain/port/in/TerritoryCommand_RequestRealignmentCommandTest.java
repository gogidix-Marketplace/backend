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
class TerritoryCommand_RequestRealignmentCommandTest {

        @Test
    void testSettersAndGetters() {
        TerritoryCommand.RequestRealignmentCommand dto = new TerritoryCommand.RequestRealignmentCommand();
        dto.setTenantId("val-tenantId");
        dto.setTerritoryId("val-territoryId");
        dto.setRequestedBy("val-requestedBy");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-territoryId", dto.getTerritoryId());
        assertEquals("val-requestedBy", dto.getRequestedBy());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryCommand.RequestRealignmentCommand dto1 = new TerritoryCommand.RequestRealignmentCommand();
        TerritoryCommand.RequestRealignmentCommand dto2 = new TerritoryCommand.RequestRealignmentCommand();
        dto1.setTenantId("test");
        dto1.setTerritoryId("test");
        dto1.setRequestedBy("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setTerritoryId("test");
        dto2.setRequestedBy("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryCommand.RequestRealignmentCommand dto = new TerritoryCommand.RequestRealignmentCommand();
        dto.setTenantId("test");
        dto.setTerritoryId("test");
        dto.setRequestedBy("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TerritoryCommand.RequestRealignmentCommand dto = new TerritoryCommand.RequestRealignmentCommand();
        dto.setTenantId("test");
        dto.setTerritoryId("test");
        dto.setRequestedBy("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}