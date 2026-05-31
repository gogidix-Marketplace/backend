package com.gogidix.hr.globalcompliance.interfaces.rest;

import com.gogidix.hr.globalcompliance.interfaces.rest.ReportController;
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
class ReportController_MetricsDtoTest {

        @Test
    void testSettersAndGetters() {
        ReportController.MetricsDto dto = new ReportController.MetricsDto();
        dto.setTotalRequirements(99);
        dto.setPassedChecks(99);
        dto.setFailedChecks(99);
        dto.setPendingChecks(99);
        assertEquals(99, dto.getTotalRequirements());
        assertEquals(99, dto.getPassedChecks());
        assertEquals(99, dto.getFailedChecks());
        assertEquals(99, dto.getPendingChecks());
    }

    @Test
    void testEqualsAndHashCode() {
        ReportController.MetricsDto dto1 = new ReportController.MetricsDto();
        ReportController.MetricsDto dto2 = new ReportController.MetricsDto();
        dto1.setTotalRequirements(42);
        dto1.setPassedChecks(42);
        dto1.setFailedChecks(42);
        dto1.setPendingChecks(42);
        dto2.setTotalRequirements(42);
        dto2.setPassedChecks(42);
        dto2.setFailedChecks(42);
        dto2.setPendingChecks(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTotalRequirements(999);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReportController.MetricsDto dto = new ReportController.MetricsDto();
        dto.setTotalRequirements(42);
        dto.setPassedChecks(42);
        dto.setFailedChecks(42);
        dto.setPendingChecks(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReportController.MetricsDto dto = new ReportController.MetricsDto();
        dto.setTotalRequirements(42);
        dto.setPassedChecks(42);
        dto.setFailedChecks(42);
        dto.setPendingChecks(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}