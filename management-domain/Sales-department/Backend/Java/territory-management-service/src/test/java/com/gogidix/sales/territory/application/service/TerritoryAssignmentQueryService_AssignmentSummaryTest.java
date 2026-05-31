package com.gogidix.sales.territory.application.service;

import com.gogidix.sales.territory.application.service.TerritoryAssignmentQueryService;
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
class TerritoryAssignmentQueryService_AssignmentSummaryTest {

        @Test
    void testBuilder() {
        TerritoryAssignmentQueryService.AssignmentSummary dto = TerritoryAssignmentQueryService.AssignmentSummary.builder()
                        .totalAssignments(42L)
            .activeAssignments(42L)
            .countByType(Collections.emptyMap())
            .countByStatus(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalAssignments());
        assertEquals(42L, dto.getActiveAssignments());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryAssignmentQueryService.AssignmentSummary dto1 = TerritoryAssignmentQueryService.AssignmentSummary.builder()
                        .totalAssignments(42L)
            .activeAssignments(42L)
            .countByType(Collections.emptyMap())
            .countByStatus(Collections.emptyMap())
            .build();
        TerritoryAssignmentQueryService.AssignmentSummary dto2 = TerritoryAssignmentQueryService.AssignmentSummary.builder()
                        .totalAssignments(42L)
            .activeAssignments(42L)
            .countByType(Collections.emptyMap())
            .countByStatus(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TerritoryAssignmentQueryService.AssignmentSummary dto = TerritoryAssignmentQueryService.AssignmentSummary.builder()
                        .totalAssignments(42L)
            .activeAssignments(42L)
            .countByType(Collections.emptyMap())
            .countByStatus(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}