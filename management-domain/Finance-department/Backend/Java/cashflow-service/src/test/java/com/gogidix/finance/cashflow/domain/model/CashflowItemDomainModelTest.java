package com.gogidix.finance.cashflow.domain.model;

import com.gogidix.finance.cashflow.domain.event.CashflowItemRecordedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cashflow Item Domain Model Tests")
class CashflowItemDomainModelTest {

    private static final String TENANT_ID = "tenant-001";
    private static final String RECORDED_BY = "user-001";

    @Test
    @DisplayName("Should create cashflow item")
    void shouldCreateCashflowItem() {
        CashflowItem item = CashflowItem.create(
                TENANT_ID, RECORDED_BY,
                CashflowItem.CashflowType.INFLOW,
                CashflowItem.CashflowCategory.OPERATING_REVENUE,
                new BigDecimal("10000.00"), "USD",
                LocalDate.now(), "Customer payment"
        );

        assertNotNull(item);
        assertNotNull(item.getCashflowItemId());
        assertEquals(TENANT_ID, item.getTenantId());
        assertEquals(RECORDED_BY, item.getRecordedBy());
        assertEquals(CashflowItem.CashflowType.INFLOW, item.getType());
        assertEquals(CashflowItem.CashflowCategory.OPERATING_REVENUE, item.getCategory());
        assertEquals(new BigDecimal("10000.00"), item.getAmount());
        assertEquals("USD", item.getCurrency());
        assertEquals("Customer payment", item.getDescription());
        assertEquals(CashflowItem.ItemStatus.PENDING, item.getStatus());
        assertFalse(item.getRecurring());
    }

    @Test
    @DisplayName("Should create cashflow item with event")
    void shouldCreateCashflowItemWithEvent() {
        CashflowItem item = CashflowItem.create(
                TENANT_ID, RECORDED_BY,
                CashflowItem.CashflowType.OUTFLOW,
                CashflowItem.CashflowCategory.OPERATING_EXPENSE,
                new BigDecimal("5000.00"), "USD",
                LocalDate.now(), "Vendor payment"
        );

        assertEquals(1, item.getDomainEvents().size());
        assertEquals("CASHFLOW_ITEM_CREATED", item.getDomainEvents().get(0).getEventType());
    }

    @Test
    @DisplayName("Should mark item as expected")
    void shouldMarkItemAsExpected() {
        CashflowItem item = createTestInflowItem();

        item.markAsExpected();

        assertEquals(CashflowItem.ItemStatus.EXPECTED, item.getStatus());
        assertNotNull(item.getExpectedAt());
        assertEquals(2, item.getDomainEvents().size());
    }

    @Test
    @DisplayName("Should not mark non-pending item as expected")
    void shouldNotMarkNonPendingItemAsExpected() {
        CashflowItem item = createTestInflowItem();
        item.setStatus(CashflowItem.ItemStatus.SETTLED);

        assertThrows(IllegalStateException.class, item::markAsExpected);
    }

    @Test
    @DisplayName("Should commit expected item")
    void shouldCommitExpectedItem() {
        CashflowItem item = createTestInflowItem();
        item.markAsExpected();

        item.commit();

        assertEquals(CashflowItem.ItemStatus.COMMITTED, item.getStatus());
        assertEquals(3, item.getDomainEvents().size());
    }

    @Test
    @DisplayName("Should not commit non-expected item")
    void shouldNotCommitNonExpectedItem() {
        CashflowItem item = createTestInflowItem();

        assertThrows(IllegalStateException.class, item::commit);
    }

    @Test
    @DisplayName("Should settle committed item")
    void shouldSettleCommittedItem() {
        CashflowItem item = createTestInflowItem();
        item.markAsExpected();
        item.commit();
        String bankReference = "BANK-REF-001";

        item.settle(bankReference);

        assertEquals(CashflowItem.ItemStatus.SETTLED, item.getStatus());
        assertNotNull(item.getSettledAt());
        assertEquals(LocalDate.now(), item.getSettledDate());
        assertEquals(bankReference, item.getBankReference());
        assertEquals(4, item.getDomainEvents().size());
    }

    @Test
    @DisplayName("Should settle expected item")
    void shouldSettleExpectedItem() {
        CashflowItem item = createTestInflowItem();
        item.markAsExpected();
        String bankReference = "BANK-REF-002";

        item.settle(bankReference);

        assertEquals(CashflowItem.ItemStatus.SETTLED, item.getStatus());
        assertEquals(bankReference, item.getBankReference());
    }

