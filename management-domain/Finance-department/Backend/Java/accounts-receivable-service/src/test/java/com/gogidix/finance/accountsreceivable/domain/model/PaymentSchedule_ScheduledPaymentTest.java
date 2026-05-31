package com.gogidix.finance.accountsreceivable.domain.model;

import com.gogidix.finance.accountsreceivable.domain.model.PaymentSchedule;
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
class PaymentSchedule_ScheduledPaymentTest {

        @Test
    void testBuilder() {
        PaymentSchedule.ScheduledPayment dto = PaymentSchedule.ScheduledPayment.builder()
                        .scheduledPaymentId("test-scheduledPaymentId")
            .installmentNumber(42)
            .amount(BigDecimal.TEN)
            .dueDate(LocalDate.of(2025,1,15))
            .status(PaymentSchedule.ScheduledPayment.PaymentStatus.PENDING)
            .paidAt(Instant.parse("2025-01-15T10:00:00Z"))
            .transactionId("test-transactionId")
            .paymentMethod("test-paymentMethod")
            .paidAmount(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .paidDate(LocalDate.of(2025,1,15))
            .failureReason("test-failureReason")
            .attemptCount(42)
            .nextAttemptDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-scheduledPaymentId", dto.getScheduledPaymentId());
        assertEquals(42, dto.getInstallmentNumber());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(LocalDate.of(2025,1,15), dto.getDueDate());
        assertEquals(PaymentSchedule.ScheduledPayment.PaymentStatus.PENDING, dto.getStatus());
        assertEquals("test-transactionId", dto.getTransactionId());
        assertEquals("test-paymentMethod", dto.getPaymentMethod());
        assertEquals(BigDecimal.TEN, dto.getPaidAmount());
        assertEquals(BigDecimal.TEN, dto.getRemainingAmount());
        assertEquals(LocalDate.of(2025,1,15), dto.getPaidDate());
        assertEquals("test-failureReason", dto.getFailureReason());
        assertEquals(42, dto.getAttemptCount());
        assertEquals(LocalDate.of(2025,1,15), dto.getNextAttemptDate());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        PaymentSchedule.ScheduledPayment dto = new PaymentSchedule.ScheduledPayment();
        dto.setScheduledPaymentId("val-scheduledPaymentId");
        dto.setInstallmentNumber(99);
        dto.setAmount(BigDecimal.ONE);
        dto.setDueDate(LocalDate.of(2025,6,1));
        dto.setStatus(PaymentSchedule.ScheduledPayment.PaymentStatus.PENDING);
        dto.setTransactionId("val-transactionId");
        dto.setPaymentMethod("val-paymentMethod");
        dto.setPaidAmount(BigDecimal.ONE);
        dto.setRemainingAmount(BigDecimal.ONE);
        dto.setPaidDate(LocalDate.of(2025,6,1));
        dto.setFailureReason("val-failureReason");
        dto.setAttemptCount(99);
        dto.setNextAttemptDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        assertEquals("val-scheduledPaymentId", dto.getScheduledPaymentId());
        assertEquals(99, dto.getInstallmentNumber());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueDate());
        assertEquals(PaymentSchedule.ScheduledPayment.PaymentStatus.PENDING, dto.getStatus());
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals("val-paymentMethod", dto.getPaymentMethod());
        assertEquals(BigDecimal.ONE, dto.getPaidAmount());
        assertEquals(BigDecimal.ONE, dto.getRemainingAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getPaidDate());
        assertEquals("val-failureReason", dto.getFailureReason());
        assertEquals(99, dto.getAttemptCount());
        assertEquals(LocalDate.of(2025,6,1), dto.getNextAttemptDate());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentSchedule.ScheduledPayment dto1 = PaymentSchedule.ScheduledPayment.builder()
                        .scheduledPaymentId("test-scheduledPaymentId")
            .installmentNumber(42)
            .amount(BigDecimal.TEN)
            .dueDate(LocalDate.of(2025,1,15))
            .status(PaymentSchedule.ScheduledPayment.PaymentStatus.PENDING)
            .paidAt(Instant.parse("2025-01-15T10:00:00Z"))
            .transactionId("test-transactionId")
            .paymentMethod("test-paymentMethod")
            .paidAmount(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .paidDate(LocalDate.of(2025,1,15))
            .failureReason("test-failureReason")
            .attemptCount(42)
            .nextAttemptDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        PaymentSchedule.ScheduledPayment dto2 = PaymentSchedule.ScheduledPayment.builder()
                        .scheduledPaymentId("test-scheduledPaymentId")
            .installmentNumber(42)
            .amount(BigDecimal.TEN)
            .dueDate(LocalDate.of(2025,1,15))
            .status(PaymentSchedule.ScheduledPayment.PaymentStatus.PENDING)
            .paidAt(Instant.parse("2025-01-15T10:00:00Z"))
            .transactionId("test-transactionId")
            .paymentMethod("test-paymentMethod")
            .paidAmount(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .paidDate(LocalDate.of(2025,1,15))
            .failureReason("test-failureReason")
            .attemptCount(42)
            .nextAttemptDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PaymentSchedule.ScheduledPayment dto = PaymentSchedule.ScheduledPayment.builder()
                        .scheduledPaymentId("test-scheduledPaymentId")
            .installmentNumber(42)
            .amount(BigDecimal.TEN)
            .dueDate(LocalDate.of(2025,1,15))
            .status(PaymentSchedule.ScheduledPayment.PaymentStatus.PENDING)
            .paidAt(Instant.parse("2025-01-15T10:00:00Z"))
            .transactionId("test-transactionId")
            .paymentMethod("test-paymentMethod")
            .paidAmount(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .paidDate(LocalDate.of(2025,1,15))
            .failureReason("test-failureReason")
            .attemptCount(42)
            .nextAttemptDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}