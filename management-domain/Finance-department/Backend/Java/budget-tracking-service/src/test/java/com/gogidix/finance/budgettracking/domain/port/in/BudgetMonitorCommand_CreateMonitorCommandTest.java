package com.gogidix.finance.budgettracking.domain.port.in;

import com.gogidix.finance.budgettracking.domain.port.in.BudgetMonitorCommand;
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
class BudgetMonitorCommand_CreateMonitorCommandTest {

        @Test
    void testSettersAndGetters() {
        BudgetMonitorCommand.CreateMonitorCommand dto = new BudgetMonitorCommand.CreateMonitorCommand();
        dto.setTenantId("val-tenantId");
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
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-tenantId", dto.getTenantId());
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
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorCommand.CreateMonitorCommand dto1 = new BudgetMonitorCommand.CreateMonitorCommand();
        BudgetMonitorCommand.CreateMonitorCommand dto2 = new BudgetMonitorCommand.CreateMonitorCommand();
        dto1.setTenantId("test");
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
        dto1.setCreatedBy("test");
        dto1.setAlertRecipients(Collections.emptyList());
        dto1.setThresholds(Collections.emptyList());
        dto2.setTenantId("test");
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
        dto2.setCreatedBy("test");
        dto2.setAlertRecipients(Collections.emptyList());
        dto2.setThresholds(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetMonitorCommand.CreateMonitorCommand dto = new BudgetMonitorCommand.CreateMonitorCommand();
        dto.setTenantId("test");
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
        dto.setCreatedBy("test");
        dto.setAlertRecipients(Collections.emptyList());
        dto.setThresholds(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetMonitorCommand.CreateMonitorCommand dto = new BudgetMonitorCommand.CreateMonitorCommand();
        dto.setTenantId("test");
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
        dto.setCreatedBy("test");
        dto.setAlertRecipients(Collections.emptyList());
        dto.setThresholds(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}