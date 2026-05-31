package com.gogidix.digitalmarketing.budgetmanagement.application.dto;

import com.gogidix.digitalmarketing.budgetmanagement.application.dto.BudgetRequestDto;
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
class BudgetRequestDtoTest {

        @Test
    void testBuilder() {
        BudgetRequestDto dto = BudgetRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .fiscalYear("test-fiscalYear")
            .totalAmount("test-totalAmount")
            .allocatedAmount("test-allocatedAmount")
            .committedAmount("test-committedAmount")
            .spentAmount("test-spentAmount")
            .status("test-status")
            .currency("test-currency")
            .budgetCategory("test-budgetCategory")
            .country("test-country")
            .department("test-department")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-fiscalYear", dto.getFiscalYear());
        assertEquals("test-totalAmount", dto.getTotalAmount());
        assertEquals("test-allocatedAmount", dto.getAllocatedAmount());
        assertEquals("test-committedAmount", dto.getCommittedAmount());
        assertEquals("test-spentAmount", dto.getSpentAmount());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-budgetCategory", dto.getBudgetCategory());
        assertEquals("test-country", dto.getCountry());
        assertEquals("test-department", dto.getDepartment());
    }

    @Test
    void testSettersAndGetters() {
        BudgetRequestDto dto = new BudgetRequestDto();
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setFiscalYear("val-fiscalYear");
        dto.setTotalAmount("val-totalAmount");
        dto.setAllocatedAmount("val-allocatedAmount");
        dto.setCommittedAmount("val-committedAmount");
        dto.setSpentAmount("val-spentAmount");
        dto.setStatus("val-status");
        dto.setCurrency("val-currency");
        dto.setBudgetCategory("val-budgetCategory");
        dto.setCountry("val-country");
        dto.setDepartment("val-department");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-fiscalYear", dto.getFiscalYear());
        assertEquals("val-totalAmount", dto.getTotalAmount());
        assertEquals("val-allocatedAmount", dto.getAllocatedAmount());
        assertEquals("val-committedAmount", dto.getCommittedAmount());
        assertEquals("val-spentAmount", dto.getSpentAmount());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-budgetCategory", dto.getBudgetCategory());
        assertEquals("val-country", dto.getCountry());
        assertEquals("val-department", dto.getDepartment());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetRequestDto dto1 = BudgetRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .fiscalYear("test-fiscalYear")
            .totalAmount("test-totalAmount")
            .allocatedAmount("test-allocatedAmount")
            .committedAmount("test-committedAmount")
            .spentAmount("test-spentAmount")
            .status("test-status")
            .currency("test-currency")
            .budgetCategory("test-budgetCategory")
            .country("test-country")
            .department("test-department")
            .build();
        BudgetRequestDto dto2 = BudgetRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .fiscalYear("test-fiscalYear")
            .totalAmount("test-totalAmount")
            .allocatedAmount("test-allocatedAmount")
            .committedAmount("test-committedAmount")
            .spentAmount("test-spentAmount")
            .status("test-status")
            .currency("test-currency")
            .budgetCategory("test-budgetCategory")
            .country("test-country")
            .department("test-department")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BudgetRequestDto dto = BudgetRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .fiscalYear("test-fiscalYear")
            .totalAmount("test-totalAmount")
            .allocatedAmount("test-allocatedAmount")
            .committedAmount("test-committedAmount")
            .spentAmount("test-spentAmount")
            .status("test-status")
            .currency("test-currency")
            .budgetCategory("test-budgetCategory")
            .country("test-country")
            .department("test-department")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}