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
class RevenueQuery_RevenueSummaryTest {

        @Test
    void testSettersAndGetters() {
        RevenueQuery.RevenueSummary dto = new RevenueQuery.RevenueSummary();
        dto.setTotalRevenue(BigDecimal.ONE);
        dto.setRecognizedRevenue(BigDecimal.ONE);
        dto.setDeferredRevenue(BigDecimal.ONE);
        dto.setPendingRevenue(BigDecimal.ONE);
        dto.setMrr(BigDecimal.ONE);
        dto.setArr(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getTotalRevenue());
        assertEquals(BigDecimal.ONE, dto.getRecognizedRevenue());
        assertEquals(BigDecimal.ONE, dto.getDeferredRevenue());
        assertEquals(BigDecimal.ONE, dto.getPendingRevenue());
        assertEquals(BigDecimal.ONE, dto.getMrr());
        assertEquals(BigDecimal.ONE, dto.getArr());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueQuery.RevenueSummary dto1 = new RevenueQuery.RevenueSummary();
        RevenueQuery.RevenueSummary dto2 = new RevenueQuery.RevenueSummary();
        dto1.setTotalRevenue(BigDecimal.TEN);
        dto1.setRecognizedRevenue(BigDecimal.TEN);
        dto1.setDeferredRevenue(BigDecimal.TEN);
        dto1.setPendingRevenue(BigDecimal.TEN);
        dto1.setTotalCount(42L);
        dto1.setRecognizedCount(42L);
        dto1.setDeferredCount(42L);
        dto1.setPendingCount(42L);
        dto1.setMrr(BigDecimal.TEN);
        dto1.setArr(BigDecimal.TEN);
        dto2.setTotalRevenue(BigDecimal.TEN);
        dto2.setRecognizedRevenue(BigDecimal.TEN);
        dto2.setDeferredRevenue(BigDecimal.TEN);
        dto2.setPendingRevenue(BigDecimal.TEN);
        dto2.setTotalCount(42L);
        dto2.setRecognizedCount(42L);
        dto2.setDeferredCount(42L);
        dto2.setPendingCount(42L);
        dto2.setMrr(BigDecimal.TEN);
        dto2.setArr(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTotalRevenue(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueQuery.RevenueSummary dto = new RevenueQuery.RevenueSummary();
        dto.setTotalRevenue(BigDecimal.TEN);
        dto.setRecognizedRevenue(BigDecimal.TEN);
        dto.setDeferredRevenue(BigDecimal.TEN);
        dto.setPendingRevenue(BigDecimal.TEN);
        dto.setTotalCount(42L);
        dto.setRecognizedCount(42L);
        dto.setDeferredCount(42L);
        dto.setPendingCount(42L);
        dto.setMrr(BigDecimal.TEN);
        dto.setArr(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueQuery.RevenueSummary dto = new RevenueQuery.RevenueSummary();
        dto.setTotalRevenue(BigDecimal.TEN);
        dto.setRecognizedRevenue(BigDecimal.TEN);
        dto.setDeferredRevenue(BigDecimal.TEN);
        dto.setPendingRevenue(BigDecimal.TEN);
        dto.setTotalCount(42L);
        dto.setRecognizedCount(42L);
        dto.setDeferredCount(42L);
        dto.setPendingCount(42L);
        dto.setMrr(BigDecimal.TEN);
        dto.setArr(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}