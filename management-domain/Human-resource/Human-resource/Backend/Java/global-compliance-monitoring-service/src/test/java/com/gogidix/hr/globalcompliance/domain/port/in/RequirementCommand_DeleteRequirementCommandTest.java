package com.gogidix.hr.globalcompliance.domain.port.in;

import com.gogidix.hr.globalcompliance.domain.port.in.RequirementCommand;
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
class RequirementCommand_DeleteRequirementCommandTest {

        @Test
    void testSettersAndGetters() {
        RequirementCommand.DeleteRequirementCommand dto = new RequirementCommand.DeleteRequirementCommand();
        dto.setTenantId("val-tenantId");
        dto.setRequirementId("val-requirementId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requirementId", dto.getRequirementId());
    }

    @Test
    void testEqualsAndHashCode() {
        RequirementCommand.DeleteRequirementCommand dto1 = new RequirementCommand.DeleteRequirementCommand();
        RequirementCommand.DeleteRequirementCommand dto2 = new RequirementCommand.DeleteRequirementCommand();
        dto1.setTenantId("test");
        dto1.setRequirementId("test");
        dto2.setTenantId("test");
        dto2.setRequirementId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RequirementCommand.DeleteRequirementCommand dto = new RequirementCommand.DeleteRequirementCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RequirementCommand.DeleteRequirementCommand dto = new RequirementCommand.DeleteRequirementCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}