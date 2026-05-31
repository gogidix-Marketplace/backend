package com.gogidix.finance.cashflow.application.dto;

import com.gogidix.finance.cashflow.application.dto.response.CashflowItemResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cashflow DTO Tests")
class CashflowDtoTest {

    @Test
    @DisplayName("Should create cashflow item response DTO")
    void shouldCreateCashflowItemResponseDto() {
        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                .cashflowItemId("item-001")
                .tenantId("tenant-001")
                .recordedBy("user-001")
                .type(CashflowItemResponseDto.CashflowTypeDto.INFLOW)
                .category(CashflowItemResponseDto.CashflowCategoryDto.OPERATING_REVENUE)
                .amount(new BigDecimal("10000.00"))
                .currency("USD")
                .transactionDate(LocalDate.now())
                .description("Customer payment")
                .status(CashflowItemResponseDto.ItemStatusDto.PENDING)
                .recurring(false)
                .tags(List.of("priority"))
                .build();

        assertNotNull(dto);
        assertEquals("item-001", dto.getCashflowItemId());
        assertEquals("tenant-001", dto.getTenantId());
        assertEquals(CashflowItemResponseDto.CashflowTypeDto.INFLOW, dto.getType());
        assertEquals(CashflowItemResponseDto.CashflowCategoryDto.OPERATING_REVENUE, dto.getCategory());
        assertEquals(new BigDecimal("10000.00"), dto.getAmount());
        assertEquals("USD", dto.getCurrency());
        assertEquals("Customer payment", dto.getDescription());
        assertEquals(CashflowItemResponseDto.ItemStatusDto.PENDING, dto.getStatus());
        assertFalse(dto.getRecurring());
        assertTrue(dto.getTags().contains("priority"));
    }

    @Test
    @DisplayName("Should support all cashflow type DTOs")
    void shouldSupportAllCashflowTypeDtos() {
        assertEquals(2, CashflowItemResponseDto.CashflowTypeDto.values().length);
        assertEquals(CashflowItemResponseDto.CashflowTypeDto.INFLOW,
                CashflowItemResponseDto.CashflowTypeDto.valueOf("INFLOW"));
        assertEquals(CashflowItemResponseDto.CashflowTypeDto.OUTFLOW,
                CashflowItemResponseDto.CashflowTypeDto.valueOf("OUTFLOW"));
    }

    @Test
    @DisplayName("Should support all item status DTOs")
    void shouldSupportAllItemStatusDtos() {
        assertEquals(6, CashflowItemResponseDto.ItemStatusDto.values().length);
        assertEquals(CashflowItemResponseDto.ItemStatusDto.PENDING,
                CashflowItemResponseDto.ItemStatusDto.valueOf("PENDING"));
        assertEquals(CashflowItemResponseDto.ItemStatusDto.EXPECTED,
                CashflowItemResponseDto.ItemStatusDto.valueOf("EXPECTED"));
        assertEquals(CashflowItemResponseDto.ItemStatusDto.COMMITTED,
                CashflowItemResponseDto.ItemStatusDto.valueOf("COMMITTED"));
        assertEquals(CashflowItemResponseDto.ItemStatusDto.SETTLED,
                CashflowItemResponseDto.ItemStatusDto.valueOf("SETTLED"));
        assertEquals(CashflowItemResponseDto.ItemStatusDto.CANCELLED,
                CashflowItemResponseDto.ItemStatusDto.valueOf("CANCELLED"));
        assertEquals(CashflowItemResponseDto.ItemStatusDto.FAILED,
                CashflowItemResponseDto.ItemStatusDto.valueOf("FAILED"));
    }

