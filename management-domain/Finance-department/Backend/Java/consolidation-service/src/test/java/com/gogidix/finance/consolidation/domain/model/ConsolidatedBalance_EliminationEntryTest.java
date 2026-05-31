package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.domain.model.ConsolidatedBalance;
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
class ConsolidatedBalance_EliminationEntryTest {

        @Test
    void testBuilder() {
        ConsolidatedBalance.EliminationEntry dto = ConsolidatedBalance.EliminationEntry.builder()
                        .eliminationId("test-eliminationId")
            .eliminationRuleId("test-eliminationRuleId")
            .counterpartEntityId("test-counterpartEntityId")
            .counterpartAccountCode("test-counterpartAccountCode")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .eliminationDate(LocalDate.of(2025,1,15))
            .build();
        assertNotNull(dto);
        assertEquals("test-eliminationId", dto.getEliminationId());
        assertEquals("test-eliminationRuleId", dto.getEliminationRuleId());
        assertEquals("test-counterpartEntityId", dto.getCounterpartEntityId());
        assertEquals("test-counterpartAccountCode", dto.getCounterpartAccountCode());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-description", dto.getDescription());
        assertEquals(LocalDate.of(2025,1,15), dto.getEliminationDate());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidatedBalance.EliminationEntry dto = new ConsolidatedBalance.EliminationEntry();
        dto.setEliminationId("val-eliminationId");
        dto.setEliminationRuleId("val-eliminationRuleId");
        dto.setCounterpartEntityId("val-counterpartEntityId");
        dto.setCounterpartAccountCode("val-counterpartAccountCode");
        dto.setAmount(BigDecimal.ONE);
        dto.setDescription("val-description");
        dto.setEliminationDate(LocalDate.of(2025,6,1));
        assertEquals("val-eliminationId", dto.getEliminationId());
        assertEquals("val-eliminationRuleId", dto.getEliminationRuleId());
        assertEquals("val-counterpartEntityId", dto.getCounterpartEntityId());
        assertEquals("val-counterpartAccountCode", dto.getCounterpartAccountCode());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-description", dto.getDescription());
        assertEquals(LocalDate.of(2025,6,1), dto.getEliminationDate());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidatedBalance.EliminationEntry dto1 = ConsolidatedBalance.EliminationEntry.builder()
                        .eliminationId("test-eliminationId")
            .eliminationRuleId("test-eliminationRuleId")
            .counterpartEntityId("test-counterpartEntityId")
            .counterpartAccountCode("test-counterpartAccountCode")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .eliminationDate(LocalDate.of(2025,1,15))
            .build();
        ConsolidatedBalance.EliminationEntry dto2 = ConsolidatedBalance.EliminationEntry.builder()
                        .eliminationId("test-eliminationId")
            .eliminationRuleId("test-eliminationRuleId")
            .counterpartEntityId("test-counterpartEntityId")
            .counterpartAccountCode("test-counterpartAccountCode")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .eliminationDate(LocalDate.of(2025,1,15))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidatedBalance.EliminationEntry dto = ConsolidatedBalance.EliminationEntry.builder()
                        .eliminationId("test-eliminationId")
            .eliminationRuleId("test-eliminationRuleId")
            .counterpartEntityId("test-counterpartEntityId")
            .counterpartAccountCode("test-counterpartAccountCode")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .eliminationDate(LocalDate.of(2025,1,15))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}