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
class ReportController_ApproveReportRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ReportController.ApproveReportRequestDto dto = new ReportController.ApproveReportRequestDto();
        dto.setApprovedByName("val-approvedByName");
        assertEquals("val-approvedByName", dto.getApprovedByName());
    }

    @Test
    void testEqualsAndHashCode() {
        ReportController.ApproveReportRequestDto dto1 = new ReportController.ApproveReportRequestDto();
        ReportController.ApproveReportRequestDto dto2 = new ReportController.ApproveReportRequestDto();
        dto1.setApprovedByName("test");
        dto2.setApprovedByName("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setApprovedByName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReportController.ApproveReportRequestDto dto = new ReportController.ApproveReportRequestDto();
        dto.setApprovedByName("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReportController.ApproveReportRequestDto dto = new ReportController.ApproveReportRequestDto();
        dto.setApprovedByName("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}