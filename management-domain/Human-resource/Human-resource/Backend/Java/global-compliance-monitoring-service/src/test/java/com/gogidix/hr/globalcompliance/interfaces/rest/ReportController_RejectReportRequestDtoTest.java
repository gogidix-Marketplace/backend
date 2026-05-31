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
class ReportController_RejectReportRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ReportController.RejectReportRequestDto dto = new ReportController.RejectReportRequestDto();
        dto.setRejectionReason("val-rejectionReason");
        assertEquals("val-rejectionReason", dto.getRejectionReason());
    }

    @Test
    void testEqualsAndHashCode() {
        ReportController.RejectReportRequestDto dto1 = new ReportController.RejectReportRequestDto();
        ReportController.RejectReportRequestDto dto2 = new ReportController.RejectReportRequestDto();
        dto1.setRejectionReason("test");
        dto2.setRejectionReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setRejectionReason(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReportController.RejectReportRequestDto dto = new ReportController.RejectReportRequestDto();
        dto.setRejectionReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReportController.RejectReportRequestDto dto = new ReportController.RejectReportRequestDto();
        dto.setRejectionReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}