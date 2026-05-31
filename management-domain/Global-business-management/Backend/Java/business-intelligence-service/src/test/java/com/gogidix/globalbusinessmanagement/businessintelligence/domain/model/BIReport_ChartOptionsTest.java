package com.gogidix.globalbusinessmanagement.businessintelligence.domain.model;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.BIReport;
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
class BIReport_ChartOptionsTest {

        @Test
    void testBuilder() {
        BIReport.ChartOptions dto = BIReport.ChartOptions.builder()
                        .responsive(true)
            .maintainAspectRatio(true)
            .legendPosition("test-legendPosition")
            .xAxes(null)
            .yAxes(null)
            .build();
        assertNotNull(dto);
        assertTrue(dto.getResponsive());
        assertTrue(dto.getMaintainAspectRatio());
        assertEquals("test-legendPosition", dto.getLegendPosition());
    }

    @Test
    void testSettersAndGetters() {
        BIReport.ChartOptions dto = new BIReport.ChartOptions();
        dto.setResponsive(true);
        dto.setMaintainAspectRatio(true);
        dto.setLegendPosition("val-legendPosition");
        assertTrue(dto.getResponsive());
        assertTrue(dto.getMaintainAspectRatio());
        assertEquals("val-legendPosition", dto.getLegendPosition());
    }

    @Test
    void testEqualsAndHashCode() {
        BIReport.ChartOptions dto1 = BIReport.ChartOptions.builder()
                        .responsive(true)
            .maintainAspectRatio(true)
            .legendPosition("test-legendPosition")
            .xAxes(null)
            .yAxes(null)
            .build();
        BIReport.ChartOptions dto2 = BIReport.ChartOptions.builder()
                        .responsive(true)
            .maintainAspectRatio(true)
            .legendPosition("test-legendPosition")
            .xAxes(null)
            .yAxes(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BIReport.ChartOptions dto = BIReport.ChartOptions.builder()
                        .responsive(true)
            .maintainAspectRatio(true)
            .legendPosition("test-legendPosition")
            .xAxes(null)
            .yAxes(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}