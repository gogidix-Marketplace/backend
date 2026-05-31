package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import com.gogidix.globalbusinessmanagement.businessintelligence.application.dto.CreateBIReportRequest;
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
class CreateBIReportRequestTest {

        @Test
    void testBuilder() {
        CreateBIReportRequest dto = CreateBIReportRequest.builder()
                        .reportName("test-reportName")
            .reportDescription("test-reportDescription")
            .reportType("test-reportType")
            .reportPeriod("test-reportPeriod")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .createdBy("test-createdBy")
            .viewers(Collections.emptyList())
            .templateId("test-templateId")
            .regionCode("test-regionCode")
            .businessUnit("test-businessUnit")
            .parameters(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-reportName", dto.getReportName());
        assertEquals("test-reportDescription", dto.getReportDescription());
        assertEquals("test-reportType", dto.getReportType());
        assertEquals("test-reportPeriod", dto.getReportPeriod());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-templateId", dto.getTemplateId());
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals("test-businessUnit", dto.getBusinessUnit());
    }

    @Test
    void testSettersAndGetters() {
        CreateBIReportRequest dto = new CreateBIReportRequest();
        dto.setReportName("val-reportName");
        dto.setReportDescription("val-reportDescription");
        dto.setReportType("val-reportType");
        dto.setReportPeriod("val-reportPeriod");
        dto.setCreatedBy("val-createdBy");
        dto.setTemplateId("val-templateId");
        dto.setRegionCode("val-regionCode");
        dto.setBusinessUnit("val-businessUnit");
        assertEquals("val-reportName", dto.getReportName());
        assertEquals("val-reportDescription", dto.getReportDescription());
        assertEquals("val-reportType", dto.getReportType());
        assertEquals("val-reportPeriod", dto.getReportPeriod());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-businessUnit", dto.getBusinessUnit());
    }

    @Test
    void testEqualsAndHashCode() {
        CreateBIReportRequest dto1 = CreateBIReportRequest.builder()
                        .reportName("test-reportName")
            .reportDescription("test-reportDescription")
            .reportType("test-reportType")
            .reportPeriod("test-reportPeriod")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .createdBy("test-createdBy")
            .viewers(Collections.emptyList())
            .templateId("test-templateId")
            .regionCode("test-regionCode")
            .businessUnit("test-businessUnit")
            .parameters(Collections.emptyMap())
            .build();
        CreateBIReportRequest dto2 = CreateBIReportRequest.builder()
                        .reportName("test-reportName")
            .reportDescription("test-reportDescription")
            .reportType("test-reportType")
            .reportPeriod("test-reportPeriod")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .createdBy("test-createdBy")
            .viewers(Collections.emptyList())
            .templateId("test-templateId")
            .regionCode("test-regionCode")
            .businessUnit("test-businessUnit")
            .parameters(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CreateBIReportRequest dto = CreateBIReportRequest.builder()
                        .reportName("test-reportName")
            .reportDescription("test-reportDescription")
            .reportType("test-reportType")
            .reportPeriod("test-reportPeriod")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .createdBy("test-createdBy")
            .viewers(Collections.emptyList())
            .templateId("test-templateId")
            .regionCode("test-regionCode")
            .businessUnit("test-businessUnit")
            .parameters(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}