package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.domain.model.ConsolidationRule;
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
class ConsolidationRule_IntercompanyEliminationTest {

        @Test
    void testBuilder() {
        ConsolidationRule.IntercompanyElimination dto = ConsolidationRule.IntercompanyElimination.builder()
                        .enabled(true)
            .method(null)
            .eliminateReciprocalAccounts(true)
            .eliminateUnrealizedProfits(true)
            .eliminateDownstreamTransactions(true)
            .eliminateUpstreamTransactions(true)
            .matchingCriteria("test-matchingCriteria")
            .build();
        assertNotNull(dto);
        assertTrue(dto.getEnabled());
        assertTrue(dto.getEliminateReciprocalAccounts());
        assertTrue(dto.getEliminateUnrealizedProfits());
        assertTrue(dto.getEliminateDownstreamTransactions());
        assertTrue(dto.getEliminateUpstreamTransactions());
        assertEquals("test-matchingCriteria", dto.getMatchingCriteria());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationRule.IntercompanyElimination dto = new ConsolidationRule.IntercompanyElimination();
        dto.setEnabled(true);
        dto.setEliminateReciprocalAccounts(true);
        dto.setEliminateUnrealizedProfits(true);
        dto.setEliminateDownstreamTransactions(true);
        dto.setEliminateUpstreamTransactions(true);
        dto.setMatchingCriteria("val-matchingCriteria");
        assertTrue(dto.getEnabled());
        assertTrue(dto.getEliminateReciprocalAccounts());
        assertTrue(dto.getEliminateUnrealizedProfits());
        assertTrue(dto.getEliminateDownstreamTransactions());
        assertTrue(dto.getEliminateUpstreamTransactions());
        assertEquals("val-matchingCriteria", dto.getMatchingCriteria());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationRule.IntercompanyElimination dto1 = ConsolidationRule.IntercompanyElimination.builder()
                        .enabled(true)
            .method(null)
            .eliminateReciprocalAccounts(true)
            .eliminateUnrealizedProfits(true)
            .eliminateDownstreamTransactions(true)
            .eliminateUpstreamTransactions(true)
            .matchingCriteria("test-matchingCriteria")
            .build();
        ConsolidationRule.IntercompanyElimination dto2 = ConsolidationRule.IntercompanyElimination.builder()
                        .enabled(true)
            .method(null)
            .eliminateReciprocalAccounts(true)
            .eliminateUnrealizedProfits(true)
            .eliminateDownstreamTransactions(true)
            .eliminateUpstreamTransactions(true)
            .matchingCriteria("test-matchingCriteria")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationRule.IntercompanyElimination dto = ConsolidationRule.IntercompanyElimination.builder()
                        .enabled(true)
            .method(null)
            .eliminateReciprocalAccounts(true)
            .eliminateUnrealizedProfits(true)
            .eliminateDownstreamTransactions(true)
            .eliminateUpstreamTransactions(true)
            .matchingCriteria("test-matchingCriteria")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}