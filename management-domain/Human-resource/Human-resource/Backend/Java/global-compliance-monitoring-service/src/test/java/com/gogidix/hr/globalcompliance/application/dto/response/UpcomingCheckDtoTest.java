package com.gogidix.hr.globalcompliance.application.dto.response;

import com.gogidix.hr.globalcompliance.application.dto.response.UpcomingCheckDto;
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
class UpcomingCheckDtoTest {

        @Test
    void testBuilder() {
        UpcomingCheckDto dto = UpcomingCheckDto.builder()
                        .checkId("test-checkId")
            .checkNumber("test-checkNumber")
            .requirementId("test-requirementId")
            .requirementName("test-requirementName")
            .scheduledDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .countryCode("test-countryCode")
            .daysUntilScheduled(42L)
            .build();
        assertNotNull(dto);
        assertEquals("test-checkId", dto.getCheckId());
        assertEquals("test-checkNumber", dto.getCheckNumber());
        assertEquals("test-requirementId", dto.getRequirementId());
        assertEquals("test-requirementName", dto.getRequirementName());
        assertEquals(LocalDate.of(2025,1,15), dto.getScheduledDate());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals(42L, dto.getDaysUntilScheduled());
    }

    @Test
    void testSettersAndGetters() {
        UpcomingCheckDto dto = new UpcomingCheckDto();
        dto.setCheckId("val-checkId");
        dto.setCheckNumber("val-checkNumber");
        dto.setRequirementId("val-requirementId");
        dto.setRequirementName("val-requirementName");
        dto.setScheduledDate(LocalDate.of(2025,6,1));
        dto.setStatus("val-status");
        dto.setCountryCode("val-countryCode");
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-checkNumber", dto.getCheckNumber());
        assertEquals("val-requirementId", dto.getRequirementId());
        assertEquals("val-requirementName", dto.getRequirementName());
        assertEquals(LocalDate.of(2025,6,1), dto.getScheduledDate());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-countryCode", dto.getCountryCode());
    }

    @Test
    void testEqualsAndHashCode() {
        UpcomingCheckDto dto1 = UpcomingCheckDto.builder()
                        .checkId("test-checkId")
            .checkNumber("test-checkNumber")
            .requirementId("test-requirementId")
            .requirementName("test-requirementName")
            .scheduledDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .countryCode("test-countryCode")
            .daysUntilScheduled(42L)
            .build();
        UpcomingCheckDto dto2 = UpcomingCheckDto.builder()
                        .checkId("test-checkId")
            .checkNumber("test-checkNumber")
            .requirementId("test-requirementId")
            .requirementName("test-requirementName")
            .scheduledDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .countryCode("test-countryCode")
            .daysUntilScheduled(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpcomingCheckDto dto = UpcomingCheckDto.builder()
                        .checkId("test-checkId")
            .checkNumber("test-checkNumber")
            .requirementId("test-requirementId")
            .requirementName("test-requirementName")
            .scheduledDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .countryCode("test-countryCode")
            .daysUntilScheduled(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}