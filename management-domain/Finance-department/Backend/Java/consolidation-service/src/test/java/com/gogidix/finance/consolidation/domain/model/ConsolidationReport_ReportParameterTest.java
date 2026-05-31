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
class ConsolidationReport_ReportParameterTest {

        @Test
    void testBuilder() {
        ConsolidationReport.ReportParameter dto = ConsolidationReport.ReportParameter.builder()
                        .name("test-name")
            .value(null)
            .description("test-description")
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationReport.ReportParameter dto = new ConsolidationReport.ReportParameter();
        dto.setName("val-name");
        dto.setDescription("val-description");
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationReport.ReportParameter dto1 = ConsolidationReport.ReportParameter.builder()
                        .name("test-name")
            .value(null)
            .description("test-description")
            .build();
        ConsolidationReport.ReportParameter dto2 = ConsolidationReport.ReportParameter.builder()
                        .name("test-name")
            .value(null)
            .description("test-description")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationReport.ReportParameter dto = ConsolidationReport.ReportParameter.builder()
                        .name("test-name")
            .value(null)
            .description("test-description")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}