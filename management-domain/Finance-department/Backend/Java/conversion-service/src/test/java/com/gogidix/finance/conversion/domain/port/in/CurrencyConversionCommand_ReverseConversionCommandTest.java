package com.gogidix.finance.conversion.domain.port.in;

import com.gogidix.finance.conversion.domain.port.in.CurrencyConversionCommand;
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
class CurrencyConversionCommand_ReverseConversionCommandTest {

        @Test
    void testSettersAndGetters() {
        CurrencyConversionCommand.ReverseConversionCommand dto = new CurrencyConversionCommand.ReverseConversionCommand();
        dto.setTenantId("val-tenantId");
        dto.setConversionId("val-conversionId");
        dto.setRequestedBy("val-requestedBy");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-conversionId", dto.getConversionId());
        assertEquals("val-requestedBy", dto.getRequestedBy());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        CurrencyConversionCommand.ReverseConversionCommand dto1 = new CurrencyConversionCommand.ReverseConversionCommand();
        CurrencyConversionCommand.ReverseConversionCommand dto2 = new CurrencyConversionCommand.ReverseConversionCommand();
        dto1.setTenantId("test");
        dto1.setConversionId("test");
        dto1.setRequestedBy("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setConversionId("test");
        dto2.setRequestedBy("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CurrencyConversionCommand.ReverseConversionCommand dto = new CurrencyConversionCommand.ReverseConversionCommand();
        dto.setTenantId("test");
        dto.setConversionId("test");
        dto.setRequestedBy("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CurrencyConversionCommand.ReverseConversionCommand dto = new CurrencyConversionCommand.ReverseConversionCommand();
        dto.setTenantId("test");
        dto.setConversionId("test");
        dto.setRequestedBy("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}