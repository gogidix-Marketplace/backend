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
class BIReport_DataSetTest {

        @Test
    void testBuilder() {
        BIReport.DataSet dto = BIReport.DataSet.builder()
                        .label("test-label")
            .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .data(Collections.emptyList())
            .backgroundColors(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-label", dto.getLabel());
        assertEquals("test-backgroundColor", dto.getBackgroundColor());
        assertEquals("test-borderColor", dto.getBorderColor());
    }

    @Test
    void testSettersAndGetters() {
        BIReport.DataSet dto = new BIReport.DataSet();
        dto.setLabel("val-label");
        dto.setBackgroundColor("val-backgroundColor");
        dto.setBorderColor("val-borderColor");
        assertEquals("val-label", dto.getLabel());
        assertEquals("val-backgroundColor", dto.getBackgroundColor());
        assertEquals("val-borderColor", dto.getBorderColor());
    }

    @Test
    void testEqualsAndHashCode() {
        BIReport.DataSet dto1 = BIReport.DataSet.builder()
                        .label("test-label")
            .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .data(Collections.emptyList())
            .backgroundColors(Collections.emptyList())
            .build();
        BIReport.DataSet dto2 = BIReport.DataSet.builder()
                        .label("test-label")
            .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .data(Collections.emptyList())
            .backgroundColors(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BIReport.DataSet dto = BIReport.DataSet.builder()
                        .label("test-label")
            .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .data(Collections.emptyList())
            .backgroundColors(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}