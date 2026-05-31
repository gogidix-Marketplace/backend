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
class RequirementCommand_UpdateRequirementCommandTest {

        @Test
    void testSettersAndGetters() {
        RequirementCommand.UpdateRequirementCommand dto = new RequirementCommand.UpdateRequirementCommand();
        dto.setTenantId("val-tenantId");
        dto.setRequirementId("val-requirementId");
        dto.setRequirementName("val-requirementName");
        dto.setDescription("val-description");
        dto.setAuthority("val-authority");
        dto.setOwnerDepartment("val-ownerDepartment");
        dto.setOwnerId("val-ownerId");
        dto.setEffectiveTo(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requirementId", dto.getRequirementId());
        assertEquals("val-requirementName", dto.getRequirementName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-authority", dto.getAuthority());
        assertEquals("val-ownerDepartment", dto.getOwnerDepartment());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveTo());
    }

    @Test
    void testEqualsAndHashCode() {
        RequirementCommand.UpdateRequirementCommand dto1 = new RequirementCommand.UpdateRequirementCommand();
        RequirementCommand.UpdateRequirementCommand dto2 = new RequirementCommand.UpdateRequirementCommand();
        dto1.setTenantId("test");
        dto1.setRequirementId("test");
        dto1.setRequirementName("test");
        dto1.setDescription("test");
        dto1.setAuthority("test");
        dto1.setOwnerDepartment("test");
        dto1.setOwnerId("test");
        dto1.setEffectiveTo(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setRequirementId("test");
        dto2.setRequirementName("test");
        dto2.setDescription("test");
        dto2.setAuthority("test");
        dto2.setOwnerDepartment("test");
        dto2.setOwnerId("test");
        dto2.setEffectiveTo(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RequirementCommand.UpdateRequirementCommand dto = new RequirementCommand.UpdateRequirementCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        dto.setRequirementName("test");
        dto.setDescription("test");
        dto.setAuthority("test");
        dto.setOwnerDepartment("test");
        dto.setOwnerId("test");
        dto.setEffectiveTo(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RequirementCommand.UpdateRequirementCommand dto = new RequirementCommand.UpdateRequirementCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        dto.setRequirementName("test");
        dto.setDescription("test");
        dto.setAuthority("test");
        dto.setOwnerDepartment("test");
        dto.setOwnerId("test");
        dto.setEffectiveTo(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}