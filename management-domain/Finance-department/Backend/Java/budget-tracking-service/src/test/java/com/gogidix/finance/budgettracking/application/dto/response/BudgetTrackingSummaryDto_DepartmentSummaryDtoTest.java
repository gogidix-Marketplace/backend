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
class BudgetTrackingSummaryDto_DepartmentSummaryDtoTest {

        @Test
    void testBuilder() {
        BudgetTrackingSummaryDto.DepartmentSummaryDto dto = BudgetTrackingSummaryDto.DepartmentSummaryDto.builder()
                        .department("test-department")
            .costCenter("test-costCenter")
            .allocatedAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .availableAmount(BigDecimal.TEN)
            .utilizationPercentage(BigDecimal.TEN)
            .status("test-status")
            .budgetCount(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-costCenter", dto.getCostCenter());
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
        BudgetTrackingSummaryDto.DepartmentSummaryDto dto = new BudgetTrackingSummaryDto.DepartmentSummaryDto();
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setAllocatedAmount(BigDecimal.ONE);
        dto.setCommittedAmount(BigDecimal.ONE);
        dto.setActualAmount(BigDecimal.ONE);
        dto.setAvailableAmount(BigDecimal.ONE);
        dto.setUtilizationPercentage(BigDecimal.ONE);
        dto.setStatus("val-status");
        dto.setBudgetCount(99);
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
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
        BudgetTrackingSummaryDto.DepartmentSummaryDto dto1 = BudgetTrackingSummaryDto.DepartmentSummaryDto.builder()
                        .department("test-department")
            .costCenter("test-costCenter")
            .allocatedAmount(BigDecimal.TEN)
            .committedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .availableAmount(BigDecimal.TEN)
            .utilizationPercentage(BigDecimal.TEN)
            .status("test-status")
            .budgetCount(42)
            .build();
        BudgetTrackingSummaryDto.DepartmentSummaryDto dto2 = BudgetTrackingSummaryDto.DepartmentSummaryDto.builder()
                        .department("test-department")
            .costCenter("test-costCenter")
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
        BudgetTrackingSummaryDto.DepartmentSummaryDto dto = BudgetTrackingSummaryDto.DepartmentSummaryDto.builder()
                        .department("test-department")
            .costCenter("test-costCenter")
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