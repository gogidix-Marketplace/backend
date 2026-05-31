package com.gogidix.finance.accountsreceivable.application.dto.response;

import com.gogidix.finance.accountsreceivable.application.dto.response.InvoiceResponseDto;
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
class InvoiceResponseDto_PaymentDtoTest {

        @Test
    void testBuilder() {
        InvoiceResponseDto.PaymentDto dto = InvoiceResponseDto.PaymentDto.builder()
                        .paymentId("test-paymentId")
            .transactionId("test-transactionId")
            .amount(BigDecimal.TEN)
            .paidAt(Instant.parse("2025-01-15T10:00:00Z"))
            .paymentMethod("test-paymentMethod")
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-paymentId", dto.getPaymentId());
        assertEquals("test-transactionId", dto.getTransactionId());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-paymentMethod", dto.getPaymentMethod());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        InvoiceResponseDto.PaymentDto dto = new InvoiceResponseDto.PaymentDto();
        dto.setPaymentId("val-paymentId");
        dto.setTransactionId("val-transactionId");
        dto.setAmount(BigDecimal.ONE);
        dto.setPaymentMethod("val-paymentMethod");
        dto.setNotes("val-notes");
        assertEquals("val-paymentId", dto.getPaymentId());
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-paymentMethod", dto.getPaymentMethod());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceResponseDto.PaymentDto dto1 = InvoiceResponseDto.PaymentDto.builder()
                        .paymentId("test-paymentId")
            .transactionId("test-transactionId")
            .amount(BigDecimal.TEN)
            .paidAt(Instant.parse("2025-01-15T10:00:00Z"))
            .paymentMethod("test-paymentMethod")
            .notes("test-notes")
            .build();
        InvoiceResponseDto.PaymentDto dto2 = InvoiceResponseDto.PaymentDto.builder()
                        .paymentId("test-paymentId")
            .transactionId("test-transactionId")
            .amount(BigDecimal.TEN)
            .paidAt(Instant.parse("2025-01-15T10:00:00Z"))
            .paymentMethod("test-paymentMethod")
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        InvoiceResponseDto.PaymentDto dto = InvoiceResponseDto.PaymentDto.builder()
                        .paymentId("test-paymentId")
            .transactionId("test-transactionId")
            .amount(BigDecimal.TEN)
            .paidAt(Instant.parse("2025-01-15T10:00:00Z"))
            .paymentMethod("test-paymentMethod")
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}