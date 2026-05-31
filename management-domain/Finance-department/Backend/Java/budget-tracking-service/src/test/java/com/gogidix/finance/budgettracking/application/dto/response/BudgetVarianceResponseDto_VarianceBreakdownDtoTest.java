package com.gogidix.finance.budgettracking.application.dto.response;

import com.gogidix.finance.budgettracking.application.dto.response.BudgetVarianceResponseDto;
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
class BudgetVarianceResponseDto_VarianceBreakdownDtoTest {

        @Test
    void testBuilder() {
        BudgetVarianceResponseDto.VarianceBreakdownDto dto = BudgetVarianceResponseDto.VarianceBreakdownDto.builder()
                        .category("test-category")
            .description("test-description")
            .budgetedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .contribution("test-contribution")
            .build();
        assertNotNull(dto);
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getBudgetedAmount());
        assertEquals(BigDecimal.TEN, dto.getActualAmount());
        assertEquals(BigDecimal.TEN, dto.getVarianceAmount());
        assertEquals(BigDecimal.TEN, dto.getVariancePercentage());
        assertEquals("test-contribution", dto.getContribution());
    }

    @Test
    void testSettersAndGetters() {
        BudgetVarianceResponseDto.VarianceBreakdownDto dto = new BudgetVarianceResponseDto.VarianceBreakdownDto();
        dto.setCategory("val-category");
        dto.setDescription("val-description");
        dto.setBudgetedAmount(BigDecimal.ONE);
        dto.setActualAmount(BigDecimal.ONE);
        dto.setVarianceAmount(BigDecimal.ONE);
        dto.setVariancePercentage(BigDecimal.ONE);
        dto.setContribution("val-contribution");
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getBudgetedAmount());
        assertEquals(BigDecimal.ONE, dto.getActualAmount());
        assertEquals(BigDecimal.ONE, dto.getVarianceAmount());
        assertEquals(BigDecimal.ONE, dto.getVariancePercentage());
        assertEquals("val-contribution", dto.getContribution());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetVarianceResponseDto.VarianceBreakdownDto dto1 = BudgetVarianceResponseDto.VarianceBreakdownDto.builder()
                        .category("test-category")
            .description("test-description")
            .budgetedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .contribution("test-contribution")
            .build();
        BudgetVarianceResponseDto.VarianceBreakdownDto dto2 = BudgetVarianceResponseDto.VarianceBreakdownDto.builder()
                        .category("test-category")
            .description("test-description")
            .budgetedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .contribution("test-contribution")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BudgetVarianceResponseDto.VarianceBreakdownDto dto = BudgetVarianceResponseDto.VarianceBreakdownDto.builder()
                        .category("test-category")
            .description("test-description")
            .budgetedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .contribution("test-contribution")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}