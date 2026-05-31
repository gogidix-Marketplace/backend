package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import com.gogidix.globalbusinessmanagement.businessintelligence.application.dto.UpdateBIReportRequest;
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
class UpdateBIReportRequestTest {

        @Test
    void testBuilder() {
        UpdateBIReportRequest dto = UpdateBIReportRequest.builder()
                        .reportName("test-reportName")
            .reportDescription("test-reportDescription")
            .reportType("test-reportType")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .viewers(Collections.emptyList())
            .regionCode("test-regionCode")
            .businessUnit("test-businessUnit")
            .parameters(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-reportName", dto.getReportName());
        assertEquals("test-reportDescription", dto.getReportDescription());
        assertEquals("test-reportType", dto.getReportType());
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals("test-businessUnit", dto.getBusinessUnit());
    }

    @Test
    void testSettersAndGetters() {
        UpdateBIReportRequest dto = new UpdateBIReportRequest();
        dto.setReportName("val-reportName");
        dto.setReportDescription("val-reportDescription");
        dto.setReportType("val-reportType");
        dto.setRegionCode("val-regionCode");
        dto.setBusinessUnit("val-businessUnit");
        assertEquals("val-reportName", dto.getReportName());
        assertEquals("val-reportDescription", dto.getReportDescription());
        assertEquals("val-reportType", dto.getReportType());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-businessUnit", dto.getBusinessUnit());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateBIReportRequest dto1 = UpdateBIReportRequest.builder()
                        .reportName("test-reportName")
            .reportDescription("test-reportDescription")
            .reportType("test-reportType")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .viewers(Collections.emptyList())
            .regionCode("test-regionCode")
            .businessUnit("test-businessUnit")
            .parameters(Collections.emptyMap())
            .build();
        UpdateBIReportRequest dto2 = UpdateBIReportRequest.builder()
                        .reportName("test-reportName")
            .reportDescription("test-reportDescription")
            .reportType("test-reportType")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .viewers(Collections.emptyList())
            .regionCode("test-regionCode")
            .businessUnit("test-businessUnit")
            .parameters(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdateBIReportRequest dto = UpdateBIReportRequest.builder()
                        .reportName("test-reportName")
            .reportDescription("test-reportDescription")
            .reportType("test-reportType")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .viewers(Collections.emptyList())
            .regionCode("test-regionCode")
            .businessUnit("test-businessUnit")
            .parameters(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}