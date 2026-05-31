package com.gogidix.finance.cashflow.domain.model;

import com.gogidix.finance.cashflow.domain.model.CashflowStatement;
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
class CashflowStatement_CashflowSectionTest {

        @Test
    void testBuilder() {
        CashflowStatement.CashflowSection dto = CashflowStatement.CashflowSection.builder()
                        .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .items(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getTotalInflow());
        assertEquals(BigDecimal.TEN, dto.getTotalOutflow());
        assertEquals(BigDecimal.TEN, dto.getNetCashflow());
    }

    @Test
    void testSettersAndGetters() {
        CashflowStatement.CashflowSection dto = new CashflowStatement.CashflowSection();
        dto.setTotalInflow(BigDecimal.ONE);
        dto.setTotalOutflow(BigDecimal.ONE);
        dto.setNetCashflow(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getTotalInflow());
        assertEquals(BigDecimal.ONE, dto.getTotalOutflow());
        assertEquals(BigDecimal.ONE, dto.getNetCashflow());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowStatement.CashflowSection dto1 = CashflowStatement.CashflowSection.builder()
                        .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .items(Collections.emptyList())
            .build();
        CashflowStatement.CashflowSection dto2 = CashflowStatement.CashflowSection.builder()
                        .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .items(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CashflowStatement.CashflowSection dto = CashflowStatement.CashflowSection.builder()
                        .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .items(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}