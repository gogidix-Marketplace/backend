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
class ConsolidationReport_ReportWarningTest {

        @Test
    void testBuilder() {
        ConsolidationReport.ReportWarning dto = ConsolidationReport.ReportWarning.builder()
                        .warningId("test-warningId")
            .warningCode("test-warningCode")
            .warningMessage("test-warningMessage")
            .affectedArea("test-affectedArea")
            .recommendation("test-recommendation")
            .build();
        assertNotNull(dto);
        assertEquals("test-warningId", dto.getWarningId());
        assertEquals("test-warningCode", dto.getWarningCode());
        assertEquals("test-warningMessage", dto.getWarningMessage());
        assertEquals("test-affectedArea", dto.getAffectedArea());
        assertEquals("test-recommendation", dto.getRecommendation());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationReport.ReportWarning dto = new ConsolidationReport.ReportWarning();
        dto.setWarningId("val-warningId");
        dto.setWarningCode("val-warningCode");
        dto.setWarningMessage("val-warningMessage");
        dto.setAffectedArea("val-affectedArea");
        dto.setRecommendation("val-recommendation");
        assertEquals("val-warningId", dto.getWarningId());
        assertEquals("val-warningCode", dto.getWarningCode());
        assertEquals("val-warningMessage", dto.getWarningMessage());
        assertEquals("val-affectedArea", dto.getAffectedArea());
        assertEquals("val-recommendation", dto.getRecommendation());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationReport.ReportWarning dto1 = ConsolidationReport.ReportWarning.builder()
                        .warningId("test-warningId")
            .warningCode("test-warningCode")
            .warningMessage("test-warningMessage")
            .affectedArea("test-affectedArea")
            .recommendation("test-recommendation")
            .build();
        ConsolidationReport.ReportWarning dto2 = ConsolidationReport.ReportWarning.builder()
                        .warningId("test-warningId")
            .warningCode("test-warningCode")
            .warningMessage("test-warningMessage")
            .affectedArea("test-affectedArea")
            .recommendation("test-recommendation")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationReport.ReportWarning dto = ConsolidationReport.ReportWarning.builder()
                        .warningId("test-warningId")
            .warningCode("test-warningCode")
            .warningMessage("test-warningMessage")
            .affectedArea("test-affectedArea")
            .recommendation("test-recommendation")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}