    @Test
    @DisplayName("Should not settle settled item")
    void shouldNotSettleSettledItem() {
        CashflowItem item = createTestInflowItem();
        item.setStatus(CashflowItem.ItemStatus.SETTLED);

        assertThrows(IllegalStateException.class, () -> item.settle("BANK-REF"));
    }

    @Test
    @DisplayName("Should cancel pending item")
    void shouldCancelPendingItem() {
        CashflowItem item = createTestInflowItem();
        String reason = "Payment cancelled by customer";

        item.cancel(reason);

        assertEquals(CashflowItem.ItemStatus.CANCELLED, item.getStatus());
        assertTrue(item.getNotes().contains(reason));
        assertEquals(2, item.getDomainEvents().size());
    }

    @Test
    @DisplayName("Should not cancel settled item")
    void shouldNotCancelSettledItem() {
        CashflowItem item = createTestInflowItem();
        item.setStatus(CashflowItem.ItemStatus.SETTLED);

        assertThrows(IllegalStateException.class, () -> item.cancel("Reason"));
    }

    @Test
    @DisplayName("Should not cancel failed item")
    void shouldNotCancelFailedItem() {
        CashflowItem item = createTestInflowItem();
        item.setStatus(CashflowItem.ItemStatus.FAILED);

        assertThrows(IllegalStateException.class, () -> item.cancel("Reason"));
    }

    @Test
    @DisplayName("Should mark item as failed")
    void shouldMarkItemAsFailed() {
        CashflowItem item = createTestInflowItem();
        String reason = "Insufficient funds";

        item.markAsFailed(reason);

        assertEquals(CashflowItem.ItemStatus.FAILED, item.getStatus());
        assertTrue(item.getNotes().contains(reason));
    }

    @Test
    @DisplayName("Should not mark settled item as failed")
    void shouldNotMarkSettledItemAsFailed() {
        CashflowItem item = createTestInflowItem();
        item.setStatus(CashflowItem.ItemStatus.SETTLED);

        assertThrows(IllegalStateException.class, () -> item.markAsFailed("Reason"));
    }

    @Test
    @DisplayName("Should setup recurring configuration")
    void shouldSetupRecurringConfiguration() {
        CashflowItem item = createTestInflowItem();

        item.setupRecurring(CashflowItem.RecurringFrequency.MONTHLY);

        assertTrue(item.getRecurring());
        assertEquals(CashflowItem.RecurringFrequency.MONTHLY, item.getRecurringFrequency());
    }

    @Test
    @DisplayName("Should add tag to item")
    void shouldAddTagToItem() {
        CashflowItem item = createTestInflowItem();
        String tag = "recurring";

        item.addTag(tag);

        assertTrue(item.getTags().contains(tag));
    }

    @Test
    @DisplayName("Should not add duplicate tag")
    void shouldNotAddDuplicateTag() {
        CashflowItem item = createTestInflowItem();
        item.addTag("priority");
        item.addTag("priority");

        assertEquals(1, item.getTags().stream().filter(t -> t.equals("priority")).count());
    }

    @Test
    @DisplayName("Should remove tag from item")
    void shouldRemoveTagFromItem() {
        CashflowItem item = createTestInflowItem();
        item.addTag("priority");
        item.addTag("quarterly");

        item.removeTag("priority");

        assertFalse(item.getTags().contains("priority"));
        assertTrue(item.getTags().contains("quarterly"));
    }

    @Test
    @DisplayName("Should calculate net amount for inflow")
    void shouldCalculateNetAmountForInflow() {
        CashflowItem item = createTestInflowItem();
        BigDecimal taxAmount = new BigDecimal("500.00");

        item.calculateNetAmount(taxAmount);

        assertEquals(taxAmount, item.getTaxAmount());
        assertEquals(new BigDecimal("9500.00"), item.getNetAmount());
    }

    @Test
    @DisplayName("Should calculate net amount for outflow")
    void shouldCalculateNetAmountForOutflow() {
        CashflowItem item = createTestOutflowItem();
        BigDecimal taxAmount = new BigDecimal("500.00");

        item.calculateNetAmount(taxAmount);

        assertEquals(taxAmount, item.getTaxAmount());
        assertEquals(new BigDecimal("5500.00"), item.getNetAmount());
    }

    @Test
    @DisplayName("Should check if item is inflow")
    void shouldCheckIfIsInflow() {
        CashflowItem inflowItem = createTestInflowItem();
        CashflowItem outflowItem = createTestOutflowItem();

        assertTrue(inflowItem.isInflow());
        assertFalse(outflowItem.isInflow());
    }

