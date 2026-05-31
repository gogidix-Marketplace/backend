package com.gogidix.sales.revenue.domain.port.in;

import com.gogidix.sales.revenue.domain.port.in.RevenueCommand;
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
class RevenueCommand_RecognizeRevenueCommandTest {

        @Test
    void testSettersAndGetters() {
        RevenueCommand.RecognizeRevenueCommand dto = new RevenueCommand.RecognizeRevenueCommand();
        dto.setTenantId("val-tenantId");
        dto.setRevenueId("val-revenueId");
        dto.setAmount(BigDecimal.ONE);
        dto.setRecognitionDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-revenueId", dto.getRevenueId());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getRecognitionDate());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueCommand.RecognizeRevenueCommand dto1 = new RevenueCommand.RecognizeRevenueCommand();
        RevenueCommand.RecognizeRevenueCommand dto2 = new RevenueCommand.RecognizeRevenueCommand();
        dto1.setTenantId("test");
        dto1.setRevenueId("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setRecognitionDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setRevenueId("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setRecognitionDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueCommand.RecognizeRevenueCommand dto = new RevenueCommand.RecognizeRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setRecognitionDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueCommand.RecognizeRevenueCommand dto = new RevenueCommand.RecognizeRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setRecognitionDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}