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
class RequirementCommand_DeactivateRequirementCommandTest {

        @Test
    void testSettersAndGetters() {
        RequirementCommand.DeactivateRequirementCommand dto = new RequirementCommand.DeactivateRequirementCommand();
        dto.setTenantId("val-tenantId");
        dto.setRequirementId("val-requirementId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requirementId", dto.getRequirementId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        RequirementCommand.DeactivateRequirementCommand dto1 = new RequirementCommand.DeactivateRequirementCommand();
        RequirementCommand.DeactivateRequirementCommand dto2 = new RequirementCommand.DeactivateRequirementCommand();
        dto1.setTenantId("test");
        dto1.setRequirementId("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setRequirementId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RequirementCommand.DeactivateRequirementCommand dto = new RequirementCommand.DeactivateRequirementCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RequirementCommand.DeactivateRequirementCommand dto = new RequirementCommand.DeactivateRequirementCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}