    @Test
    @DisplayName("Should support all recurring frequency DTOs")
    void shouldSupportAllRecurringFrequencyDtos() {
        assertEquals(7, CashflowItemResponseDto.RecurringFrequencyDto.values().length);
        assertEquals(CashflowItemResponseDto.RecurringFrequencyDto.DAILY,
                CashflowItemResponseDto.RecurringFrequencyDto.valueOf("DAILY"));
        assertEquals(CashflowItemResponseDto.RecurringFrequencyDto.WEEKLY,
                CashflowItemResponseDto.RecurringFrequencyDto.valueOf("WEEKLY"));
        assertEquals(CashflowItemResponseDto.RecurringFrequencyDto.MONTHLY,
                CashflowItemResponseDto.RecurringFrequencyDto.valueOf("MONTHLY"));
        assertEquals(CashflowItemResponseDto.RecurringFrequencyDto.QUARTERLY,
                CashflowItemResponseDto.RecurringFrequencyDto.valueOf("QUARTERLY"));
        assertEquals(CashflowItemResponseDto.RecurringFrequencyDto.ANNUALLY,
                CashflowItemResponseDto.RecurringFrequencyDto.valueOf("ANNUALLY"));
    }

    @Test
    @DisplayName("Should create DTO with settlement details")
    void shouldCreateDtoWithSettlementDetails() {
        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                .cashflowItemId("item-002")
                .status(CashflowItemResponseDto.ItemStatusDto.SETTLED)
                .settledAt(Instant.now())
                .settledDate(LocalDate.now())
                .bankReference("BANK-REF-001")
                .build();

        assertEquals(CashflowItemResponseDto.ItemStatusDto.SETTLED, dto.getStatus());
        assertNotNull(dto.getSettledAt());
        assertNotNull(dto.getSettledDate());
        assertEquals("BANK-REF-001", dto.getBankReference());
    }

    @Test
    @DisplayName("Should create DTO with tax and net amount")
    void shouldCreateDtoWithTaxAndNetAmount() {
        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                .cashflowItemId("item-003")
                .amount(new BigDecimal("10000.00"))
                .taxAmount(new BigDecimal("500.00"))
                .netAmount(new BigDecimal("9500.00"))
                .build();

        assertEquals(new BigDecimal("10000.00"), dto.getAmount());
        assertEquals(new BigDecimal("500.00"), dto.getTaxAmount());
        assertEquals(new BigDecimal("9500.00"), dto.getNetAmount());
    }

    @Test
    @DisplayName("Should create DTO with recurring configuration")
    void shouldCreateDtoWithRecurringConfiguration() {
        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                .cashflowItemId("item-004")
                .recurring(true)
                .recurringFrequency(CashflowItemResponseDto.RecurringFrequencyDto.MONTHLY)
                .parentRecurringItemId("parent-001")
                .build();

        assertTrue(dto.getRecurring());
        assertEquals(CashflowItemResponseDto.RecurringFrequencyDto.MONTHLY, dto.getRecurringFrequency());
        assertEquals("parent-001", dto.getParentRecurringItemId());
    }

    @Test
    @DisplayName("Should create DTO with all optional fields")
    void shouldCreateDtoWithAllOptionalFields() {
        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                .cashflowItemId("item-005")
                .reference("REF-001")
                .counterparty("Vendor Inc.")
                .account("EXP-001")
                .costCenter("CC-001")
                .projectId("PROJ-001")
                .paymentMethod("BANK_TRANSFER")
                .invoiceReference("INV-001")
                .linkedExpenseId("exp-001")
                .linkedRevenueId("rev-001")
                .allocationPercentage(new BigDecimal("50.00"))
                .notes("Payment terms: 30 days")
                .build();

        assertEquals("REF-001", dto.getReference());
        assertEquals("Vendor Inc.", dto.getCounterparty());
        assertEquals("EXP-001", dto.getAccount());
        assertEquals("CC-001", dto.getCostCenter());
        assertEquals("PROJ-001", dto.getProjectId());
        assertEquals("BANK_TRANSFER", dto.getPaymentMethod());
        assertEquals("INV-001", dto.getInvoiceReference());
        assertEquals("exp-001", dto.getLinkedExpenseId());
        assertEquals("rev-001", dto.getLinkedRevenueId());
        assertEquals(new BigDecimal("50.00"), dto.getAllocationPercentage());
        assertEquals("Payment terms: 30 days", dto.getNotes());
    }

