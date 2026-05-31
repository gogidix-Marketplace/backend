package com.gogidix.marketing.budget.domain.model;

import com.gogidix.digitalmarketing.budgetmanagement.domain.model.Budget;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Unit tests for Budget domain model.
 *
 * Tests cover:
 * - Budget creation and initialization
 * - Budget allocation
 * - Fund commitment and spending
 * - Budget approval and activation
 * - Status checks and calculations
 */
@DisplayName("Budget Domain Model Tests")
class BudgetTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String BUDGET_NAME = "Annual Marketing Budget 2024";
    private static final String FISCAL_YEAR = "2024";
    private static final BigDecimal TOTAL_AMOUNT = new BigDecimal("1000000");
    private static final String USER_ID = "user-456";

    private Budget budget;

    @BeforeEach
    void setUp() {
        budget = new Budget(TENANT_ID, BUDGET_NAME, FISCAL_YEAR, TOTAL_AMOUNT);
    }

    @Test
    @DisplayName("Should create budget with required fields")
    void testCreateBudgetWithRequiredFields() {
        assertNotNull(budget.getId(), "ID should be auto-generated");
        assertEquals(TENANT_ID, budget.getTenantId(), "Tenant ID should match");
        assertEquals(BUDGET_NAME, budget.getName(), "Budget name should match");
        assertEquals(FISCAL_YEAR, budget.getFiscalYear(), "Fiscal year should match");
        assertEquals(TOTAL_AMOUNT, budget.getTotalAmount(), "Total amount should match");
        assertEquals("DRAFT", budget.getStatus(), "Status should default to DRAFT");
        assertEquals("USD", budget.getCurrency(), "Currency should default to USD");
    }

    @Test
    @DisplayName("Should initialize amounts correctly")
    void testInitializeAmounts() {
        assertEquals(BigDecimal.ZERO, budget.getAllocatedAmount(), "Allocated amount should be zero");
        assertEquals(TOTAL_AMOUNT, budget.getRemainingAmount(), "Remaining amount should equal total");
        assertEquals(BigDecimal.ZERO, budget.getCommittedAmount(), "Committed amount should be zero");
        assertEquals(BigDecimal.ZERO, budget.getSpentAmount(), "Spent amount should be zero");
    }

    @Test
    @DisplayName("Should initialize empty collections")
    void testInitializeEmptyCollections() {
        assertNotNull(budget.getAllocations(), "Allocations list should be initialized");
        assertNotNull(budget.getPeriods(), "Periods list should be initialized");
        assertNotNull(budget.getMetadata(), "Metadata map should be initialized");
        assertTrue(budget.getAllocations().isEmpty(), "Allocations should be empty");
        assertTrue(budget.getPeriods().isEmpty(), "Periods should be empty");
        assertTrue(budget.getMetadata().isEmpty(), "Metadata should be empty");
    }

    @Test
    @DisplayName("Should allocate funds to category")
    void testAllocateFunds() {
        BigDecimal allocationAmount = new BigDecimal("100000");
        budget.allocate("DIGITAL_MARKETING", allocationAmount, "2024-01-01", "2024-12-31");

        assertEquals(1, budget.getAllocations().size(), "Should have 1 allocation");
        assertEquals(allocationAmount, budget.getAllocatedAmount(), "Allocated amount should be updated");
        assertEquals(new BigDecimal("900000"), budget.getRemainingAmount(),
            "Remaining amount should be reduced");
    }

    @Test
    @DisplayName("Should track multiple allocations")
    void testMultipleAllocations() {
        budget.allocate("DIGITAL_MARKETING", new BigDecimal("300000"), "2024-01-01", "2024-12-31");
        budget.allocate("PRINT_MEDIA", new BigDecimal("200000"), "2024-01-01", "2024-06-30");
        budget.allocate("EVENTS", new BigDecimal("150000"), "2024-06-01", "2024-12-31");

        assertEquals(3, budget.getAllocations().size(), "Should have 3 allocations");
        assertEquals(new BigDecimal("650000"), budget.getAllocatedAmount(),
            "Total allocated should be sum of allocations");
        assertEquals(new BigDecimal("350000"), budget.getRemainingAmount(),
            "Remaining should be total minus allocated");
    }

    @Test
    @DisplayName("Should check if sufficient funds remain")
    void testHasRemainingFunds() {
        budget.allocate("DIGITAL_MARKETING", new BigDecimal("500000"), "2024-01-01", "2024-12-31");

        assertTrue(budget.hasRemainingFunds(new BigDecimal("400000")),
            "Should have sufficient funds");
        assertTrue(budget.hasRemainingFunds(new BigDecimal("500000")),
            "Should have exact remaining amount");
        assertFalse(budget.hasRemainingFunds(new BigDecimal("600000")),
            "Should not have sufficient funds");
    }

    @Test
    @DisplayName("Should commit funds from budget")
    void testCommitFunds() {
        BigDecimal commitAmount = new BigDecimal("100000");
        budget.commit(commitAmount);

        assertEquals(commitAmount, budget.getCommittedAmount(),
            "Committed amount should be updated");
    }

    @Test
    @DisplayName("Should reject commit when insufficient funds")
    void testCommitWithInsufficientFunds() {
        budget.allocate("DIGITAL_MARKETING", new BigDecimal("900000"), "2024-01-01", "2024-12-31");

        assertThrows(IllegalStateException.class, () -> {
            budget.commit(new BigDecimal("200000"));
        }, "Should throw exception when committing more than remaining");
    }

    @Test
    @DisplayName("Should reject commit with negative amount")
    void testCommitWithNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            budget.commit(new BigDecimal("-1000"));
        }, "Should throw exception for negative amount");
    }

    @Test
    @DisplayName("Should reject commit with zero amount")
    void testCommitWithZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            budget.commit(BigDecimal.ZERO);
        }, "Should throw exception for zero amount");
    }

    @Test
    @DisplayName("Should spend funds from budget")
    void testSpendFunds() {
        BigDecimal spendAmount = new BigDecimal("50000");
        budget.spend(spendAmount);

        assertEquals(spendAmount, budget.getSpentAmount(),
            "Spent amount should be updated");
    }

    @Test
    @DisplayName("Should track multiple spends")
    void testMultipleSpends() {
        budget.spend(new BigDecimal("30000"));
        budget.spend(new BigDecimal("25000"));
        budget.spend(new BigDecimal("45000"));

        assertEquals(new BigDecimal("100000"), budget.getSpentAmount(),
            "Spent amount should be sum of all spends");
    }

    @Test
    @DisplayName("Should reject spend with negative amount")
    void testSpendWithNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            budget.spend(new BigDecimal("-1000"));
        }, "Should throw exception for negative spend amount");
    }

    @Test
    @DisplayName("Should calculate unallocated amount")
    void testGetUnallocatedAmount() {
        budget.allocate("DIGITAL_MARKETING", new BigDecimal("300000"), "2024-01-01", "2024-12-31");
        budget.allocate("PRINT_MEDIA", new BigDecimal("200000"), "2024-01-01", "2024-06-30");

        assertEquals(new BigDecimal("500000"), budget.getUnallocatedAmount(),
            "Unallocated amount should be total minus allocated");
    }

    @Test
    @DisplayName("Should calculate utilization percentage")
    void testGetUtilizationPercentage() {
        budget.spend(new BigDecimal("250000"));

        BigDecimal utilization = budget.getUtilizationPercentage();
        assertEquals(new BigDecimal("25.0000"), utilization,
            "Utilization should be 25%");
    }

    @Test
    @DisplayName("Should return null utilization when total is zero")
    void testGetUtilizationPercentageWithZeroTotal() {
        Budget zeroBudget = new Budget(TENANT_ID, "Zero Budget", FISCAL_YEAR, BigDecimal.ZERO);

        assertNull(zeroBudget.getUtilizationPercentage(),
            "Utilization should be null when total is zero");
    }

    @Test
    @DisplayName("Should return true when budget is active")
    void testIsActive() {
        budget.setStatus("ACTIVE");
        assertTrue(budget.isActive(), "Budget should be active");
    }

    @Test
    @DisplayName("Should return false when budget is not active")
    void testIsNotActive() {
        budget.setStatus("DRAFT");
        assertFalse(budget.isActive(), "Budget should not be active");
    }

    @Test
    @DisplayName("Should return true when budget is approved")
    void testIsApproved() {
        budget.approve(USER_ID);
        assertTrue(budget.isApproved(), "Budget should be approved");
    }

    @Test
    @DisplayName("Should approve budget")
    void testApproveBudget() {
        budget.approve(USER_ID);

        assertEquals("APPROVED", budget.getStatus(), "Status should be APPROVED");
        assertEquals(USER_ID, budget.getApprover(), "Approver should be set");
        assertNotNull(budget.getApprovedAt(), "Approved at should be set");
    }

    @Test
    @DisplayName("Should activate approved budget")
    void testActivateApprovedBudget() {
        budget.approve(USER_ID);
        budget.activate();

        assertEquals("ACTIVE", budget.getStatus(), "Status should be ACTIVE");
    }

    @Test
    @DisplayName("Should reject activation of non-approved budget")
    void testActivateNonApprovedBudget() {
        assertThrows(IllegalStateException.class, () -> {
            budget.activate();
        }, "Should throw exception when activating non-approved budget");
    }

    @Test
    @DisplayName("Should handle budget category")
    void testSetBudgetCategory() {
        budget.setBudgetCategory("MARKETING");
        assertEquals("MARKETING", budget.getBudgetCategory(), "Budget category should be set");
    }

    @Test
    @DisplayName("Should handle country")
    void testSetCountry() {
        budget.setCountry("US");
        assertEquals("US", budget.getCountry(), "Country should be set");
    }

    @Test
    @DisplayName("Should handle currency")
    void testSetCurrency() {
        budget.setCurrency("EUR");
        assertEquals("EUR", budget.getCurrency(), "Currency should be set");
    }

    @Test
    @DisplayName("Should handle department")
    void testSetDepartment() {
        budget.setDepartment("Marketing");
        assertEquals("Marketing", budget.getDepartment(), "Department should be set");
    }

    @Test
    @DisplayName("Should handle parent budget ID")
    void testSetParentBudgetId() {
        budget.setParentBudgetId("parent-budget-123");
        assertEquals("parent-budget-123", budget.getParentBudgetId(), "Parent budget ID should be set");
    }

    @Test
    @DisplayName("Should handle date range")
    void testSetDateRange() {
        Instant start = Instant.parse("2024-01-01T00:00:00Z");
        Instant end = Instant.parse("2024-12-31T23:59:59Z");

        budget.setStartDate(start);
        budget.setEndDate(end);

        assertEquals(start, budget.getStartDate(), "Start date should be set");
        assertEquals(end, budget.getEndDate(), "End date should be set");
    }

    @Test
    @DisplayName("Should create BudgetAllocation correctly")
    void testBudgetAllocationCreation() {
        Budget.BudgetAllocation allocation = new Budget.BudgetAllocation(
            "DIGITAL_MARKETING", new BigDecimal("100000"), "2024-01-01", "2024-12-31"
        );

        assertEquals("DIGITAL_MARKETING", allocation.getCategory(), "Category should match");
        assertEquals(new BigDecimal("100000"), allocation.getAmount(), "Amount should match");
        assertEquals("2024-01-01", allocation.getStartDate(), "Start date should match");
        assertEquals("2024-12-31", allocation.getEndDate(), "End date should match");
    }

    @Test
    @DisplayName("Should set allocation description")
    void testSetAllocationDescription() {
        Budget.BudgetAllocation allocation = new Budget.BudgetAllocation(
            "DIGITAL_MARKETING", new BigDecimal("100000"), "2024-01-01", "2024-12-31"
        );
        allocation.setDescription("Q1 digital marketing campaigns");

        assertEquals("Q1 digital marketing campaigns", allocation.getDescription(),
            "Description should be set");
    }

    @Test
    @DisplayName("Should create BudgetPeriod correctly")
    void testBudgetPeriodCreation() {
        Instant start = Instant.parse("2024-01-01T00:00:00Z");
        Instant end = Instant.parse("2024-03-31T23:59:59Z");

        Budget.BudgetPeriod period = new Budget.BudgetPeriod(
            "Q1-2024", new BigDecimal("250000"), start, end
        );

        assertEquals("Q1-2024", period.getPeriod(), "Period should match");
        assertEquals(new BigDecimal("250000"), period.getAmount(), "Amount should match");
        assertEquals(start, period.getStartDate(), "Start date should match");
        assertEquals(end, period.getEndDate(), "End date should match");
    }

    @Test
    @DisplayName("Should support builder pattern")
    void testBuilderPattern() {
        Budget builtBudget = Budget.builder()
            .tenantId(TENANT_ID)
            .name("Built Budget")
            .fiscalYear(FISCAL_YEAR)
            .totalAmount(new BigDecimal("500000"))
            .country("US")
            .currency("USD")
            .department("Sales")
            .status("APPROVED")
            .build();

        assertEquals("Built Budget", builtBudget.getName(), "Name should match builder value");
        assertEquals("US", builtBudget.getCountry(), "Country should match builder value");
        assertEquals("APPROVED", builtBudget.getStatus(), "Status should match builder value");
    }

    @Test
    @DisplayName("Should handle different statuses")
    void testDifferentStatuses() {
        String[] statuses = {"DRAFT", "APPROVED", "ACTIVE", "SUSPENDED", "CLOSED", "CANCELLED"};

        for (String status : statuses) {
            Budget b = new Budget(TENANT_ID, "Test", FISCAL_YEAR, TOTAL_AMOUNT);
            b.setStatus(status);
            assertEquals(status, b.getStatus(), "Status should be " + status);
        }
    }
}
