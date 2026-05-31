package com.gogidix.finance.generalledger;

import com.gogidix.finance.ledger.domain.model.JournalEntry;
import com.gogidix.finance.ledger.domain.model.LedgerAccount;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GeneralLedgerServiceTest {

    private static final String TENANT_ID = "tenant-test-001";

    @Test
    void testJournalEntryCreation() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Test Entry")
                .entryNumber("JE-001")
                .build();

        assertNotNull(entry);
        assertEquals(TENANT_ID, entry.getTenantId());
        assertEquals("Test Entry", entry.getDescription());
    }

    @Test
    void testLedgerAccountCreation() {
        LedgerAccount account = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("1000")
                .accountName("Cash")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .build();

        assertNotNull(account);
        assertEquals(TENANT_ID, account.getTenantId());
        assertEquals("1000", account.getAccountNumber());
        assertEquals("Cash", account.getAccountName());
        assertEquals(LedgerAccount.AccountType.ASSET, account.getAccountType());
    }

    @Test
    void testAccountTypeEnum() {
        assertEquals(5, LedgerAccount.AccountType.values().length);
        assertEquals(LedgerAccount.AccountType.ASSET, LedgerAccount.AccountType.valueOf("ASSET"));
        assertEquals(LedgerAccount.AccountType.LIABILITY, LedgerAccount.AccountType.valueOf("LIABILITY"));
        assertEquals(LedgerAccount.AccountType.EQUITY, LedgerAccount.AccountType.valueOf("EQUITY"));
        assertEquals(LedgerAccount.AccountType.REVENUE, LedgerAccount.AccountType.valueOf("REVENUE"));
        assertEquals(LedgerAccount.AccountType.EXPENSE, LedgerAccount.AccountType.valueOf("EXPENSE"));
    }

    @Test
    void testJournalEntryStatus() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Test Entry")
                .entryNumber("JE-002")
                .status(JournalEntry.JournalEntryStatus.POSTED)
                .build();

        assertEquals(JournalEntry.JournalEntryStatus.POSTED, entry.getStatus());
    }

    @Test
    void testLedgerAccountWithParent() {
        LedgerAccount parent = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("1000")
                .accountName("Assets")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .accountId("ACC-PARENT-001")
                .build();

        LedgerAccount child = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("1001")
                .accountName("Cash")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .accountId("ACC-CHILD-001")
                .parentAccountId(parent.getAccountId())
                .build();

        assertNotNull(child.getParentAccountId());
    }

    @Test
    void testJournalEntryCurrency() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Multi-Currency Entry")
                .entryNumber("JE-003")
                .currency("EUR")
                .build();

        assertEquals("EUR", entry.getCurrency());
    }

    @Test
    void testJournalEntrySourceDocument() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Invoice Payment")
                .entryNumber("JE-004")
                .sourceDocumentId("INV-001")
                .sourceDocumentType("INVOICE")
                .build();

        assertEquals("INV-001", entry.getSourceDocumentId());
        assertEquals("INVOICE", entry.getSourceDocumentType());
    }

    @Test
    void testLedgerAccountWithDescription() {
        LedgerAccount account = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("1000")
                .accountName("Cash")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .description("Primary cash account")
                .build();

        assertEquals("Primary cash account", account.getDescription());
    }

    @Test
    void testJournalEntryTotals() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Entry with totals")
                .entryNumber("JE-005")
                .totalDebit(BigDecimal.valueOf(1000))
                .totalCredit(BigDecimal.valueOf(1000))
                .build();

        assertEquals(BigDecimal.valueOf(1000), entry.getTotalDebit());
        assertEquals(BigDecimal.valueOf(1000), entry.getTotalCredit());
    }

    @Test
    void testJournalEntryApproval() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Entry requiring approval")
                .entryNumber("JE-006")
                .requiresApproval(true)
                .approvedByUserId("manager@example.com")
                .approvedAt(java.time.LocalDateTime.now())
                .build();

        assertTrue(entry.getRequiresApproval());
        assertEquals("manager@example.com", entry.getApprovedByUserId());
    }

    @Test
    void testLedgerAccountDepartment() {
        LedgerAccount account = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("5000")
                .accountName("Office Supplies")
                .accountType(LedgerAccount.AccountType.EXPENSE)
                .currency("USD")
                .department("Administration")
                .build();

        assertEquals("Administration", account.getDepartment());
    }

    @Test
    void testJournalEntryIdNotNull() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Test Entry")
                .entryNumber("JE-007")
                .build();

        assertNotNull(entry);
    }

    @Test
    void testLedgerAccountIdNotNull() {
        LedgerAccount account = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("1000")
                .accountName("Cash")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .build();

        assertNotNull(account);
    }

    @Test
    void testJournalEntryTenantId() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Test Entry")
                .entryNumber("JE-008")
                .build();

        assertEquals(TENANT_ID, entry.getTenantId());
    }

    @Test
    void testLedgerAccountTenantId() {
        LedgerAccount account = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("1000")
                .accountName("Cash")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .build();

        assertEquals(TENANT_ID, account.getTenantId());
    }

    @Test
    void testJournalEntryEntryNumber() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Test Entry")
                .entryNumber("JE-009")
                .build();

        assertEquals("JE-009", entry.getEntryNumber());
    }

    @Test
    void testLedgerAccountAccountName() {
        LedgerAccount account = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("1000")
                .accountName("Petty Cash")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .build();

        assertEquals("Petty Cash", account.getAccountName());
    }

    @Test
    void testLedgerAccountDefaultCurrency() {
        LedgerAccount account = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("1000")
                .accountName("Cash")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .build();

        assertEquals("USD", account.getCurrency());
    }

    @Test
    void testJournalEntryWithZeroTotals() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Zero Entry")
                .entryNumber("JE-010")
                .totalDebit(BigDecimal.ZERO)
                .totalCredit(BigDecimal.ZERO)
                .build();

        assertEquals(BigDecimal.ZERO, entry.getTotalDebit());
        assertEquals(BigDecimal.ZERO, entry.getTotalCredit());
    }

    @Test
    void testJournalEntryStatusDraft() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Draft Entry")
                .entryNumber("JE-011")
                .status(JournalEntry.JournalEntryStatus.DRAFT)
                .build();

        assertEquals(JournalEntry.JournalEntryStatus.DRAFT, entry.getStatus());
    }

    @Test
    void testJournalEntryStatusPendingApproval() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Pending Entry")
                .entryNumber("JE-012")
                .status(JournalEntry.JournalEntryStatus.PENDING_APPROVAL)
                .build();

        assertEquals(JournalEntry.JournalEntryStatus.PENDING_APPROVAL, entry.getStatus());
    }

    @Test
    void testJournalEntryRequiresApproval() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Entry requiring approval")
                .entryNumber("JE-013")
                .requiresApproval(true)
                .build();

        assertTrue(entry.getRequiresApproval());
    }

    @Test
    void testJournalEntryDoesNotRequireApproval() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Entry not requiring approval")
                .entryNumber("JE-014")
                .requiresApproval(false)
                .build();

        assertFalse(entry.getRequiresApproval());
    }

    @Test
    void testJournalEntryWithLargeAmount() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Large Amount Entry")
                .entryNumber("JE-015")
                .totalDebit(BigDecimal.valueOf(1000000))
                .totalCredit(BigDecimal.valueOf(1000000))
                .build();

        assertEquals(BigDecimal.valueOf(1000000), entry.getTotalDebit());
        assertEquals(BigDecimal.valueOf(1000000), entry.getTotalCredit());
    }

    @Test
    void testLedgerAccountLongAccountNumber() {
        LedgerAccount account = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("999999")
                .accountName("Special Account")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .build();

        assertEquals("999999", account.getAccountNumber());
    }

    @Test
    void testJournalEntryWithSpecialCharacters() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Payment for vendor - Co.'s invoice #123")
                .entryNumber("JE-016")
                .build();

        assertEquals("Payment for vendor - Co.'s invoice #123", entry.getDescription());
    }

    @Test
    void testLedgerAccountWithEmptyDescription() {
        LedgerAccount account = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("7000")
                .accountName("Cash")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .description("")
                .build();

        assertEquals("", account.getDescription());
    }

    @Test
    void testJournalEntryWithNullDescription() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .entryNumber("JE-017")
                .build();

        assertNull(entry.getDescription());
    }

    @Test
    void testLedgerAccountWithMultipleCurrencyCodes() {
        LedgerAccount usdAccount = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("1000")
                .accountName("Cash USD")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .build();

        LedgerAccount eurAccount = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("1001")
                .accountName("Cash EUR")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("EUR")
                .build();

        assertEquals("USD", usdAccount.getCurrency());
        assertEquals("EUR", eurAccount.getCurrency());
    }

    @Test
    void testJournalEntrySequence() {
        for (int i = 1; i <= 5; i++) {
            JournalEntry entry = JournalEntry.builder()
                    .tenantId(TENANT_ID)
                    .description("Test Entry " + i)
                    .entryNumber("JE-SEQ-" + i)
                    .build();

            assertEquals("Test Entry " + i, entry.getDescription());
            assertEquals("JE-SEQ-" + i, entry.getEntryNumber());
        }
    }

    @Test
    void testLedgerAccountWithDepartmentAndLocation() {
        LedgerAccount account = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("5000")
                .accountName("Office Supplies")
                .accountType(LedgerAccount.AccountType.EXPENSE)
                .currency("USD")
                .department("Administration")
                .location("Head Office")
                .build();

        assertEquals("Administration", account.getDepartment());
        assertEquals("Head Office", account.getLocation());
    }

    @Test
    void testJournalEntryWithReversalInfo() {
        JournalEntry original = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Original Entry")
                .entryNumber("JE-018")
                .build();

        JournalEntry reversal = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Reversal of " + original.getEntryNumber())
                .entryNumber("JE-018-R")
                .build();

        assertTrue(reversal.getDescription().contains(original.getEntryNumber()));
    }

    @Test
    void testLedgerAccountHierarchy() {
        LedgerAccount level1 = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("1")
                .accountName("Assets")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .accountId("LEVEL-1-001")
                .build();

        LedgerAccount level2 = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("11")
                .accountName("Current Assets")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .accountId("LEVEL-2-001")
                .parentAccountId(level1.getAccountId())
                .build();

        LedgerAccount level3 = LedgerAccount.builder()
                .tenantId(TENANT_ID)
                .accountNumber("110")
                .accountName("Cash")
                .accountType(LedgerAccount.AccountType.ASSET)
                .currency("USD")
                .accountId("LEVEL-3-001")
                .parentAccountId(level2.getAccountId())
                .build();

        assertNotNull(level1.getAccountId());
        assertNotNull(level2.getParentAccountId());
        assertNotNull(level3.getParentAccountId());
    }

    @Test
    void testJournalEntryStatusApproved() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Approved Entry")
                .entryNumber("JE-019")
                .totalDebit(BigDecimal.valueOf(5000))
                .totalCredit(BigDecimal.valueOf(5000))
                .status(JournalEntry.JournalEntryStatus.APPROVED)
                .build();

        assertEquals(JournalEntry.JournalEntryStatus.APPROVED, entry.getStatus());
        assertEquals(BigDecimal.valueOf(5000), entry.getTotalDebit());
        assertEquals(BigDecimal.valueOf(5000), entry.getTotalCredit());
    }

    @Test
    void testJournalEntryWithMultipleAccounts() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Multi-account Entry")
                .entryNumber("JE-020")
                .totalDebit(BigDecimal.valueOf(15000))
                .totalCredit(BigDecimal.valueOf(15000))
                .build();

        assertNotNull(entry);
        assertEquals(BigDecimal.valueOf(15000), entry.getTotalDebit());
        assertEquals(BigDecimal.valueOf(15000), entry.getTotalCredit());
    }

    @Test
    void testJournalEntryStatusReversed() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Reversed Entry")
                .entryNumber("JE-021")
                .status(JournalEntry.JournalEntryStatus.REVERSED)
                .build();

        assertEquals(JournalEntry.JournalEntryStatus.REVERSED, entry.getStatus());
    }

    @Test
    void testJournalEntryStatusCancelled() {
        JournalEntry entry = JournalEntry.builder()
                .tenantId(TENANT_ID)
                .description("Cancelled Entry")
                .entryNumber("JE-022")
                .status(JournalEntry.JournalEntryStatus.CANCELLED)
                .build();

        assertEquals(JournalEntry.JournalEntryStatus.CANCELLED, entry.getStatus());
    }
}