    @Test
    @DisplayName("Should create DTO with multiple tags")
    void shouldCreateDtoWithMultipleTags() {
        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                .cashflowItemId("item-006")
                .tags(List.of("priority", "recurring", "quarterly", "verified"))
                .build();

        assertEquals(4, dto.getTags().size());
        assertTrue(dto.getTags().contains("priority"));
        assertTrue(dto.getTags().contains("recurring"));
        assertTrue(dto.getTags().contains("quarterly"));
        assertTrue(dto.getTags().contains("verified"));
    }

    @Test
    @DisplayName("Should handle null values in optional fields")
    void shouldHandleNullValuesInOptionalFields() {
        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                .cashflowItemId("item-007")
                .build();

        assertEquals("item-007", dto.getCashflowItemId());
        assertNull(dto.getReference());
        assertNull(dto.getCounterparty());
        assertNull(dto.getTaxAmount());
        assertNull(dto.getTags());
    }

    @Test
    @DisplayName("Should create DTO for outflow transaction")
    void shouldCreateDtoForOutflowTransaction() {
        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                .cashflowItemId("item-008")
                .type(CashflowItemResponseDto.CashflowTypeDto.OUTFLOW)
                .category(CashflowItemResponseDto.CashflowCategoryDto.OPERATING_EXPENSE)
                .amount(new BigDecimal("5000.00"))
                .currency("EUR")
                .description("Vendor payment")
                .build();

        assertEquals(CashflowItemResponseDto.CashflowTypeDto.OUTFLOW, dto.getType());
        assertEquals(CashflowItemResponseDto.CashflowCategoryDto.OPERATING_EXPENSE, dto.getCategory());
        assertEquals(new BigDecimal("5000.00"), dto.getAmount());
        assertEquals("EUR", dto.getCurrency());
    }

    @Test
    @DisplayName("Should support different cashflow categories")
    void shouldSupportDifferentCashflowCategories() {
        CashflowItemResponseDto[] dtos = {
                CashflowItemResponseDto.builder()
                        .category(CashflowItemResponseDto.CashflowCategoryDto.OPERATING_REVENUE)
                        .build(),
                CashflowItemResponseDto.builder()
                        .category(CashflowItemResponseDto.CashflowCategoryDto.INVESTMENT_RETURN)
                        .build(),
                CashflowItemResponseDto.builder()
                        .category(CashflowItemResponseDto.CashflowCategoryDto.PAYROLL)
                        .build(),
                CashflowItemResponseDto.builder()
                        .category(CashflowItemResponseDto.CashflowCategoryDto.TAX_PAYMENT)
                        .build(),
                CashflowItemResponseDto.builder()
                        .category(CashflowItemResponseDto.CashflowCategoryDto.LOAN_REPAYMENT)
                        .build()
        };

        assertEquals(CashflowItemResponseDto.CashflowCategoryDto.OPERATING_REVENUE, dtos[0].getCategory());
        assertEquals(CashflowItemResponseDto.CashflowCategoryDto.INVESTMENT_RETURN, dtos[1].getCategory());
        assertEquals(CashflowItemResponseDto.CashflowCategoryDto.PAYROLL, dtos[2].getCategory());
        assertEquals(CashflowItemResponseDto.CashflowCategoryDto.TAX_PAYMENT, dtos[3].getCategory());
        assertEquals(CashflowItemResponseDto.CashflowCategoryDto.LOAN_REPAYMENT, dtos[4].getCategory());
    }

