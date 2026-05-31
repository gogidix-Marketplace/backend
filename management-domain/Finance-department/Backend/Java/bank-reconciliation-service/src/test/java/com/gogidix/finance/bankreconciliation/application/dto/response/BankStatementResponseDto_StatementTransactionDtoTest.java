package com.gogidix.finance.bankreconciliation.application.dto.response;

import com.gogidix.finance.bankreconciliation.application.dto.response.BankStatementResponseDto;
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
class BankStatementResponseDto_StatementTransactionDtoTest {

        @Test
    void testBuilder() {
        BankStatementResponseDto.StatementTransactionDto dto = BankStatementResponseDto.StatementTransactionDto.builder()
                        .transactionId("test-transactionId")
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .reference("test-reference")
            .amount(BigDecimal.TEN)
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
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-category", dto.getCategory());
        assertTrue(dto.getIsReconciled());
        assertEquals("test-reconciliationLineId", dto.getReconciliationLineId());
    }

    @Test
    void testSettersAndGetters() {
        BankStatementResponseDto.StatementTransactionDto dto = new BankStatementResponseDto.StatementTransactionDto();
        dto.setTransactionId("val-transactionId");
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setReference("val-reference");
        dto.setAmount(BigDecimal.ONE);
        dto.setCategory("val-category");
        dto.setIsReconciled(true);
        dto.setReconciliationLineId("val-reconciliationLineId");
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-reference", dto.getReference());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-category", dto.getCategory());
        assertTrue(dto.getIsReconciled());
        assertEquals("val-reconciliationLineId", dto.getReconciliationLineId());
    }

    @Test
    void testEqualsAndHashCode() {
        BankStatementResponseDto.StatementTransactionDto dto1 = BankStatementResponseDto.StatementTransactionDto.builder()
                        .transactionId("test-transactionId")
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .reference("test-reference")
            .amount(BigDecimal.TEN)
            .transactionType(null)
            .category("test-category")
            .isReconciled(true)
            .reconciliationLineId("test-reconciliationLineId")
            .build();
        BankStatementResponseDto.StatementTransactionDto dto2 = BankStatementResponseDto.StatementTransactionDto.builder()
                        .transactionId("test-transactionId")
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .reference("test-reference")
            .amount(BigDecimal.TEN)
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
        BankStatementResponseDto.StatementTransactionDto dto = BankStatementResponseDto.StatementTransactionDto.builder()
                        .transactionId("test-transactionId")
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .reference("test-reference")
            .amount(BigDecimal.TEN)
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