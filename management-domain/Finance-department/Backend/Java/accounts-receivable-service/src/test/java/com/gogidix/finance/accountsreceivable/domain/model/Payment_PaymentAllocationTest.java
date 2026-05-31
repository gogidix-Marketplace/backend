package com.gogidix.finance.accountsreceivable.domain.model;

import com.gogidix.finance.accountsreceivable.domain.model.Payment;
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
class Payment_PaymentAllocationTest {

        @Test
    void testBuilder() {
        Payment.PaymentAllocation dto = Payment.PaymentAllocation.builder()
                        .allocationId("test-allocationId")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .allocatedAmount(BigDecimal.TEN)
            .allocationDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-allocationId", dto.getAllocationId());
        assertEquals("test-invoiceId", dto.getInvoiceId());
        assertEquals("test-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(BigDecimal.TEN, dto.getAllocatedAmount());
        assertEquals(LocalDate.of(2025,1,15), dto.getAllocationDate());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        Payment.PaymentAllocation dto = new Payment.PaymentAllocation();
        dto.setAllocationId("val-allocationId");
        dto.setInvoiceId("val-invoiceId");
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setAllocatedAmount(BigDecimal.ONE);
        dto.setAllocationDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        assertEquals("val-allocationId", dto.getAllocationId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(BigDecimal.ONE, dto.getAllocatedAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getAllocationDate());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        Payment.PaymentAllocation dto1 = Payment.PaymentAllocation.builder()
                        .allocationId("test-allocationId")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .allocatedAmount(BigDecimal.TEN)
            .allocationDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        Payment.PaymentAllocation dto2 = Payment.PaymentAllocation.builder()
                        .allocationId("test-allocationId")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .allocatedAmount(BigDecimal.TEN)
            .allocationDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Payment.PaymentAllocation dto = Payment.PaymentAllocation.builder()
                        .allocationId("test-allocationId")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .allocatedAmount(BigDecimal.TEN)
            .allocationDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}