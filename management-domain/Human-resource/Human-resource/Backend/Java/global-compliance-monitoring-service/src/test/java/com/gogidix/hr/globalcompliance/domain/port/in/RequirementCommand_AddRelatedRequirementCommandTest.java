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
class RequirementCommand_AddRelatedRequirementCommandTest {

        @Test
    void testSettersAndGetters() {
        RequirementCommand.AddRelatedRequirementCommand dto = new RequirementCommand.AddRelatedRequirementCommand();
        dto.setTenantId("val-tenantId");
        dto.setRequirementId("val-requirementId");
        dto.setRelatedRequirementId("val-relatedRequirementId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requirementId", dto.getRequirementId());
        assertEquals("val-relatedRequirementId", dto.getRelatedRequirementId());
    }

    @Test
    void testEqualsAndHashCode() {
        RequirementCommand.AddRelatedRequirementCommand dto1 = new RequirementCommand.AddRelatedRequirementCommand();
        RequirementCommand.AddRelatedRequirementCommand dto2 = new RequirementCommand.AddRelatedRequirementCommand();
        dto1.setTenantId("test");
        dto1.setRequirementId("test");
        dto1.setRelatedRequirementId("test");
        dto2.setTenantId("test");
        dto2.setRequirementId("test");
        dto2.setRelatedRequirementId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RequirementCommand.AddRelatedRequirementCommand dto = new RequirementCommand.AddRelatedRequirementCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        dto.setRelatedRequirementId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RequirementCommand.AddRelatedRequirementCommand dto = new RequirementCommand.AddRelatedRequirementCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        dto.setRelatedRequirementId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}