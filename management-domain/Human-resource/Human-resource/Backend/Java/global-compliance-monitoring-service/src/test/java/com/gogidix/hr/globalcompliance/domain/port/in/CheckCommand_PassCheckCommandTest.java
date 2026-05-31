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
class CheckCommand_PassCheckCommandTest {

        @Test
    void testSettersAndGetters() {
        CheckCommand.PassCheckCommand dto = new CheckCommand.PassCheckCommand();
        dto.setTenantId("val-tenantId");
        dto.setCheckId("val-checkId");
        dto.setFindings("val-findings");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-findings", dto.getFindings());
    }

    @Test
    void testEqualsAndHashCode() {
        CheckCommand.PassCheckCommand dto1 = new CheckCommand.PassCheckCommand();
        CheckCommand.PassCheckCommand dto2 = new CheckCommand.PassCheckCommand();
        dto1.setTenantId("test");
        dto1.setCheckId("test");
        dto1.setFindings("test");
        dto1.setSupportingDocuments(Collections.emptyList());
        dto2.setTenantId("test");
        dto2.setCheckId("test");
        dto2.setFindings("test");
        dto2.setSupportingDocuments(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CheckCommand.PassCheckCommand dto = new CheckCommand.PassCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setFindings("test");
        dto.setSupportingDocuments(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CheckCommand.PassCheckCommand dto = new CheckCommand.PassCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setFindings("test");
        dto.setSupportingDocuments(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}