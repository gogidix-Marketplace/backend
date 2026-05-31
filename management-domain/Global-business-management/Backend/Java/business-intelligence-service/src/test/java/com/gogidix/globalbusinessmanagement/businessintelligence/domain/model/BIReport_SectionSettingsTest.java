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
class BIReport_SectionSettingsTest {

        @Test
    void testBuilder() {
        BIReport.SectionSettings dto = BIReport.SectionSettings.builder()
                        .collapsible(true)
            .defaultExpanded(true)
            .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .build();
        assertNotNull(dto);
        assertTrue(dto.getCollapsible());
        assertTrue(dto.getDefaultExpanded());
        assertEquals("test-backgroundColor", dto.getBackgroundColor());
        assertEquals("test-borderColor", dto.getBorderColor());
    }

    @Test
    void testSettersAndGetters() {
        BIReport.SectionSettings dto = new BIReport.SectionSettings();
        dto.setCollapsible(true);
        dto.setDefaultExpanded(true);
        dto.setBackgroundColor("val-backgroundColor");
        dto.setBorderColor("val-borderColor");
        assertTrue(dto.getCollapsible());
        assertTrue(dto.getDefaultExpanded());
        assertEquals("val-backgroundColor", dto.getBackgroundColor());
        assertEquals("val-borderColor", dto.getBorderColor());
    }

    @Test
    void testEqualsAndHashCode() {
        BIReport.SectionSettings dto1 = BIReport.SectionSettings.builder()
                        .collapsible(true)
            .defaultExpanded(true)
            .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .build();
        BIReport.SectionSettings dto2 = BIReport.SectionSettings.builder()
                        .collapsible(true)
            .defaultExpanded(true)
            .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BIReport.SectionSettings dto = BIReport.SectionSettings.builder()
                        .collapsible(true)
            .defaultExpanded(true)
            .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}