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
class BIReport_AxisOptionsTest {

        @Test
    void testBuilder() {
        BIReport.AxisOptions dto = BIReport.AxisOptions.builder()
                        .stacked(true)
            .display(true)
            .position("test-position")
            .build();
        assertNotNull(dto);
        assertTrue(dto.getStacked());
        assertTrue(dto.getDisplay());
        assertEquals("test-position", dto.getPosition());
    }

    @Test
    void testSettersAndGetters() {
        BIReport.AxisOptions dto = new BIReport.AxisOptions();
        dto.setStacked(true);
        dto.setDisplay(true);
        dto.setPosition("val-position");
        assertTrue(dto.getStacked());
        assertTrue(dto.getDisplay());
        assertEquals("val-position", dto.getPosition());
    }

    @Test
    void testEqualsAndHashCode() {
        BIReport.AxisOptions dto1 = BIReport.AxisOptions.builder()
                        .stacked(true)
            .display(true)
            .position("test-position")
            .build();
        BIReport.AxisOptions dto2 = BIReport.AxisOptions.builder()
                        .stacked(true)
            .display(true)
            .position("test-position")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BIReport.AxisOptions dto = BIReport.AxisOptions.builder()
                        .stacked(true)
            .display(true)
            .position("test-position")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}