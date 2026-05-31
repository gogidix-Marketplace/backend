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
class CheckCommand_StartCheckCommandTest {

        @Test
    void testSettersAndGetters() {
        CheckCommand.StartCheckCommand dto = new CheckCommand.StartCheckCommand();
        dto.setTenantId("val-tenantId");
        dto.setCheckId("val-checkId");
        dto.setCheckedBy("val-checkedBy");
        dto.setCheckedByName("val-checkedByName");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-checkedBy", dto.getCheckedBy());
        assertEquals("val-checkedByName", dto.getCheckedByName());
    }

    @Test
    void testEqualsAndHashCode() {
        CheckCommand.StartCheckCommand dto1 = new CheckCommand.StartCheckCommand();
        CheckCommand.StartCheckCommand dto2 = new CheckCommand.StartCheckCommand();
        dto1.setTenantId("test");
        dto1.setCheckId("test");
        dto1.setCheckedBy("test");
        dto1.setCheckedByName("test");
        dto2.setTenantId("test");
        dto2.setCheckId("test");
        dto2.setCheckedBy("test");
        dto2.setCheckedByName("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CheckCommand.StartCheckCommand dto = new CheckCommand.StartCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setCheckedBy("test");
        dto.setCheckedByName("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CheckCommand.StartCheckCommand dto = new CheckCommand.StartCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setCheckedBy("test");
        dto.setCheckedByName("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}