    @Test
    @DisplayName("Should check if item is outflow")
    void shouldCheckIfIsOutflow() {
        CashflowItem inflowItem = createTestInflowItem();
        CashflowItem outflowItem = createTestOutflowItem();

        assertFalse(inflowItem.isOutflow());
        assertTrue(outflowItem.isOutflow());
    }

    @Test
    @DisplayName("Should add domain event")
    void shouldAddDomainEvent() {
        CashflowItem item = createTestInflowItem();
        int initialSize = item.getDomainEvents().size();

        item.addDomainEvent(CashflowItemRecordedEvent.builder()
                .cashflowItemId(item.getCashflowItemId())
                .tenantId(TENANT_ID)
                .eventType("TEST_EVENT")
                .timestamp(Instant.now())
                .build());

        assertEquals(initialSize + 1, item.getDomainEvents().size());
    }

    @Test
    @DisplayName("Should clear domain events")
    void shouldClearDomainEvents() {
        CashflowItem item = createTestInflowItem();

        item.clearDomainEvents();

        assertTrue(item.getDomainEvents().isEmpty());
    }

    @Test
    @DisplayName("Should support all cashflow types")
    void shouldSupportAllCashflowTypes() {
        assertEquals(2, CashflowItem.CashflowType.values().length);
        assertEquals(CashflowItem.CashflowType.INFLOW, CashflowItem.CashflowType.valueOf("INFLOW"));
        assertEquals(CashflowItem.CashflowType.OUTFLOW, CashflowItem.CashflowType.valueOf("OUTFLOW"));
    }

    @Test
    @DisplayName("Should support all item statuses")
    void shouldSupportAllItemStatuses() {
        assertEquals(6, CashflowItem.ItemStatus.values().length);
        assertEquals(CashflowItem.ItemStatus.PENDING, CashflowItem.ItemStatus.valueOf("PENDING"));
        assertEquals(CashflowItem.ItemStatus.EXPECTED, CashflowItem.ItemStatus.valueOf("EXPECTED"));
        assertEquals(CashflowItem.ItemStatus.COMMITTED, CashflowItem.ItemStatus.valueOf("COMMITTED"));
        assertEquals(CashflowItem.ItemStatus.SETTLED, CashflowItem.ItemStatus.valueOf("SETTLED"));
        assertEquals(CashflowItem.ItemStatus.CANCELLED, CashflowItem.ItemStatus.valueOf("CANCELLED"));
        assertEquals(CashflowItem.ItemStatus.FAILED, CashflowItem.ItemStatus.valueOf("FAILED"));
    }

    @Test
    @DisplayName("Should support all recurring frequencies")
    void shouldSupportAllRecurringFrequencies() {
        assertEquals(7, CashflowItem.RecurringFrequency.values().length);
        assertEquals(CashflowItem.RecurringFrequency.DAILY, CashflowItem.RecurringFrequency.valueOf("DAILY"));
        assertEquals(CashflowItem.RecurringFrequency.WEEKLY, CashflowItem.RecurringFrequency.valueOf("WEEKLY"));
        assertEquals(CashflowItem.RecurringFrequency.BI_WEEKLY, CashflowItem.RecurringFrequency.valueOf("BI_WEEKLY"));
        assertEquals(CashflowItem.RecurringFrequency.MONTHLY, CashflowItem.RecurringFrequency.valueOf("MONTHLY"));
        assertEquals(CashflowItem.RecurringFrequency.QUARTERLY, CashflowItem.RecurringFrequency.valueOf("QUARTERLY"));
        assertEquals(CashflowItem.RecurringFrequency.SEMI_ANNUALLY, CashflowItem.RecurringFrequency.valueOf("SEMI_ANNUALLY"));
        assertEquals(CashflowItem.RecurringFrequency.ANNUALLY, CashflowItem.RecurringFrequency.valueOf("ANNUALLY"));
    }

    private CashflowItem createTestInflowItem() {
        return CashflowItem.create(
                TENANT_ID, RECORDED_BY,
                CashflowItem.CashflowType.INFLOW,
                CashflowItem.CashflowCategory.OPERATING_REVENUE,
                new BigDecimal("10000.00"), "USD",
                LocalDate.now(), "Customer payment"
        );
    }

    private CashflowItem createTestOutflowItem() {
        return CashflowItem.create(
                TENANT_ID, RECORDED_BY,
                CashflowItem.CashflowType.OUTFLOW,
                CashflowItem.CashflowCategory.OPERATING_EXPENSE,
                new BigDecimal("5000.00"), "USD",
                LocalDate.now(), "Vendor payment"
        );
    }
}
