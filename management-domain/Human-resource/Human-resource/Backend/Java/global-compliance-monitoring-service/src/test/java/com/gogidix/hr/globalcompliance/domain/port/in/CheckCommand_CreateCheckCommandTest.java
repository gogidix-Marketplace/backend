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
class CheckCommand_CreateCheckCommandTest {

        @Test
    void testSettersAndGetters() {
        CheckCommand.CreateCheckCommand dto = new CheckCommand.CreateCheckCommand();
        dto.setTenantId("val-tenantId");
        dto.setRequirementId("val-requirementId");
        dto.setRequirementName("val-requirementName");
        dto.setCountryCode("val-countryCode");
        dto.setScheduledDate(LocalDate.of(2025,6,1));
        dto.setFrequency("val-frequency");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requirementId", dto.getRequirementId());
        assertEquals("val-requirementName", dto.getRequirementName());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals(LocalDate.of(2025,6,1), dto.getScheduledDate());
        assertEquals("val-frequency", dto.getFrequency());
    }

    @Test
    void testEqualsAndHashCode() {
        CheckCommand.CreateCheckCommand dto1 = new CheckCommand.CreateCheckCommand();
        CheckCommand.CreateCheckCommand dto2 = new CheckCommand.CreateCheckCommand();
        dto1.setTenantId("test");
        dto1.setRequirementId("test");
        dto1.setRequirementName("test");
        dto1.setCountryCode("test");
        dto1.setScheduledDate(LocalDate.of(2025,1,1));
        dto1.setFrequency("test");
        dto2.setTenantId("test");
        dto2.setRequirementId("test");
        dto2.setRequirementName("test");
        dto2.setCountryCode("test");
        dto2.setScheduledDate(LocalDate.of(2025,1,1));
        dto2.setFrequency("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CheckCommand.CreateCheckCommand dto = new CheckCommand.CreateCheckCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        dto.setRequirementName("test");
        dto.setCountryCode("test");
        dto.setScheduledDate(LocalDate.of(2025,1,1));
        dto.setFrequency("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CheckCommand.CreateCheckCommand dto = new CheckCommand.CreateCheckCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        dto.setRequirementName("test");
        dto.setCountryCode("test");
        dto.setScheduledDate(LocalDate.of(2025,1,1));
        dto.setFrequency("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}