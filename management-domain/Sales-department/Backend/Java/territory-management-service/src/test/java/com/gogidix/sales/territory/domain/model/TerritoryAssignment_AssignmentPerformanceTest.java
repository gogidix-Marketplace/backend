package com.gogidix.sales.territory.domain.model;

import com.gogidix.sales.territory.domain.model.TerritoryAssignment;
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
class TerritoryAssignment_AssignmentPerformanceTest {

        @Test
    void testBuilder() {
        TerritoryAssignment.AssignmentPerformance dto = TerritoryAssignment.AssignmentPerformance.builder()
                        .salesGenerated(null)
            .accountsManaged(42)
            .dealsClosed(42)
            .quotaAttainment(null)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getAccountsManaged());
        assertEquals(42, dto.getDealsClosed());
    }

    @Test
    void testSettersAndGetters() {
        TerritoryAssignment.AssignmentPerformance dto = new TerritoryAssignment.AssignmentPerformance();
        dto.setAccountsManaged(99);
        dto.setDealsClosed(99);
        assertEquals(99, dto.getAccountsManaged());
        assertEquals(99, dto.getDealsClosed());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryAssignment.AssignmentPerformance dto1 = TerritoryAssignment.AssignmentPerformance.builder()
                        .salesGenerated(null)
            .accountsManaged(42)
            .dealsClosed(42)
            .quotaAttainment(null)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        TerritoryAssignment.AssignmentPerformance dto2 = TerritoryAssignment.AssignmentPerformance.builder()
                        .salesGenerated(null)
            .accountsManaged(42)
            .dealsClosed(42)
            .quotaAttainment(null)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TerritoryAssignment.AssignmentPerformance dto = TerritoryAssignment.AssignmentPerformance.builder()
                        .salesGenerated(null)
            .accountsManaged(42)
            .dealsClosed(42)
            .quotaAttainment(null)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}