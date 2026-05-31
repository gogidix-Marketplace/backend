package com.gogidix.finance.cashflow.application.dto.response;

import com.gogidix.finance.cashflow.application.dto.response.CashflowItemResponseDto;
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
class CashflowItemResponseDtoTest {

        @Test
    void testBuilder() {
        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                        .id("test-id")
            .cashflowItemId("test-cashflowItemId")
            .tenantId("test-tenantId")
            .recordedBy("test-recordedBy")
            .reference("test-reference")
            .type(CashflowItemResponseDto.CashflowTypeDto.INFLOW)
            .category(CashflowItemResponseDto.CashflowCategoryDto.OPERATING_REVENUE)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .transactionDate(LocalDate.of(2025,1,15))
            .expectedDate(LocalDate.of(2025,1,15))
            .settledDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .counterparty("test-counterparty")
            .account("test-account")
            .costCenter("test-costCenter")
            .projectId("test-projectId")
            .status(CashflowItemResponseDto.ItemStatusDto.PENDING)
            .recurring(true)
            .recurringFrequency(CashflowItemResponseDto.RecurringFrequencyDto.DAILY)
            .parentRecurringItemId("test-parentRecurringItemId")
            .expectedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .settledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .paymentMethod("test-paymentMethod")
            .bankReference("test-bankReference")
            .invoiceReference("test-invoiceReference")
            .taxAmount(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .linkedExpenseId("test-linkedExpenseId")
            .linkedRevenueId("test-linkedRevenueId")
            .allocationPercentage(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-cashflowItemId", dto.getCashflowItemId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-recordedBy", dto.getRecordedBy());
        assertEquals("test-reference", dto.getReference());
        assertEquals(CashflowItemResponseDto.CashflowTypeDto.INFLOW, dto.getType());
        assertEquals(CashflowItemResponseDto.CashflowCategoryDto.OPERATING_REVENUE, dto.getCategory());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,1,15), dto.getTransactionDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getExpectedDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getSettledDate());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-counterparty", dto.getCounterparty());
        assertEquals("test-account", dto.getAccount());
        assertEquals("test-costCenter", dto.getCostCenter());
        assertEquals("test-projectId", dto.getProjectId());
        assertEquals(CashflowItemResponseDto.ItemStatusDto.PENDING, dto.getStatus());
        assertTrue(dto.getRecurring());
        assertEquals(CashflowItemResponseDto.RecurringFrequencyDto.DAILY, dto.getRecurringFrequency());
        assertEquals("test-parentRecurringItemId", dto.getParentRecurringItemId());
        assertEquals("test-paymentMethod", dto.getPaymentMethod());
        assertEquals("test-bankReference", dto.getBankReference());
        assertEquals("test-invoiceReference", dto.getInvoiceReference());
        assertEquals(BigDecimal.TEN, dto.getTaxAmount());
        assertEquals(BigDecimal.TEN, dto.getNetAmount());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-linkedExpenseId", dto.getLinkedExpenseId());
        assertEquals("test-linkedRevenueId", dto.getLinkedRevenueId());
        assertEquals(BigDecimal.TEN, dto.getAllocationPercentage());
    }

    @Test
    void testSettersAndGetters() {
        CashflowItemResponseDto dto = new CashflowItemResponseDto();
        dto.setId("val-id");
        dto.setCashflowItemId("val-cashflowItemId");
        dto.setTenantId("val-tenantId");
        dto.setRecordedBy("val-recordedBy");
        dto.setReference("val-reference");
        dto.setType(CashflowItemResponseDto.CashflowTypeDto.INFLOW);
        dto.setCategory(CashflowItemResponseDto.CashflowCategoryDto.OPERATING_REVENUE);
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setExpectedDate(LocalDate.of(2025,6,1));
        dto.setSettledDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setCounterparty("val-counterparty");
        dto.setAccount("val-account");
        dto.setCostCenter("val-costCenter");
        dto.setProjectId("val-projectId");
        dto.setStatus(CashflowItemResponseDto.ItemStatusDto.PENDING);
        dto.setRecurring(true);
        dto.setRecurringFrequency(CashflowItemResponseDto.RecurringFrequencyDto.DAILY);
        dto.setParentRecurringItemId("val-parentRecurringItemId");
        dto.setPaymentMethod("val-paymentMethod");
        dto.setBankReference("val-bankReference");
        dto.setInvoiceReference("val-invoiceReference");
        dto.setTaxAmount(BigDecimal.ONE);
        dto.setNetAmount(BigDecimal.ONE);
        dto.setNotes("val-notes");
        dto.setLinkedExpenseId("val-linkedExpenseId");
        dto.setLinkedRevenueId("val-linkedRevenueId");
        dto.setAllocationPercentage(BigDecimal.ONE);
        assertEquals("val-id", dto.getId());
        assertEquals("val-cashflowItemId", dto.getCashflowItemId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-recordedBy", dto.getRecordedBy());
        assertEquals("val-reference", dto.getReference());
        assertEquals(CashflowItemResponseDto.CashflowTypeDto.INFLOW, dto.getType());
        assertEquals(CashflowItemResponseDto.CashflowCategoryDto.OPERATING_REVENUE, dto.getCategory());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpectedDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getSettledDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-counterparty", dto.getCounterparty());
        assertEquals("val-account", dto.getAccount());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-projectId", dto.getProjectId());
        assertEquals(CashflowItemResponseDto.ItemStatusDto.PENDING, dto.getStatus());
        assertTrue(dto.getRecurring());
        assertEquals(CashflowItemResponseDto.RecurringFrequencyDto.DAILY, dto.getRecurringFrequency());
        assertEquals("val-parentRecurringItemId", dto.getParentRecurringItemId());
        assertEquals("val-paymentMethod", dto.getPaymentMethod());
        assertEquals("val-bankReference", dto.getBankReference());
        assertEquals("val-invoiceReference", dto.getInvoiceReference());
        assertEquals(BigDecimal.ONE, dto.getTaxAmount());
        assertEquals(BigDecimal.ONE, dto.getNetAmount());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-linkedExpenseId", dto.getLinkedExpenseId());
        assertEquals("val-linkedRevenueId", dto.getLinkedRevenueId());
        assertEquals(BigDecimal.ONE, dto.getAllocationPercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowItemResponseDto dto1 = CashflowItemResponseDto.builder()
                        .id("test-id")
            .cashflowItemId("test-cashflowItemId")
            .tenantId("test-tenantId")
            .recordedBy("test-recordedBy")
            .reference("test-reference")
            .type(CashflowItemResponseDto.CashflowTypeDto.INFLOW)
            .category(CashflowItemResponseDto.CashflowCategoryDto.OPERATING_REVENUE)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .transactionDate(LocalDate.of(2025,1,15))
            .expectedDate(LocalDate.of(2025,1,15))
            .settledDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .counterparty("test-counterparty")
            .account("test-account")
            .costCenter("test-costCenter")
            .projectId("test-projectId")
            .status(CashflowItemResponseDto.ItemStatusDto.PENDING)
            .recurring(true)
            .recurringFrequency(CashflowItemResponseDto.RecurringFrequencyDto.DAILY)
            .parentRecurringItemId("test-parentRecurringItemId")
            .expectedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .settledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .paymentMethod("test-paymentMethod")
            .bankReference("test-bankReference")
            .invoiceReference("test-invoiceReference")
            .taxAmount(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .linkedExpenseId("test-linkedExpenseId")
            .linkedRevenueId("test-linkedRevenueId")
            .allocationPercentage(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CashflowItemResponseDto dto2 = CashflowItemResponseDto.builder()
                        .id("test-id")
            .cashflowItemId("test-cashflowItemId")
            .tenantId("test-tenantId")
            .recordedBy("test-recordedBy")
            .reference("test-reference")
            .type(CashflowItemResponseDto.CashflowTypeDto.INFLOW)
            .category(CashflowItemResponseDto.CashflowCategoryDto.OPERATING_REVENUE)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .transactionDate(LocalDate.of(2025,1,15))
            .expectedDate(LocalDate.of(2025,1,15))
            .settledDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .counterparty("test-counterparty")
            .account("test-account")
            .costCenter("test-costCenter")
            .projectId("test-projectId")
            .status(CashflowItemResponseDto.ItemStatusDto.PENDING)
            .recurring(true)
            .recurringFrequency(CashflowItemResponseDto.RecurringFrequencyDto.DAILY)
            .parentRecurringItemId("test-parentRecurringItemId")
            .expectedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .settledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .paymentMethod("test-paymentMethod")
            .bankReference("test-bankReference")
            .invoiceReference("test-invoiceReference")
            .taxAmount(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .linkedExpenseId("test-linkedExpenseId")
            .linkedRevenueId("test-linkedRevenueId")
            .allocationPercentage(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                        .id("test-id")
            .cashflowItemId("test-cashflowItemId")
            .tenantId("test-tenantId")
            .recordedBy("test-recordedBy")
            .reference("test-reference")
            .type(CashflowItemResponseDto.CashflowTypeDto.INFLOW)
            .category(CashflowItemResponseDto.CashflowCategoryDto.OPERATING_REVENUE)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .transactionDate(LocalDate.of(2025,1,15))
            .expectedDate(LocalDate.of(2025,1,15))
            .settledDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .counterparty("test-counterparty")
            .account("test-account")
            .costCenter("test-costCenter")
            .projectId("test-projectId")
            .status(CashflowItemResponseDto.ItemStatusDto.PENDING)
            .recurring(true)
            .recurringFrequency(CashflowItemResponseDto.RecurringFrequencyDto.DAILY)
            .parentRecurringItemId("test-parentRecurringItemId")
            .expectedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .settledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .paymentMethod("test-paymentMethod")
            .bankReference("test-bankReference")
            .invoiceReference("test-invoiceReference")
            .taxAmount(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .linkedExpenseId("test-linkedExpenseId")
            .linkedRevenueId("test-linkedRevenueId")
            .allocationPercentage(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}