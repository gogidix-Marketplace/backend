package com.gogidix.finance.budgettracking.application.dto.response;

import com.gogidix.finance.budgettracking.application.dto.response.BudgetTrackingSummaryDto;
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
class BudgetTrackingSummaryDto_CategorySummaryDtoTest {

        @Test
    void testBuilder() {
        BudgetTrackingSummaryDto.CategorySummaryDto dto = BudgetTrackingSummaryDto.CategorySummaryDto.builder()
                        .category("test-category")
            .allocatedAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .availableAmount(BigDecimal.TEN)
            .utilizationPercentage(BigDecimal.TEN)
            .status("test-status")
            .budgetCount(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-category", dto.getCategory());
        assertEquals(BigDecimal.TEN, dto.getAllocatedAmount());
        assertEquals(BigDecimal.TEN, dto.getCommittedAmount());
        assertEquals(BigDecimal.TEN, dto.getActualAmount());
        assertEquals(BigDecimal.TEN, dto.getAvailableAmount());
        assertEquals(BigDecimal.TEN, dto.getUtilizationPercentage());
        assertEquals("test-status", dto.getStatus());
        assertEquals(42, dto.getBudgetCount());
    }

    @Test
    void testSettersAndGetters() {
        BudgetTrackingSummaryDto.CategorySummaryDto dto = new BudgetTrackingSummaryDto.CategorySummaryDto();
        dto.setCategory("val-category");
        dto.setAllocatedAmount(BigDecimal.ONE);
        dto.setCommittedAmount(BigDecimal.ONE);
        dto.setActualAmount(BigDecimal.ONE);
        dto.setAvailableAmount(BigDecimal.ONE);
        dto.setUtilizationPercentage(BigDecimal.ONE);
        dto.setStatus("val-status");
        dto.setBudgetCount(99);
        assertEquals("val-category", dto.getCategory());
        assertEquals(BigDecimal.ONE, dto.getAllocatedAmount());
        assertEquals(BigDecimal.ONE, dto.getCommittedAmount());
        assertEquals(BigDecimal.ONE, dto.getActualAmount());
        assertEquals(BigDecimal.ONE, dto.getAvailableAmount());
        assertEquals(BigDecimal.ONE, dto.getUtilizationPercentage());
        assertEquals("val-status", dto.getStatus());
        assertEquals(99, dto.getBudgetCount());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTrackingSummaryDto.CategorySummaryDto dto1 = BudgetTrackingSummaryDto.CategorySummaryDto.builder()
                        .category("test-category")
            .allocatedAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .availableAmount(BigDecimal.TEN)
            .utilizationPercentage(BigDecimal.TEN)
            .status("test-status")
            .budgetCount(42)
            .build();
        BudgetTrackingSummaryDto.CategorySummaryDto dto2 = BudgetTrackingSummaryDto.CategorySummaryDto.builder()
                        .category("test-category")
            .allocatedAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .availableAmount(BigDecimal.TEN)
            .utilizationPercentage(BigDecimal.TEN)
            .status("test-status")
            .budgetCount(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BudgetTrackingSummaryDto.CategorySummaryDto dto = BudgetTrackingSummaryDto.CategorySummaryDto.builder()
                        .category("test-category")
            .allocatedAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .availableAmount(BigDecimal.TEN)
            .utilizationPercentage(BigDecimal.TEN)
            .status("test-status")
            .budgetCount(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}