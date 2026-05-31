package com.gogidix.finance.budgettracking.interfaces.rest;

import com.gogidix.finance.budgettracking.interfaces.rest.BudgetMonitorController;
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
class BudgetMonitorController_CreateMonitorRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        BudgetMonitorController.CreateMonitorRequestDto dto = new BudgetMonitorController.CreateMonitorRequestDto();
        dto.setBudgetId("val-budgetId");
        dto.setBudgetCode("val-budgetCode");
        dto.setBudgetName("val-budgetName");
        dto.setBudgetPeriod("val-budgetPeriod");
        dto.setAllocatedAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setCategory("val-category");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setFiscalYear("val-fiscalYear");
        assertEquals("val-budgetId", dto.getBudgetId());
        assertEquals("val-budgetCode", dto.getBudgetCode());
        assertEquals("val-budgetName", dto.getBudgetName());
        assertEquals("val-budgetPeriod", dto.getBudgetPeriod());
        assertEquals(BigDecimal.ONE, dto.getAllocatedAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-fiscalYear", dto.getFiscalYear());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorController.CreateMonitorRequestDto dto1 = new BudgetMonitorController.CreateMonitorRequestDto();
        BudgetMonitorController.CreateMonitorRequestDto dto2 = new BudgetMonitorController.CreateMonitorRequestDto();
        dto1.setBudgetId("test");
        dto1.setBudgetCode("test");
        dto1.setBudgetName("test");
        dto1.setBudgetPeriod("test");
        dto1.setPeriod(null);
        dto1.setAllocatedAmount(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setCategory("test");
        dto1.setDepartment("test");
        dto1.setCostCenter("test");
        dto1.setFiscalYear("test");
        dto1.setAlertRecipients(Collections.emptyList());
        dto2.setBudgetId("test");
        dto2.setBudgetCode("test");
        dto2.setBudgetName("test");
        dto2.setBudgetPeriod("test");
        dto2.setPeriod(null);
        dto2.setAllocatedAmount(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setCategory("test");
        dto2.setDepartment("test");
        dto2.setCostCenter("test");
        dto2.setFiscalYear("test");
        dto2.setAlertRecipients(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setBudgetId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetMonitorController.CreateMonitorRequestDto dto = new BudgetMonitorController.CreateMonitorRequestDto();
        dto.setBudgetId("test");
        dto.setBudgetCode("test");
        dto.setBudgetName("test");
        dto.setBudgetPeriod("test");
        dto.setPeriod(null);
        dto.setAllocatedAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setCategory("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setFiscalYear("test");
        dto.setAlertRecipients(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetMonitorController.CreateMonitorRequestDto dto = new BudgetMonitorController.CreateMonitorRequestDto();
        dto.setBudgetId("test");
        dto.setBudgetCode("test");
        dto.setBudgetName("test");
        dto.setBudgetPeriod("test");
        dto.setPeriod(null);
        dto.setAllocatedAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setCategory("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setFiscalYear("test");
        dto.setAlertRecipients(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}