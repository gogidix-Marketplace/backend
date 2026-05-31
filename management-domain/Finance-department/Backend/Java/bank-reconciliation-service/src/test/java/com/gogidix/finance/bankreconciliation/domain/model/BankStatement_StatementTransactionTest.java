package com.gogidix.finance.bankreconciliation.domain.model;

import com.gogidix.finance.bankreconciliation.domain.model.BankStatement;
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
class BankStatement_StatementTransactionTest {

        @Test
    void testBuilder() {
        BankStatement.StatementTransaction dto = BankStatement.StatementTransaction.builder()
                        .transactionId("test-transactionId")
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .reference("test-reference")
            .amount(null)
            .transactionType(null)
            .category("test-category")
            .isReconciled(true)
            .reconciliationLineId("test-reconciliationLineId")
            .build();
        assertNotNull(dto);
        assertEquals("test-transactionId", dto.getTransactionId());
        assertEquals(LocalDate.of(2025,1,15), dto.getTransactionDate());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-reference", dto.getReference());
        assertEquals("test-category", dto.getCategory());
        assertTrue(dto.getIsReconciled());
        assertEquals("test-reconciliationLineId", dto.getReconciliationLineId());
    }

    @Test
    void testSettersAndGetters() {
        BankStatement.StatementTransaction dto = new BankStatement.StatementTransaction();
        dto.setTransactionId("val-transactionId");
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setReference("val-reference");
        dto.setCategory("val-category");
        dto.setIsReconciled(true);
        dto.setReconciliationLineId("val-reconciliationLineId");
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-reference", dto.getReference());
        assertEquals("val-category", dto.getCategory());
        assertTrue(dto.getIsReconciled());
        assertEquals("val-reconciliationLineId", dto.getReconciliationLineId());
    }

    @Test
    void testEqualsAndHashCode() {
        BankStatement.StatementTransaction dto1 = BankStatement.StatementTransaction.builder()
                        .transactionId("test-transactionId")
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .reference("test-reference")
            .amount(null)
            .transactionType(null)
            .category("test-category")
            .isReconciled(true)
            .reconciliationLineId("test-reconciliationLineId")
            .build();
        BankStatement.StatementTransaction dto2 = BankStatement.StatementTransaction.builder()
                        .transactionId("test-transactionId")
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .reference("test-reference")
            .amount(null)
            .transactionType(null)
            .category("test-category")
            .isReconciled(true)
            .reconciliationLineId("test-reconciliationLineId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BankStatement.StatementTransaction dto = BankStatement.StatementTransaction.builder()
                        .transactionId("test-transactionId")
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .reference("test-reference")
            .amount(null)
            .transactionType(null)
            .category("test-category")
            .isReconciled(true)
            .reconciliationLineId("test-reconciliationLineId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}