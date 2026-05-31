package com.gogidix.hr.globalcompliance.domain.model;

import com.gogidix.hr.globalcompliance.domain.model.ComplianceRequirement;
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
class ComplianceRequirement_ComplianceCheckInfoTest {

        @Test
    void testBuilder() {
        ComplianceRequirement.ComplianceCheckInfo dto = ComplianceRequirement.ComplianceCheckInfo.builder()
                        .checkId("test-checkId")
            .scheduledDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .build();
        assertNotNull(dto);
        assertEquals("test-checkId", dto.getCheckId());
        assertEquals(LocalDate.of(2025,1,15), dto.getScheduledDate());
        assertEquals("test-status", dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        ComplianceRequirement.ComplianceCheckInfo dto = new ComplianceRequirement.ComplianceCheckInfo();
        dto.setCheckId("val-checkId");
        dto.setScheduledDate(LocalDate.of(2025,6,1));
        dto.setStatus("val-status");
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals(LocalDate.of(2025,6,1), dto.getScheduledDate());
        assertEquals("val-status", dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceRequirement.ComplianceCheckInfo dto1 = ComplianceRequirement.ComplianceCheckInfo.builder()
                        .checkId("test-checkId")
            .scheduledDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .build();
        ComplianceRequirement.ComplianceCheckInfo dto2 = ComplianceRequirement.ComplianceCheckInfo.builder()
                        .checkId("test-checkId")
            .scheduledDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceRequirement.ComplianceCheckInfo dto = ComplianceRequirement.ComplianceCheckInfo.builder()
                        .checkId("test-checkId")
            .scheduledDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}