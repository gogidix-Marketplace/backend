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
class RequirementCommand_CreateRequirementCommandTest {

        @Test
    void testSettersAndGetters() {
        RequirementCommand.CreateRequirementCommand dto = new RequirementCommand.CreateRequirementCommand();
        dto.setTenantId("val-tenantId");
        dto.setRequirementCode("val-requirementCode");
        dto.setRequirementName("val-requirementName");
        dto.setCategory("val-category");
        dto.setCountryCode("val-countryCode");
        dto.setDescription("val-description");
        dto.setAuthority("val-authority");
        dto.setType("val-type");
        dto.setEffectiveFrom(LocalDate.of(2025,6,1));
        dto.setEffectiveTo(LocalDate.of(2025,6,1));
        dto.setFrequency("val-frequency");
        dto.setSeverity("val-severity");
        dto.setOwnerDepartment("val-ownerDepartment");
        dto.setOwnerId("val-ownerId");
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requirementCode", dto.getRequirementCode());
        assertEquals("val-requirementName", dto.getRequirementName());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-authority", dto.getAuthority());
        assertEquals("val-type", dto.getType());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveFrom());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveTo());
        assertEquals("val-frequency", dto.getFrequency());
        assertEquals("val-severity", dto.getSeverity());
        assertEquals("val-ownerDepartment", dto.getOwnerDepartment());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        RequirementCommand.CreateRequirementCommand dto1 = new RequirementCommand.CreateRequirementCommand();
        RequirementCommand.CreateRequirementCommand dto2 = new RequirementCommand.CreateRequirementCommand();
        dto1.setTenantId("test");
        dto1.setRequirementCode("test");
        dto1.setRequirementName("test");
        dto1.setCategory("test");
        dto1.setCountryCode("test");
        dto1.setDescription("test");
        dto1.setAuthority("test");
        dto1.setType("test");
        dto1.setEffectiveFrom(LocalDate.of(2025,1,1));
        dto1.setEffectiveTo(LocalDate.of(2025,1,1));
        dto1.setFrequency("test");
        dto1.setSeverity("test");
        dto1.setOwnerDepartment("test");
        dto1.setOwnerId("test");
        dto1.setRelatedRequirements(Collections.emptyList());
        dto1.setCreatedBy("test");
        dto2.setTenantId("test");
        dto2.setRequirementCode("test");
        dto2.setRequirementName("test");
        dto2.setCategory("test");
        dto2.setCountryCode("test");
        dto2.setDescription("test");
        dto2.setAuthority("test");
        dto2.setType("test");
        dto2.setEffectiveFrom(LocalDate.of(2025,1,1));
        dto2.setEffectiveTo(LocalDate.of(2025,1,1));
        dto2.setFrequency("test");
        dto2.setSeverity("test");
        dto2.setOwnerDepartment("test");
        dto2.setOwnerId("test");
        dto2.setRelatedRequirements(Collections.emptyList());
        dto2.setCreatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RequirementCommand.CreateRequirementCommand dto = new RequirementCommand.CreateRequirementCommand();
        dto.setTenantId("test");
        dto.setRequirementCode("test");
        dto.setRequirementName("test");
        dto.setCategory("test");
        dto.setCountryCode("test");
        dto.setDescription("test");
        dto.setAuthority("test");
        dto.setType("test");
        dto.setEffectiveFrom(LocalDate.of(2025,1,1));
        dto.setEffectiveTo(LocalDate.of(2025,1,1));
        dto.setFrequency("test");
        dto.setSeverity("test");
        dto.setOwnerDepartment("test");
        dto.setOwnerId("test");
        dto.setRelatedRequirements(Collections.emptyList());
        dto.setCreatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RequirementCommand.CreateRequirementCommand dto = new RequirementCommand.CreateRequirementCommand();
        dto.setTenantId("test");
        dto.setRequirementCode("test");
        dto.setRequirementName("test");
        dto.setCategory("test");
        dto.setCountryCode("test");
        dto.setDescription("test");
        dto.setAuthority("test");
        dto.setType("test");
        dto.setEffectiveFrom(LocalDate.of(2025,1,1));
        dto.setEffectiveTo(LocalDate.of(2025,1,1));
        dto.setFrequency("test");
        dto.setSeverity("test");
        dto.setOwnerDepartment("test");
        dto.setOwnerId("test");
        dto.setRelatedRequirements(Collections.emptyList());
        dto.setCreatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}