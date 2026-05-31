package com.gogidix.sales.territory.application.dto.response;

import com.gogidix.sales.territory.application.dto.response.TerritoryAssignmentResponseDto;
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
class TerritoryAssignmentResponseDto_AssignmentPerformanceDtoTest {

        @Test
    void testBuilder() {
        TerritoryAssignmentResponseDto.AssignmentPerformanceDto dto = TerritoryAssignmentResponseDto.AssignmentPerformanceDto.builder()
                        .salesGenerated(BigDecimal.TEN)
            .accountsManaged(42)
            .dealsClosed(42)
            .quotaAttainment(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getSalesGenerated());
        assertEquals(42, dto.getAccountsManaged());
        assertEquals(42, dto.getDealsClosed());
        assertEquals(BigDecimal.TEN, dto.getQuotaAttainment());
    }

    @Test
    void testBuilderWithValues() {
        TerritoryAssignmentResponseDto.AssignmentPerformanceDto dto = TerritoryAssignmentResponseDto.AssignmentPerformanceDto.builder()
            .salesGenerated(BigDecimal.ONE)
            .accountsManaged(99)
            .dealsClosed(99)
            .quotaAttainment(BigDecimal.ONE)
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryAssignmentResponseDto.AssignmentPerformanceDto dto1 = TerritoryAssignmentResponseDto.AssignmentPerformanceDto.builder()
                        .salesGenerated(BigDecimal.TEN)
            .accountsManaged(42)
            .dealsClosed(42)
            .quotaAttainment(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        TerritoryAssignmentResponseDto.AssignmentPerformanceDto dto2 = TerritoryAssignmentResponseDto.AssignmentPerformanceDto.builder()
                        .salesGenerated(BigDecimal.TEN)
            .accountsManaged(42)
            .dealsClosed(42)
            .quotaAttainment(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TerritoryAssignmentResponseDto.AssignmentPerformanceDto dto = TerritoryAssignmentResponseDto.AssignmentPerformanceDto.builder()
                        .salesGenerated(BigDecimal.TEN)
            .accountsManaged(42)
            .dealsClosed(42)
            .quotaAttainment(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}