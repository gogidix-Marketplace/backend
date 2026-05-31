package com.gogidix.finance.revenue.domain.port.in;

import com.gogidix.finance.revenue.domain.port.in.RevenueQuery;
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
class RevenueQuery_RevenueByPeriodTest {

        @Test
    void testSettersAndGetters() {
        RevenueQuery.RevenueByPeriod dto = new RevenueQuery.RevenueByPeriod();
        dto.setPeriod("val-period");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setAmount(BigDecimal.ONE);
        dto.setRecognizedAmount(BigDecimal.ONE);
        dto.setDeferredAmount(BigDecimal.ONE);
        assertEquals("val-period", dto.getPeriod());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(BigDecimal.ONE, dto.getRecognizedAmount());
        assertEquals(BigDecimal.ONE, dto.getDeferredAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueQuery.RevenueByPeriod dto1 = new RevenueQuery.RevenueByPeriod();
        RevenueQuery.RevenueByPeriod dto2 = new RevenueQuery.RevenueByPeriod();
        dto1.setPeriod("test");
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setAmount(BigDecimal.TEN);
        dto1.setRecognizedAmount(BigDecimal.TEN);
        dto1.setDeferredAmount(BigDecimal.TEN);
        dto2.setPeriod("test");
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setAmount(BigDecimal.TEN);
        dto2.setRecognizedAmount(BigDecimal.TEN);
        dto2.setDeferredAmount(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setPeriod(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueQuery.RevenueByPeriod dto = new RevenueQuery.RevenueByPeriod();
        dto.setPeriod("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setAmount(BigDecimal.TEN);
        dto.setRecognizedAmount(BigDecimal.TEN);
        dto.setDeferredAmount(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueQuery.RevenueByPeriod dto = new RevenueQuery.RevenueByPeriod();
        dto.setPeriod("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setAmount(BigDecimal.TEN);
        dto.setRecognizedAmount(BigDecimal.TEN);
        dto.setDeferredAmount(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}