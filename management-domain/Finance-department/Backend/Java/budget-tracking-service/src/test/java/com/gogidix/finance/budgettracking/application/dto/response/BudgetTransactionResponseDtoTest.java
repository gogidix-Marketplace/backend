package com.gogidix.finance.budgettracking.application.dto.response;

import com.gogidix.finance.budgettracking.application.dto.response.BudgetTransactionResponseDto;
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
class BudgetTransactionResponseDtoTest {

        @Test
    void testBuilder() {
        BudgetTransactionResponseDto dto = BudgetTransactionResponseDto.builder()
                        .id("test-id")
            .transactionId("test-transactionId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .referenceType("test-referenceType")
            .referenceId("test-referenceId")
            .transactionType(BudgetTransactionResponseDto.TransactionTypeDto.ALLOCATION)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .description("test-description")
            .status(BudgetTransactionResponseDto.TransactionStatusDto.PENDING)
            .transactionDate(LocalDate.of(2025,1,15))
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .projectId("test-projectId")
            .recordedBy("test-recordedBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .relatedBudgetPeriod("test-relatedBudgetPeriod")
            .balanceBefore(BigDecimal.TEN)
            .balanceAfter(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .correlationId("test-correlationId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-transactionId", dto.getTransactionId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-budgetId", dto.getBudgetId());
        assertEquals("test-budgetCode", dto.getBudgetCode());
        assertEquals("test-referenceType", dto.getReferenceType());
        assertEquals("test-referenceId", dto.getReferenceId());
        assertEquals(BudgetTransactionResponseDto.TransactionTypeDto.ALLOCATION, dto.getTransactionType());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BudgetTransactionResponseDto.TransactionStatusDto.PENDING, dto.getStatus());
        assertEquals(LocalDate.of(2025,1,15), dto.getTransactionDate());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-costCenter", dto.getCostCenter());
        assertEquals("test-projectId", dto.getProjectId());
        assertEquals("test-recordedBy", dto.getRecordedBy());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals("test-rejectionReason", dto.getRejectionReason());
        assertEquals("test-relatedBudgetPeriod", dto.getRelatedBudgetPeriod());
        assertEquals(BigDecimal.TEN, dto.getBalanceBefore());
        assertEquals(BigDecimal.TEN, dto.getBalanceAfter());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-correlationId", dto.getCorrelationId());
    }

    @Test
    void testSettersAndGetters() {
        BudgetTransactionResponseDto dto = new BudgetTransactionResponseDto();
        dto.setId("val-id");
        dto.setTransactionId("val-transactionId");
        dto.setTenantId("val-tenantId");
        dto.setBudgetId("val-budgetId");
        dto.setBudgetCode("val-budgetCode");
        dto.setReferenceType("val-referenceType");
        dto.setReferenceId("val-referenceId");
        dto.setTransactionType(BudgetTransactionResponseDto.TransactionTypeDto.ALLOCATION);
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setDescription("val-description");
        dto.setStatus(BudgetTransactionResponseDto.TransactionStatusDto.PENDING);
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setCategory("val-category");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setProjectId("val-projectId");
        dto.setRecordedBy("val-recordedBy");
        dto.setApprovedBy("val-approvedBy");
        dto.setRejectionReason("val-rejectionReason");
        dto.setRelatedBudgetPeriod("val-relatedBudgetPeriod");
        dto.setBalanceBefore(BigDecimal.ONE);
        dto.setBalanceAfter(BigDecimal.ONE);
        dto.setNotes("val-notes");
        dto.setCorrelationId("val-correlationId");
        assertEquals("val-id", dto.getId());
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-budgetId", dto.getBudgetId());
        assertEquals("val-budgetCode", dto.getBudgetCode());
        assertEquals("val-referenceType", dto.getReferenceType());
        assertEquals("val-referenceId", dto.getReferenceId());
        assertEquals(BudgetTransactionResponseDto.TransactionTypeDto.ALLOCATION, dto.getTransactionType());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BudgetTransactionResponseDto.TransactionStatusDto.PENDING, dto.getStatus());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-projectId", dto.getProjectId());
        assertEquals("val-recordedBy", dto.getRecordedBy());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals("val-rejectionReason", dto.getRejectionReason());
        assertEquals("val-relatedBudgetPeriod", dto.getRelatedBudgetPeriod());
        assertEquals(BigDecimal.ONE, dto.getBalanceBefore());
        assertEquals(BigDecimal.ONE, dto.getBalanceAfter());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-correlationId", dto.getCorrelationId());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTransactionResponseDto dto1 = BudgetTransactionResponseDto.builder()
                        .id("test-id")
            .transactionId("test-transactionId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .referenceType("test-referenceType")
            .referenceId("test-referenceId")
            .transactionType(BudgetTransactionResponseDto.TransactionTypeDto.ALLOCATION)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .description("test-description")
            .status(BudgetTransactionResponseDto.TransactionStatusDto.PENDING)
            .transactionDate(LocalDate.of(2025,1,15))
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .projectId("test-projectId")
            .recordedBy("test-recordedBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .relatedBudgetPeriod("test-relatedBudgetPeriod")
            .balanceBefore(BigDecimal.TEN)
            .balanceAfter(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .correlationId("test-correlationId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        BudgetTransactionResponseDto dto2 = BudgetTransactionResponseDto.builder()
                        .id("test-id")
            .transactionId("test-transactionId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .referenceType("test-referenceType")
            .referenceId("test-referenceId")
            .transactionType(BudgetTransactionResponseDto.TransactionTypeDto.ALLOCATION)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .description("test-description")
            .status(BudgetTransactionResponseDto.TransactionStatusDto.PENDING)
            .transactionDate(LocalDate.of(2025,1,15))
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .projectId("test-projectId")
            .recordedBy("test-recordedBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .relatedBudgetPeriod("test-relatedBudgetPeriod")
            .balanceBefore(BigDecimal.TEN)
            .balanceAfter(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .correlationId("test-correlationId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BudgetTransactionResponseDto dto = BudgetTransactionResponseDto.builder()
                        .id("test-id")
            .transactionId("test-transactionId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .referenceType("test-referenceType")
            .referenceId("test-referenceId")
            .transactionType(BudgetTransactionResponseDto.TransactionTypeDto.ALLOCATION)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .description("test-description")
            .status(BudgetTransactionResponseDto.TransactionStatusDto.PENDING)
            .transactionDate(LocalDate.of(2025,1,15))
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .projectId("test-projectId")
            .recordedBy("test-recordedBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .relatedBudgetPeriod("test-relatedBudgetPeriod")
            .balanceBefore(BigDecimal.TEN)
            .balanceAfter(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .correlationId("test-correlationId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}