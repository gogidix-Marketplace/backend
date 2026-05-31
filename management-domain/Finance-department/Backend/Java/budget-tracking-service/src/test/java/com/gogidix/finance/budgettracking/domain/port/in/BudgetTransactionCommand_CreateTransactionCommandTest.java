package com.gogidix.finance.budgettracking.domain.port.in;

import com.gogidix.finance.budgettracking.domain.model.BudgetTransaction;
import com.gogidix.finance.budgettracking.domain.port.in.BudgetTransactionCommand;
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
class BudgetTransactionCommand_CreateTransactionCommandTest {

        @Test
    void testSettersAndGetters() {
        BudgetTransactionCommand.CreateTransactionCommand dto = new BudgetTransactionCommand.CreateTransactionCommand();
        dto.setTenantId("val-tenantId");
        dto.setBudgetId("val-budgetId");
        dto.setBudgetCode("val-budgetCode");
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setDescription("val-description");
        dto.setReferenceType("val-referenceType");
        dto.setReferenceId("val-referenceId");
        dto.setCategory("val-category");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setProjectId("val-projectId");
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        dto.setCorrelationId("val-correlationId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-budgetId", dto.getBudgetId());
        assertEquals("val-budgetCode", dto.getBudgetCode());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-referenceType", dto.getReferenceType());
        assertEquals("val-referenceId", dto.getReferenceId());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-projectId", dto.getProjectId());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-correlationId", dto.getCorrelationId());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTransactionCommand.CreateTransactionCommand dto1 = new BudgetTransactionCommand.CreateTransactionCommand();
        BudgetTransactionCommand.CreateTransactionCommand dto2 = new BudgetTransactionCommand.CreateTransactionCommand();
        dto1.setTenantId("test");
        dto1.setBudgetId("test");
        dto1.setBudgetCode("test");
        dto1.setTransactionType(BudgetTransaction.TransactionType.ALLOCATION);
        dto1.setAmount(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setDescription("test");
        dto1.setReferenceType("test");
        dto1.setReferenceId("test");
        dto1.setCategory("test");
        dto1.setDepartment("test");
        dto1.setCostCenter("test");
        dto1.setProjectId("test");
        dto1.setTransactionDate(LocalDate.of(2025,1,1));
        dto1.setTags(Collections.emptyList());
        dto1.setNotes("test");
        dto1.setCorrelationId("test");
        dto2.setTenantId("test");
        dto2.setBudgetId("test");
        dto2.setBudgetCode("test");
        dto2.setTransactionType(BudgetTransaction.TransactionType.ALLOCATION);
        dto2.setAmount(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setDescription("test");
        dto2.setReferenceType("test");
        dto2.setReferenceId("test");
        dto2.setCategory("test");
        dto2.setDepartment("test");
        dto2.setCostCenter("test");
        dto2.setProjectId("test");
        dto2.setTransactionDate(LocalDate.of(2025,1,1));
        dto2.setTags(Collections.emptyList());
        dto2.setNotes("test");
        dto2.setCorrelationId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTransactionCommand.CreateTransactionCommand dto = new BudgetTransactionCommand.CreateTransactionCommand();
        dto.setTenantId("test");
        dto.setBudgetId("test");
        dto.setBudgetCode("test");
        dto.setTransactionType(BudgetTransaction.TransactionType.ALLOCATION);
        dto.setAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setDescription("test");
        dto.setReferenceType("test");
        dto.setReferenceId("test");
        dto.setCategory("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setProjectId("test");
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        dto.setCorrelationId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTransactionCommand.CreateTransactionCommand dto = new BudgetTransactionCommand.CreateTransactionCommand();
        dto.setTenantId("test");
        dto.setBudgetId("test");
        dto.setBudgetCode("test");
        dto.setTransactionType(BudgetTransaction.TransactionType.ALLOCATION);
        dto.setAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setDescription("test");
        dto.setReferenceType("test");
        dto.setReferenceId("test");
        dto.setCategory("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setProjectId("test");
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        dto.setCorrelationId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}