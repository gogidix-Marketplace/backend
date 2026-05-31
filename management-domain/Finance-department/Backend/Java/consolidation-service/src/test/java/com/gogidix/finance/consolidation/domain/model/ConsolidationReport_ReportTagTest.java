package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.domain.model.ConsolidationReport;
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
class ConsolidationReport_ReportTagTest {

        @Test
    void testBuilder() {
        ConsolidationReport.ReportTag dto = ConsolidationReport.ReportTag.builder()
                        .key("test-key")
            .value("test-value")
            .build();
        assertNotNull(dto);
        assertEquals("test-key", dto.getKey());
        assertEquals("test-value", dto.getValue());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationReport.ReportTag dto = new ConsolidationReport.ReportTag();
        dto.setKey("val-key");
        dto.setValue("val-value");
        assertEquals("val-key", dto.getKey());
        assertEquals("val-value", dto.getValue());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationReport.ReportTag dto1 = ConsolidationReport.ReportTag.builder()
                        .key("test-key")
            .value("test-value")
            .build();
        ConsolidationReport.ReportTag dto2 = ConsolidationReport.ReportTag.builder()
                        .key("test-key")
            .value("test-value")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationReport.ReportTag dto = ConsolidationReport.ReportTag.builder()
                        .key("test-key")
            .value("test-value")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}