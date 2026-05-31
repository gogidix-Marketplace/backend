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
class CurrencyConversionCommand_ConvertAmountCommandTest {

        @Test
    void testSettersAndGetters() {
        CurrencyConversionCommand.ConvertAmountCommand dto = new CurrencyConversionCommand.ConvertAmountCommand();
        dto.setTenantId("val-tenantId");
        dto.setRequestedBy("val-requestedBy");
        dto.setAmount(BigDecimal.ONE);
        dto.setFromCurrency("val-fromCurrency");
        dto.setToCurrency("val-toCurrency");
        dto.setCorrelationId("val-correlationId");
        dto.setProvider("val-provider");
        dto.setApplyFee(true);
        dto.setFeePercentage(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requestedBy", dto.getRequestedBy());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-fromCurrency", dto.getFromCurrency());
        assertEquals("val-toCurrency", dto.getToCurrency());
        assertEquals("val-correlationId", dto.getCorrelationId());
        assertEquals("val-provider", dto.getProvider());
        assertTrue(dto.getApplyFee());
        assertEquals(BigDecimal.ONE, dto.getFeePercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        CurrencyConversionCommand.ConvertAmountCommand dto1 = new CurrencyConversionCommand.ConvertAmountCommand();
        CurrencyConversionCommand.ConvertAmountCommand dto2 = new CurrencyConversionCommand.ConvertAmountCommand();
        dto1.setTenantId("test");
        dto1.setRequestedBy("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setFromCurrency("test");
        dto1.setToCurrency("test");
        dto1.setCorrelationId("test");
        dto1.setProvider("test");
        dto1.setApplyFee(true);
        dto1.setFeePercentage(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setRequestedBy("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setFromCurrency("test");
        dto2.setToCurrency("test");
        dto2.setCorrelationId("test");
        dto2.setProvider("test");
        dto2.setApplyFee(true);
        dto2.setFeePercentage(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CurrencyConversionCommand.ConvertAmountCommand dto = new CurrencyConversionCommand.ConvertAmountCommand();
        dto.setTenantId("test");
        dto.setRequestedBy("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        dto.setCorrelationId("test");
        dto.setProvider("test");
        dto.setApplyFee(true);
        dto.setFeePercentage(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CurrencyConversionCommand.ConvertAmountCommand dto = new CurrencyConversionCommand.ConvertAmountCommand();
        dto.setTenantId("test");
        dto.setRequestedBy("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        dto.setCorrelationId("test");
        dto.setProvider("test");
        dto.setApplyFee(true);
        dto.setFeePercentage(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}