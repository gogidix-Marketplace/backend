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
class BIReport_SectionContentTest {

        @Test
    void testBuilder() {
        BIReport.SectionContent dto = BIReport.SectionContent.builder()
                        .text("test-text")
            .metrics(Collections.emptyList())
            .charts(Collections.emptyList())
            .tables(Collections.emptyList())
            .insights(Collections.emptyList())
            .rawData(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-text", dto.getText());
    }

    @Test
    void testSettersAndGetters() {
        BIReport.SectionContent dto = new BIReport.SectionContent();
        dto.setText("val-text");
        assertEquals("val-text", dto.getText());
    }

    @Test
    void testEqualsAndHashCode() {
        BIReport.SectionContent dto1 = BIReport.SectionContent.builder()
                        .text("test-text")
            .metrics(Collections.emptyList())
            .charts(Collections.emptyList())
            .tables(Collections.emptyList())
            .insights(Collections.emptyList())
            .rawData(Collections.emptyMap())
            .build();
        BIReport.SectionContent dto2 = BIReport.SectionContent.builder()
                        .text("test-text")
            .metrics(Collections.emptyList())
            .charts(Collections.emptyList())
            .tables(Collections.emptyList())
            .insights(Collections.emptyList())
            .rawData(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BIReport.SectionContent dto = BIReport.SectionContent.builder()
                        .text("test-text")
            .metrics(Collections.emptyList())
            .charts(Collections.emptyList())
            .tables(Collections.emptyList())
            .insights(Collections.emptyList())
            .rawData(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}