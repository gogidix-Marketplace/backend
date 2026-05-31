package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.port.in.PaymentCommand;
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
class PaymentCommand_SetCurrencyConversionCommandTest {

        @Test
    void testSettersAndGetters() {
        PaymentCommand.SetCurrencyConversionCommand dto = new PaymentCommand.SetCurrencyConversionCommand();
        dto.setTenantId("val-tenantId");
        dto.setPaymentId("val-paymentId");
        dto.setExchangeRate("val-exchangeRate");
        dto.setOriginalCurrency("val-originalCurrency");
        dto.setOriginalAmount(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-paymentId", dto.getPaymentId());
        assertEquals("val-exchangeRate", dto.getExchangeRate());
        assertEquals("val-originalCurrency", dto.getOriginalCurrency());
        assertEquals(BigDecimal.ONE, dto.getOriginalAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentCommand.SetCurrencyConversionCommand dto1 = new PaymentCommand.SetCurrencyConversionCommand();
        PaymentCommand.SetCurrencyConversionCommand dto2 = new PaymentCommand.SetCurrencyConversionCommand();
        dto1.setTenantId("test");
        dto1.setPaymentId("test");
        dto1.setExchangeRate("test");
        dto1.setOriginalCurrency("test");
        dto1.setOriginalAmount(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setPaymentId("test");
        dto2.setExchangeRate("test");
        dto2.setOriginalCurrency("test");
        dto2.setOriginalAmount(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PaymentCommand.SetCurrencyConversionCommand dto = new PaymentCommand.SetCurrencyConversionCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setExchangeRate("test");
        dto.setOriginalCurrency("test");
        dto.setOriginalAmount(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PaymentCommand.SetCurrencyConversionCommand dto = new PaymentCommand.SetCurrencyConversionCommand();
        dto.setTenantId("test");
        dto.setPaymentId("test");
        dto.setExchangeRate("test");
        dto.setOriginalCurrency("test");
        dto.setOriginalAmount(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}