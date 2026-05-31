package com.gogidix.finance.bankreconciliation.application.dto.response;

import com.gogidix.finance.bankreconciliation.application.dto.response.ReconciliationSummaryResponseDto;
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
class ReconciliationSummaryResponseDtoTest {

        @Test
    void testBuilder() {
        ReconciliationSummaryResponseDto dto = ReconciliationSummaryResponseDto.builder()
                        .totalReconciliations(42L)
            .completedCount(42L)
            .pendingCount(42L)
            .balancedCount(42L)
            .totalDiscrepancyAmount(BigDecimal.TEN)
            .totalLines(42)
            .totalMatched(42)
            .pendingStatements(42L)
            .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalReconciliations());
        assertEquals(42L, dto.getCompletedCount());
        assertEquals(42L, dto.getPendingCount());
        assertEquals(42L, dto.getBalancedCount());
        assertEquals(BigDecimal.TEN, dto.getTotalDiscrepancyAmount());
        assertEquals(42, dto.getTotalLines());
        assertEquals(42, dto.getTotalMatched());
        assertEquals(42L, dto.getPendingStatements());
        assertEquals(LocalDate.of(2025,1,15), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,1,15), dto.getPeriodEnd());
        assertEquals("test-accountId", dto.getAccountId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
    }

    @Test
    void testSettersAndGetters() {
        ReconciliationSummaryResponseDto dto = new ReconciliationSummaryResponseDto();
        dto.setTotalDiscrepancyAmount(BigDecimal.ONE);
        dto.setTotalLines(99);
        dto.setTotalMatched(99);
        dto.setPeriodStart(LocalDate.of(2025,6,1));
        dto.setPeriodEnd(LocalDate.of(2025,6,1));
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        assertEquals(BigDecimal.ONE, dto.getTotalDiscrepancyAmount());
        assertEquals(99, dto.getTotalLines());
        assertEquals(99, dto.getTotalMatched());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEnd());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationSummaryResponseDto dto1 = ReconciliationSummaryResponseDto.builder()
                        .totalReconciliations(42L)
            .completedCount(42L)
            .pendingCount(42L)
            .balancedCount(42L)
            .totalDiscrepancyAmount(BigDecimal.TEN)
            .totalLines(42)
            .totalMatched(42)
            .pendingStatements(42L)
            .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .build();
        ReconciliationSummaryResponseDto dto2 = ReconciliationSummaryResponseDto.builder()
                        .totalReconciliations(42L)
            .completedCount(42L)
            .pendingCount(42L)
            .balancedCount(42L)
            .totalDiscrepancyAmount(BigDecimal.TEN)
            .totalLines(42)
            .totalMatched(42)
            .pendingStatements(42L)
            .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ReconciliationSummaryResponseDto dto = ReconciliationSummaryResponseDto.builder()
                        .totalReconciliations(42L)
            .completedCount(42L)
            .pendingCount(42L)
            .balancedCount(42L)
            .totalDiscrepancyAmount(BigDecimal.TEN)
            .totalLines(42)
            .totalMatched(42)
            .pendingStatements(42L)
            .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .accountId("test-accountId")
            .accountNumber("test-accountNumber")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}