    @Test
    @DisplayName("Should create DTO with timestamps")
    void shouldCreateDtoWithTimestamps() {
        Instant now = Instant.now();

        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                .cashflowItemId("item-009")
                .expectedAt(now)
                .settledAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertNotNull(dto.getExpectedAt());
        assertNotNull(dto.getSettledAt());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle zero amount")
    void shouldHandleZeroAmount() {
        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                .cashflowItemId("item-010")
                .amount(BigDecimal.ZERO)
                .taxAmount(BigDecimal.ZERO)
                .netAmount(BigDecimal.ZERO)
                .build();

        assertEquals(BigDecimal.ZERO, dto.getAmount());
        assertEquals(BigDecimal.ZERO, dto.getTaxAmount());
        assertEquals(BigDecimal.ZERO, dto.getNetAmount());
    }

    @Test
    @DisplayName("Should handle large amounts")
    void shouldHandleLargeAmounts() {
        BigDecimal largeAmount = new BigDecimal("1000000000.00");

        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                .cashflowItemId("item-011")
                .amount(largeAmount)
                .build();

        assertEquals(largeAmount, dto.getAmount());
    }

    @Test
    @DisplayName("Should support different currencies")
    void shouldSupportDifferentCurrencies() {
        String[] currencies = {"USD", "EUR", "GBP", "JPY", "CAD", "AUD", "CHF", "CNY"};

        for (String currency : currencies) {
            CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                    .cashflowItemId("item-" + currency)
                    .currency(currency)
                    .build();

            assertEquals(currency, dto.getCurrency());
        }
    }

    @Test
    @DisplayName("Should create DTO for cancelled status")
    void shouldCreateDtoForCancelledStatus() {
        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                .cashflowItemId("item-012")
                .status(CashflowItemResponseDto.ItemStatusDto.CANCELLED)
                .notes("Cancelled by customer")
                .build();

        assertEquals(CashflowItemResponseDto.ItemStatusDto.CANCELLED, dto.getStatus());
        assertEquals("Cancelled by customer", dto.getNotes());
    }

    @Test
    @DisplayName("Should create DTO for failed status")
    void shouldCreateDtoForFailedStatus() {
        CashflowItemResponseDto dto = CashflowItemResponseDto.builder()
                .cashflowItemId("item-013")
                .status(CashflowItemResponseDto.ItemStatusDto.FAILED)
                .notes("Insufficient funds")
                .build();

        assertEquals(CashflowItemResponseDto.ItemStatusDto.FAILED, dto.getStatus());
        assertEquals("Insufficient funds", dto.getNotes());
    }

    @Test
    @DisplayName("Should use no-args constructor")
    void shouldUseNoArgsConstructor() {
        CashflowItemResponseDto dto = new CashflowItemResponseDto();

        assertNotNull(dto);
        dto.setCashflowItemId("item-014");
        dto.setAmount(new BigDecimal("1000.00"));

        assertEquals("item-014", dto.getCashflowItemId());
        assertEquals(new BigDecimal("1000.00"), dto.getAmount());
    }

    @Test
    @DisplayName("Should use all-args constructor")
    void shouldUseAllArgsConstructor() {
        Instant now = Instant.now();
        LocalDate today = LocalDate.now();

        CashflowItemResponseDto dto = new CashflowItemResponseDto(
                "id", "item-015", "tenant-001", "user-001", "REF-002",
                CashflowItemResponseDto.CashflowTypeDto.INFLOW,
                CashflowItemResponseDto.CashflowCategoryDto.OPERATING_REVENUE,
                new BigDecimal("1000.00"), "USD", today, today, today,
                "Test payment", "Counterparty", "Account", "CC-001", "PROJ-001",
                CashflowItemResponseDto.ItemStatusDto.PENDING, false,
                CashflowItemResponseDto.RecurringFrequencyDto.MONTHLY, "parent-001",
                now, now, "BANK_TRANSFER", "BANK-REF", "INV-002",
                new BigDecimal("100.00"), new BigDecimal("900.00"),
                List.of("tag1", "tag2"), "Notes", "exp-001", "rev-001",
                new BigDecimal("50.00"), now, now
        );

        assertEquals("item-015", dto.getCashflowItemId());
        assertEquals("tenant-001", dto.getTenantId());
        assertEquals(new BigDecimal("1000.00"), dto.getAmount());
        assertEquals(2, dto.getTags().size());
    }
}
