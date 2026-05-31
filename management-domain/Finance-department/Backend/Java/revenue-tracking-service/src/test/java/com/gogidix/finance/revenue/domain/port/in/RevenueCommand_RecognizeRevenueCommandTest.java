package com.gogidix.finance.revenue.domain.port.in;

import com.gogidix.finance.revenue.domain.port.in.RevenueCommand;
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
        dto.setRecognizedBy("val-recognizedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-revenueId", dto.getRevenueId());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-recognizedBy", dto.getRecognizedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueCommand.RecognizeRevenueCommand dto1 = new RevenueCommand.RecognizeRevenueCommand();
        RevenueCommand.RecognizeRevenueCommand dto2 = new RevenueCommand.RecognizeRevenueCommand();
        dto1.setTenantId("test");
        dto1.setRevenueId("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setRecognizedBy("test");
        dto2.setTenantId("test");
        dto2.setRevenueId("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setRecognizedBy("test");
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
        dto.setRecognizedBy("test");
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
        dto.setRecognizedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}