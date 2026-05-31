package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.interfaces.rest.TaxFilingController;
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
class TaxFilingController_PaymentRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingController.PaymentRequestDto dto = new TaxFilingController.PaymentRequestDto();
        dto.setPaymentReference("val-paymentReference");
        assertEquals("val-paymentReference", dto.getPaymentReference());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingController.PaymentRequestDto dto1 = new TaxFilingController.PaymentRequestDto();
        TaxFilingController.PaymentRequestDto dto2 = new TaxFilingController.PaymentRequestDto();
        dto1.setPaymentReference("test");
        dto2.setPaymentReference("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setPaymentReference(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingController.PaymentRequestDto dto = new TaxFilingController.PaymentRequestDto();
        dto.setPaymentReference("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingController.PaymentRequestDto dto = new TaxFilingController.PaymentRequestDto();
        dto.setPaymentReference("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}