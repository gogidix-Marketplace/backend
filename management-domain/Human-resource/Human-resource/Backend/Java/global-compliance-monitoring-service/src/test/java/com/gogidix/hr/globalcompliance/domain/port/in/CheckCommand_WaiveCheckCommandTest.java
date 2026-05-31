package com.gogidix.hr.globalcompliance.domain.port.in;

import com.gogidix.hr.globalcompliance.domain.port.in.CheckCommand;
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
class CheckCommand_WaiveCheckCommandTest {

        @Test
    void testSettersAndGetters() {
        CheckCommand.WaiveCheckCommand dto = new CheckCommand.WaiveCheckCommand();
        dto.setTenantId("val-tenantId");
        dto.setCheckId("val-checkId");
        dto.setReason("val-reason");
        dto.setWaivedBy("val-waivedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-waivedBy", dto.getWaivedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        CheckCommand.WaiveCheckCommand dto1 = new CheckCommand.WaiveCheckCommand();
        CheckCommand.WaiveCheckCommand dto2 = new CheckCommand.WaiveCheckCommand();
        dto1.setTenantId("test");
        dto1.setCheckId("test");
        dto1.setReason("test");
        dto1.setWaivedBy("test");
        dto2.setTenantId("test");
        dto2.setCheckId("test");
        dto2.setReason("test");
        dto2.setWaivedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CheckCommand.WaiveCheckCommand dto = new CheckCommand.WaiveCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setReason("test");
        dto.setWaivedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CheckCommand.WaiveCheckCommand dto = new CheckCommand.WaiveCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setReason("test");
        dto.setWaivedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}