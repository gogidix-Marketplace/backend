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
class BudgetTrackingSummaryDto_BudgetHealthDtoTest {

        @Test
    void testBuilder() {
        BudgetTrackingSummaryDto.BudgetHealthDto dto = BudgetTrackingSummaryDto.BudgetHealthDto.builder()
                        .budgetCode("test-budgetCode")
            .budgetName("test-budgetName")
            .category("test-category")
            .department("test-department")
            .utilizationPercentage(BigDecimal.TEN)
            .healthStatus("test-healthStatus")
            .requiresAttention(true)
            .alertCount(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-budgetCode", dto.getBudgetCode());
        assertEquals("test-budgetName", dto.getBudgetName());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-department", dto.getDepartment());
        assertEquals(BigDecimal.TEN, dto.getUtilizationPercentage());
        assertEquals("test-healthStatus", dto.getHealthStatus());
        assertTrue(dto.isRequiresAttention());
        assertEquals(42, dto.getAlertCount());
    }

    @Test
    void testSettersAndGetters() {
        BudgetTrackingSummaryDto.BudgetHealthDto dto = new BudgetTrackingSummaryDto.BudgetHealthDto();
        dto.setBudgetCode("val-budgetCode");
        dto.setBudgetName("val-budgetName");
        dto.setCategory("val-category");
        dto.setDepartment("val-department");
        dto.setUtilizationPercentage(BigDecimal.ONE);
        dto.setHealthStatus("val-healthStatus");
        dto.setRequiresAttention(true);
        dto.setAlertCount(99);
        assertEquals("val-budgetCode", dto.getBudgetCode());
        assertEquals("val-budgetName", dto.getBudgetName());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-department", dto.getDepartment());
        assertEquals(BigDecimal.ONE, dto.getUtilizationPercentage());
        assertEquals("val-healthStatus", dto.getHealthStatus());
        assertTrue(dto.isRequiresAttention());
        assertEquals(99, dto.getAlertCount());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTrackingSummaryDto.BudgetHealthDto dto1 = BudgetTrackingSummaryDto.BudgetHealthDto.builder()
                        .budgetCode("test-budgetCode")
            .budgetName("test-budgetName")
            .category("test-category")
            .department("test-department")
            .utilizationPercentage(BigDecimal.TEN)
            .healthStatus("test-healthStatus")
            .requiresAttention(true)
            .alertCount(42)
            .build();
        BudgetTrackingSummaryDto.BudgetHealthDto dto2 = BudgetTrackingSummaryDto.BudgetHealthDto.builder()
                        .budgetCode("test-budgetCode")
            .budgetName("test-budgetName")
            .category("test-category")
            .department("test-department")
            .utilizationPercentage(BigDecimal.TEN)
            .healthStatus("test-healthStatus")
            .requiresAttention(true)
            .alertCount(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BudgetTrackingSummaryDto.BudgetHealthDto dto = BudgetTrackingSummaryDto.BudgetHealthDto.builder()
                        .budgetCode("test-budgetCode")
            .budgetName("test-budgetName")
            .category("test-category")
            .department("test-department")
            .utilizationPercentage(BigDecimal.TEN)
            .healthStatus("test-healthStatus")
            .requiresAttention(true)
            .alertCount(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}