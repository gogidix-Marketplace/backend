package com.gogidix.sales.territory.application.service;

import com.gogidix.sales.territory.application.service.TerritoryQueryService;
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
class TerritoryQueryService_TerritorySummaryTest {

        @Test
    void testBuilder() {
        TerritoryQueryService.TerritorySummary dto = TerritoryQueryService.TerritorySummary.builder()
                        .totalTerritories(42L)
            .activeTerritories(42L)
            .inactiveTerritories(42L)
            .pendingRealignment(42L)
            .countByType(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalTerritories());
        assertEquals(42L, dto.getActiveTerritories());
        assertEquals(42L, dto.getInactiveTerritories());
        assertEquals(42L, dto.getPendingRealignment());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryQueryService.TerritorySummary dto1 = TerritoryQueryService.TerritorySummary.builder()
                        .totalTerritories(42L)
            .activeTerritories(42L)
            .inactiveTerritories(42L)
            .pendingRealignment(42L)
            .countByType(Collections.emptyMap())
            .build();
        TerritoryQueryService.TerritorySummary dto2 = TerritoryQueryService.TerritorySummary.builder()
                        .totalTerritories(42L)
            .activeTerritories(42L)
            .inactiveTerritories(42L)
            .pendingRealignment(42L)
            .countByType(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TerritoryQueryService.TerritorySummary dto = TerritoryQueryService.TerritorySummary.builder()
                        .totalTerritories(42L)
            .activeTerritories(42L)
            .inactiveTerritories(42L)
            .pendingRealignment(42L)
            .countByType(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}