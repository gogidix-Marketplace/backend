package com.gogidix.hr.countryhrmanagement.domain.model;

import com.gogidix.hr.countryhrmanagement.domain.model.WorkingHoursConfig;
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
class WorkingHoursConfig_WorkShiftTest {

        @Test
    void testBuilder() {
        WorkingHoursConfig.WorkShift dto = WorkingHoursConfig.WorkShift.builder()
                        .shiftCode("test-shiftCode")
            .shiftName("test-shiftName")
            .startTime("test-startTime")
            .endTime("test-endTime")
            .duration(null)
            .shiftType("test-shiftType")
            .rateMultiplier(null)
            .requiresApproval(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-shiftCode", dto.getShiftCode());
        assertEquals("test-shiftName", dto.getShiftName());
        assertEquals("test-startTime", dto.getStartTime());
        assertEquals("test-endTime", dto.getEndTime());
        assertEquals("test-shiftType", dto.getShiftType());
        assertTrue(dto.getRequiresApproval());
    }

    @Test
    void testSettersAndGetters() {
        WorkingHoursConfig.WorkShift dto = new WorkingHoursConfig.WorkShift();
        dto.setShiftCode("val-shiftCode");
        dto.setShiftName("val-shiftName");
        dto.setStartTime("val-startTime");
        dto.setEndTime("val-endTime");
        dto.setShiftType("val-shiftType");
        dto.setRequiresApproval(true);
        assertEquals("val-shiftCode", dto.getShiftCode());
        assertEquals("val-shiftName", dto.getShiftName());
        assertEquals("val-startTime", dto.getStartTime());
        assertEquals("val-endTime", dto.getEndTime());
        assertEquals("val-shiftType", dto.getShiftType());
        assertTrue(dto.getRequiresApproval());
    }

    @Test
    void testEqualsAndHashCode() {
        WorkingHoursConfig.WorkShift dto1 = WorkingHoursConfig.WorkShift.builder()
                        .shiftCode("test-shiftCode")
            .shiftName("test-shiftName")
            .startTime("test-startTime")
            .endTime("test-endTime")
            .duration(null)
            .shiftType("test-shiftType")
            .rateMultiplier(null)
            .requiresApproval(true)
            .build();
        WorkingHoursConfig.WorkShift dto2 = WorkingHoursConfig.WorkShift.builder()
                        .shiftCode("test-shiftCode")
            .shiftName("test-shiftName")
            .startTime("test-startTime")
            .endTime("test-endTime")
            .duration(null)
            .shiftType("test-shiftType")
            .rateMultiplier(null)
            .requiresApproval(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        WorkingHoursConfig.WorkShift dto = WorkingHoursConfig.WorkShift.builder()
                        .shiftCode("test-shiftCode")
            .shiftName("test-shiftName")
            .startTime("test-startTime")
            .endTime("test-endTime")
            .duration(null)
            .shiftType("test-shiftType")
            .rateMultiplier(null)
            .requiresApproval(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}