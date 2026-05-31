package com.gogidix.sales.revenue.domain.model;

import com.gogidix.sales.revenue.domain.model.Invoice;
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
class Invoice_InvoicePaymentTest {

        @Test
    void testBuilder() {
        Invoice.InvoicePayment dto = Invoice.InvoicePayment.builder()
                        .paymentId("test-paymentId")
            .paymentDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .paymentMethod("test-paymentMethod")
            .reference("test-reference")
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-paymentId", dto.getPaymentId());
        assertEquals(LocalDate.of(2025,1,15), dto.getPaymentDate());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-paymentMethod", dto.getPaymentMethod());
        assertEquals("test-reference", dto.getReference());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        Invoice.InvoicePayment dto = new Invoice.InvoicePayment();
        dto.setPaymentId("val-paymentId");
        dto.setPaymentDate(LocalDate.of(2025,6,1));
        dto.setAmount(BigDecimal.ONE);
        dto.setPaymentMethod("val-paymentMethod");
        dto.setReference("val-reference");
        dto.setNotes("val-notes");
        assertEquals("val-paymentId", dto.getPaymentId());
        assertEquals(LocalDate.of(2025,6,1), dto.getPaymentDate());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-paymentMethod", dto.getPaymentMethod());
        assertEquals("val-reference", dto.getReference());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        Invoice.InvoicePayment dto1 = Invoice.InvoicePayment.builder()
                        .paymentId("test-paymentId")
            .paymentDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .paymentMethod("test-paymentMethod")
            .reference("test-reference")
            .notes("test-notes")
            .build();
        Invoice.InvoicePayment dto2 = Invoice.InvoicePayment.builder()
                        .paymentId("test-paymentId")
            .paymentDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .paymentMethod("test-paymentMethod")
            .reference("test-reference")
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Invoice.InvoicePayment dto = Invoice.InvoicePayment.builder()
                        .paymentId("test-paymentId")
            .paymentDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .paymentMethod("test-paymentMethod")
            .reference("test-reference")
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}