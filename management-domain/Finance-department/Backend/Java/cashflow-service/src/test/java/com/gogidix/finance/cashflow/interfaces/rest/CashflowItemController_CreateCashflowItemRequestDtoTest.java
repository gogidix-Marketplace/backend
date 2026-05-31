package com.gogidix.finance.cashflow.interfaces.rest;

import com.gogidix.finance.cashflow.domain.model.CashflowItem;
import com.gogidix.finance.cashflow.interfaces.rest.CashflowItemController;
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
class CashflowItemController_CreateCashflowItemRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CashflowItemController.CreateCashflowItemRequestDto dto = new CashflowItemController.CreateCashflowItemRequestDto();
        dto.setReference("val-reference");
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setExpectedDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setCounterparty("val-counterparty");
        dto.setAccount("val-account");
        dto.setCostCenter("val-costCenter");
        dto.setProjectId("val-projectId");
        dto.setRecurring(true);
        dto.setParentRecurringItemId("val-parentRecurringItemId");
        dto.setPaymentMethod("val-paymentMethod");
        dto.setTaxAmount(BigDecimal.ONE);
        dto.setNotes("val-notes");
        dto.setLinkedExpenseId("val-linkedExpenseId");
        dto.setLinkedRevenueId("val-linkedRevenueId");
        assertEquals("val-reference", dto.getReference());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpectedDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-counterparty", dto.getCounterparty());
        assertEquals("val-account", dto.getAccount());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-projectId", dto.getProjectId());
        assertTrue(dto.getRecurring());
        assertEquals("val-parentRecurringItemId", dto.getParentRecurringItemId());
        assertEquals("val-paymentMethod", dto.getPaymentMethod());
        assertEquals(BigDecimal.ONE, dto.getTaxAmount());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-linkedExpenseId", dto.getLinkedExpenseId());
        assertEquals("val-linkedRevenueId", dto.getLinkedRevenueId());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowItemController.CreateCashflowItemRequestDto dto1 = new CashflowItemController.CreateCashflowItemRequestDto();
        CashflowItemController.CreateCashflowItemRequestDto dto2 = new CashflowItemController.CreateCashflowItemRequestDto();
        dto1.setReference("test");
        dto1.setType(CashflowItem.CashflowType.INFLOW);
        dto1.setCategory(CashflowItem.CashflowCategory.OPERATING_REVENUE);
        dto1.setAmount(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setTransactionDate(LocalDate.of(2025,1,1));
        dto1.setExpectedDate(LocalDate.of(2025,1,1));
        dto1.setDescription("test");
        dto1.setCounterparty("test");
        dto1.setAccount("test");
        dto1.setCostCenter("test");
        dto1.setProjectId("test");
        dto1.setRecurring(true);
        dto1.setRecurringFrequency(CashflowItem.RecurringFrequency.DAILY);
        dto1.setParentRecurringItemId("test");
        dto1.setPaymentMethod("test");
        dto1.setTaxAmount(BigDecimal.TEN);
        dto1.setTags(Collections.emptyList());
        dto1.setNotes("test");
        dto1.setLinkedExpenseId("test");
        dto1.setLinkedRevenueId("test");
        dto2.setReference("test");
        dto2.setType(CashflowItem.CashflowType.INFLOW);
        dto2.setCategory(CashflowItem.CashflowCategory.OPERATING_REVENUE);
        dto2.setAmount(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setTransactionDate(LocalDate.of(2025,1,1));
        dto2.setExpectedDate(LocalDate.of(2025,1,1));
        dto2.setDescription("test");
        dto2.setCounterparty("test");
        dto2.setAccount("test");
        dto2.setCostCenter("test");
        dto2.setProjectId("test");
        dto2.setRecurring(true);
        dto2.setRecurringFrequency(CashflowItem.RecurringFrequency.DAILY);
        dto2.setParentRecurringItemId("test");
        dto2.setPaymentMethod("test");
        dto2.setTaxAmount(BigDecimal.TEN);
        dto2.setTags(Collections.emptyList());
        dto2.setNotes("test");
        dto2.setLinkedExpenseId("test");
        dto2.setLinkedRevenueId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setReference(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowItemController.CreateCashflowItemRequestDto dto = new CashflowItemController.CreateCashflowItemRequestDto();
        dto.setReference("test");
        dto.setType(CashflowItem.CashflowType.INFLOW);
        dto.setCategory(CashflowItem.CashflowCategory.OPERATING_REVENUE);
        dto.setAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setExpectedDate(LocalDate.of(2025,1,1));
        dto.setDescription("test");
        dto.setCounterparty("test");
        dto.setAccount("test");
        dto.setCostCenter("test");
        dto.setProjectId("test");
        dto.setRecurring(true);
        dto.setRecurringFrequency(CashflowItem.RecurringFrequency.DAILY);
        dto.setParentRecurringItemId("test");
        dto.setPaymentMethod("test");
        dto.setTaxAmount(BigDecimal.TEN);
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        dto.setLinkedExpenseId("test");
        dto.setLinkedRevenueId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowItemController.CreateCashflowItemRequestDto dto = new CashflowItemController.CreateCashflowItemRequestDto();
        dto.setReference("test");
        dto.setType(CashflowItem.CashflowType.INFLOW);
        dto.setCategory(CashflowItem.CashflowCategory.OPERATING_REVENUE);
        dto.setAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setExpectedDate(LocalDate.of(2025,1,1));
        dto.setDescription("test");
        dto.setCounterparty("test");
        dto.setAccount("test");
        dto.setCostCenter("test");
        dto.setProjectId("test");
        dto.setRecurring(true);
        dto.setRecurringFrequency(CashflowItem.RecurringFrequency.DAILY);
        dto.setParentRecurringItemId("test");
        dto.setPaymentMethod("test");
        dto.setTaxAmount(BigDecimal.TEN);
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        dto.setLinkedExpenseId("test");
        dto.setLinkedRevenueId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}