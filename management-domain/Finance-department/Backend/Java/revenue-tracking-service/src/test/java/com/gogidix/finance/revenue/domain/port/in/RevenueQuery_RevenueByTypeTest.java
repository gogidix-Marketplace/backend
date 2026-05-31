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
class RevenueQuery_RevenueByTypeTest {

        @Test
    void testSettersAndGetters() {
        RevenueQuery.RevenueByType dto = new RevenueQuery.RevenueByType();
        dto.setType("val-type");
        dto.setTotalAmount(BigDecimal.ONE);
        dto.setRecognizedAmount(BigDecimal.ONE);
        assertEquals("val-type", dto.getType());
        assertEquals(BigDecimal.ONE, dto.getTotalAmount());
        assertEquals(BigDecimal.ONE, dto.getRecognizedAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueQuery.RevenueByType dto1 = new RevenueQuery.RevenueByType();
        RevenueQuery.RevenueByType dto2 = new RevenueQuery.RevenueByType();
        dto1.setType("test");
        dto1.setTotalAmount(BigDecimal.TEN);
        dto1.setRecognizedAmount(BigDecimal.TEN);
        dto1.setCount(42L);
        dto2.setType("test");
        dto2.setTotalAmount(BigDecimal.TEN);
        dto2.setRecognizedAmount(BigDecimal.TEN);
        dto2.setCount(42L);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setType(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueQuery.RevenueByType dto = new RevenueQuery.RevenueByType();
        dto.setType("test");
        dto.setTotalAmount(BigDecimal.TEN);
        dto.setRecognizedAmount(BigDecimal.TEN);
        dto.setCount(42L);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueQuery.RevenueByType dto = new RevenueQuery.RevenueByType();
        dto.setType("test");
        dto.setTotalAmount(BigDecimal.TEN);
        dto.setRecognizedAmount(BigDecimal.TEN);
        dto.setCount(42L);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}