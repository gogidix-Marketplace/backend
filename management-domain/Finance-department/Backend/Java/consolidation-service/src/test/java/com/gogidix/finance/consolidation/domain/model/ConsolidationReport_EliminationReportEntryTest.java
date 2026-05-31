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
class ConsolidationReport_EliminationReportEntryTest {

        @Test
    void testBuilder() {
        ConsolidationReport.EliminationReportEntry dto = ConsolidationReport.EliminationReportEntry.builder()
                        .eliminationId("test-eliminationId")
            .ruleId("test-ruleId")
            .entity1Id("test-entity1Id")
            .entity1Name("test-entity1Name")
            .entity2Id("test-entity2Id")
            .entity2Name("test-entity2Name")
            .accountCode("test-accountCode")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .eliminationMethod("test-eliminationMethod")
            .build();
        assertNotNull(dto);
        assertEquals("test-eliminationId", dto.getEliminationId());
        assertEquals("test-ruleId", dto.getRuleId());
        assertEquals("test-entity1Id", dto.getEntity1Id());
        assertEquals("test-entity1Name", dto.getEntity1Name());
        assertEquals("test-entity2Id", dto.getEntity2Id());
        assertEquals("test-entity2Name", dto.getEntity2Name());
        assertEquals("test-accountCode", dto.getAccountCode());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-eliminationMethod", dto.getEliminationMethod());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationReport.EliminationReportEntry dto = new ConsolidationReport.EliminationReportEntry();
        dto.setEliminationId("val-eliminationId");
        dto.setRuleId("val-ruleId");
        dto.setEntity1Id("val-entity1Id");
        dto.setEntity1Name("val-entity1Name");
        dto.setEntity2Id("val-entity2Id");
        dto.setEntity2Name("val-entity2Name");
        dto.setAccountCode("val-accountCode");
        dto.setAmount(BigDecimal.ONE);
        dto.setDescription("val-description");
        dto.setEliminationMethod("val-eliminationMethod");
        assertEquals("val-eliminationId", dto.getEliminationId());
        assertEquals("val-ruleId", dto.getRuleId());
        assertEquals("val-entity1Id", dto.getEntity1Id());
        assertEquals("val-entity1Name", dto.getEntity1Name());
        assertEquals("val-entity2Id", dto.getEntity2Id());
        assertEquals("val-entity2Name", dto.getEntity2Name());
        assertEquals("val-accountCode", dto.getAccountCode());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-eliminationMethod", dto.getEliminationMethod());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationReport.EliminationReportEntry dto1 = ConsolidationReport.EliminationReportEntry.builder()
                        .eliminationId("test-eliminationId")
            .ruleId("test-ruleId")
            .entity1Id("test-entity1Id")
            .entity1Name("test-entity1Name")
            .entity2Id("test-entity2Id")
            .entity2Name("test-entity2Name")
            .accountCode("test-accountCode")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .eliminationMethod("test-eliminationMethod")
            .build();
        ConsolidationReport.EliminationReportEntry dto2 = ConsolidationReport.EliminationReportEntry.builder()
                        .eliminationId("test-eliminationId")
            .ruleId("test-ruleId")
            .entity1Id("test-entity1Id")
            .entity1Name("test-entity1Name")
            .entity2Id("test-entity2Id")
            .entity2Name("test-entity2Name")
            .accountCode("test-accountCode")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .eliminationMethod("test-eliminationMethod")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationReport.EliminationReportEntry dto = ConsolidationReport.EliminationReportEntry.builder()
                        .eliminationId("test-eliminationId")
            .ruleId("test-ruleId")
            .entity1Id("test-entity1Id")
            .entity1Name("test-entity1Name")
            .entity2Id("test-entity2Id")
            .entity2Name("test-entity2Name")
            .accountCode("test-accountCode")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .eliminationMethod("test-eliminationMethod")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}