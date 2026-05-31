package com.gogidix.marketing.budget.domain.model;

import com.gogidix.digitalmarketing.budgetmanagement.domain.model.BudgetTransaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;

/**
 * Unit tests for BudgetTransaction domain model.
 *
 * Tests cover:
 * - Transaction creation and initialization
 * - Transaction approval and rejection
 * - Status checks
 * - Transaction metadata
 */
@DisplayName("BudgetTransaction Domain Model Tests")
class BudgetTransactionTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String BUDGET_ID = "budget-456";
    private static final String TRANSACTION_TYPE = "ALLOCATE";
    private static final BigDecimal AMOUNT = new BigDecimal("50000");
    private static final String USER_ID = "user-789";

    private BudgetTransaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new BudgetTransaction(TENANT_ID, BUDGET_ID, TRANSACTION_TYPE, AMOUNT);
    }

    @Test
    @DisplayName("Should create transaction with required fields")
    void testCreateTransactionWithRequiredFields() {
        assertNotNull(transaction.getId(), "ID should be auto-generated");
        assertEquals(TENANT_ID, transaction.getTenantId(), "Tenant ID should match");
        assertEquals(BUDGET_ID, transaction.getBudgetId(), "Budget ID should match");
        assertEquals(TRANSACTION_TYPE, transaction.getType(), "Transaction type should match");
        assertEquals(AMOUNT, transaction.getAmount(), "Amount should match");
        assertEquals("PENDING", transaction.getStatus(), "Status should default to PENDING");
        assertNotNull(transaction.getTransactionDate(), "Transaction date should be set");
    }

    @Test
    @DisplayName("Should approve transaction")
    void testApproveTransaction() {
        transaction.approve(USER_ID);

        assertEquals("APPROVED", transaction.getStatus(), "Status should be APPROVED");
        assertEquals(USER_ID, transaction.getApprovedBy(), "Approved by should match");
    }

    @Test
    @DisplayName("Should return true when transaction is approved")
    void testIsApproved() {
        assertFalse(transaction.isApproved(), "Transaction should not be approved initially");

        transaction.approve(USER_ID);

        assertTrue(transaction.isApproved(), "Transaction should be approved after approval");
    }

    @Test
    @DisplayName("Should reject transaction")
    void testRejectTransaction() {
        transaction.reject();

        assertEquals("REJECTED", transaction.getStatus(), "Status should be REJECTED");
    }

    @Test
    @DisplayName("Should return true when transaction is rejected")
    void testIsRejected() {
        transaction.reject();

        assertTrue(transaction.isRejected(), "Transaction should be rejected");
    }

    @Test
    @DisplayName("Should return true when transaction is pending")
    void testIsPending() {
        assertTrue(transaction.isPending(), "Transaction should be pending initially");

        transaction.approve(USER_ID);

        assertFalse(transaction.isPending(), "Transaction should not be pending after approval");
    }

    @Test
    @DisplayName("Should set description")
    void testSetDescription() {
        String description = "Q1 digital marketing allocation";
        transaction.setDescription(description);

        assertEquals(description, transaction.getDescription(), "Description should be set");
    }

    @Test
    @DisplayName("Should set campaign ID")
    void testSetCampaignId() {
        transaction.setCampaignId("campaign-123");

        assertEquals("campaign-123", transaction.getCampaignId(), "Campaign ID should be set");
    }

    @Test
    @DisplayName("Should set channel ID")
    void testSetChannelId() {
        transaction.setChannelId("channel-456");

        assertEquals("channel-456", transaction.getChannelId(), "Channel ID should be set");
    }

    @Test
    @DisplayName("Should set category")
    void testSetCategory() {
        transaction.setCategory("DIGITAL_MARKETING");

        assertEquals("DIGITAL_MARKETING", transaction.getCategory(), "Category should be set");
    }

    @Test
    @DisplayName("Should set reference number")
    void testSetReferenceNumber() {
        transaction.setReferenceNumber("REF-2024-001");

        assertEquals("REF-2024-001", transaction.getReferenceNumber(), "Reference number should be set");
    }

    @Test
    @DisplayName("Should set initiated by")
    void testSetInitiatedBy() {
        transaction.setInitiatedBy(USER_ID);

        assertEquals(USER_ID, transaction.getInitiatedBy(), "Initiated by should be set");
    }

    @Test
    @DisplayName("Should set currency")
    void testSetCurrency() {
        transaction.setCurrency("USD");

        assertEquals("USD", transaction.getCurrency(), "Currency should be set");
    }

    @Test
    @DisplayName("Should set exchange rate")
    void testSetExchangeRate() {
        transaction.setExchangeRate(new BigDecimal("1.08"));

        assertEquals(new BigDecimal("1.08"), transaction.getExchangeRate(), "Exchange rate should be set");
    }

    @Test
    @DisplayName("Should set vendor")
    void testSetVendor() {
        transaction.setVendor("Acme Marketing Corp");

        assertEquals("Acme Marketing Corp", transaction.getVendor(), "Vendor should be set");
    }

    @Test
    @DisplayName("Should set invoice number")
    void testSetInvoiceNumber() {
        transaction.setInvoiceNumber("INV-2024-12345");

        assertEquals("INV-2024-12345", transaction.getInvoiceNumber(), "Invoice number should be set");
    }

    @Test
    @DisplayName("Should handle metadata")
    void testSetMetadata() {
        transaction.setMetadata(new HashMap<>());
        transaction.getMetadata().put("region", "US");
        transaction.getMetadata().put("quarter", "Q1");
        transaction.getMetadata().put("approvalRequired", true);

        assertEquals(3, transaction.getMetadata().size(), "Should have 3 metadata entries");
        assertEquals("US", transaction.getMetadata().get("region"), "Region metadata should match");
    }

    @Test
    @DisplayName("Should handle different transaction types")
    void testDifferentTransactionTypes() {
        String[] types = {"ALLOCATE", "COMMIT", "SPEND", "REFUND", "TRANSFER", "ADJUSTMENT"};

        for (String type : types) {
            BudgetTransaction t = new BudgetTransaction(TENANT_ID, BUDGET_ID, type, AMOUNT);
            assertEquals(type, t.getType(), "Transaction type should be " + type);
        }
    }

    @Test
    @DisplayName("Should handle different statuses")
    void testDifferentStatuses() {
        String[] statuses = {"PENDING", "APPROVED", "REJECTED", "PROCESSED", "CANCELLED"};

        for (String status : statuses) {
            BudgetTransaction t = new BudgetTransaction(TENANT_ID, BUDGET_ID, TRANSACTION_TYPE, AMOUNT);
            t.setStatus(status);
            assertEquals(status, t.getStatus(), "Status should be " + status);
        }
    }

    @Test
    @DisplayName("Should support builder pattern")
    void testBuilderPattern() {
        BudgetTransaction builtTransaction = BudgetTransaction.builder()
            .tenantId(TENANT_ID)
            .budgetId(BUDGET_ID)
            .type("SPEND")
            .amount(new BigDecimal("25000"))
            .description("Google Ads spend")
            .campaignId("campaign-123")
            .category("PPC")
            .currency("USD")
            .status("APPROVED")
            .build();

        assertEquals("SPEND", builtTransaction.getType(), "Type should match builder value");
        assertEquals("Google Ads spend", builtTransaction.getDescription(), "Description should match builder value");
        assertEquals("campaign-123", builtTransaction.getCampaignId(), "Campaign ID should match builder value");
        assertEquals("APPROVED", builtTransaction.getStatus(), "Status should match builder value");
    }

    @Test
    @DisplayName("Should track transaction date")
    void testTransactionDateIsSet() {
        assertNotNull(transaction.getTransactionDate(), "Transaction date should be set");
        assertTrue(transaction.getTransactionDate().isAfter(Instant.now().minusSeconds(60)),
            "Transaction date should be recent");
    }
}
