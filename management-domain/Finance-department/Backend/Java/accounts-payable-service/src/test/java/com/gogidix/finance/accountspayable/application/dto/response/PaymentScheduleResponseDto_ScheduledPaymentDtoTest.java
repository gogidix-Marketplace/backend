package com.gogidix.finance.accountspayable.application.dto.response;

import com.gogidix.finance.accountspayable.application.dto.response.PaymentScheduleResponseDto;
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
class PaymentScheduleResponseDto_ScheduledPaymentDtoTest {

        @Test
    void testBuilder() {
        PaymentScheduleResponseDto.ScheduledPaymentDto dto = PaymentScheduleResponseDto.ScheduledPaymentDto.builder()
                        .paymentId("test-paymentId")
            .scheduledDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .status(PaymentScheduleResponseDto.ScheduledPaymentDto.PaymentStatusDto.PENDING)
            .paymentReference("test-paymentReference")
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failureReason("test-failureReason")
            .build();
        assertNotNull(dto);
        assertEquals("test-paymentId", dto.getPaymentId());
        assertEquals(LocalDate.of(2025,1,15), dto.getScheduledDate());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(PaymentScheduleResponseDto.ScheduledPaymentDto.PaymentStatusDto.PENDING, dto.getStatus());
        assertEquals("test-paymentReference", dto.getPaymentReference());
        assertEquals("test-failureReason", dto.getFailureReason());
    }

    @Test
    void testSettersAndGetters() {
        PaymentScheduleResponseDto.ScheduledPaymentDto dto = new PaymentScheduleResponseDto.ScheduledPaymentDto();
        dto.setPaymentId("val-paymentId");
        dto.setScheduledDate(LocalDate.of(2025,6,1));
        dto.setAmount(BigDecimal.ONE);
        dto.setStatus(PaymentScheduleResponseDto.ScheduledPaymentDto.PaymentStatusDto.PENDING);
        dto.setPaymentReference("val-paymentReference");
        dto.setFailureReason("val-failureReason");
        assertEquals("val-paymentId", dto.getPaymentId());
        assertEquals(LocalDate.of(2025,6,1), dto.getScheduledDate());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(PaymentScheduleResponseDto.ScheduledPaymentDto.PaymentStatusDto.PENDING, dto.getStatus());
        assertEquals("val-paymentReference", dto.getPaymentReference());
        assertEquals("val-failureReason", dto.getFailureReason());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentScheduleResponseDto.ScheduledPaymentDto dto1 = PaymentScheduleResponseDto.ScheduledPaymentDto.builder()
                        .paymentId("test-paymentId")
            .scheduledDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .status(PaymentScheduleResponseDto.ScheduledPaymentDto.PaymentStatusDto.PENDING)
            .paymentReference("test-paymentReference")
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failureReason("test-failureReason")
            .build();
        PaymentScheduleResponseDto.ScheduledPaymentDto dto2 = PaymentScheduleResponseDto.ScheduledPaymentDto.builder()
                        .paymentId("test-paymentId")
            .scheduledDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .status(PaymentScheduleResponseDto.ScheduledPaymentDto.PaymentStatusDto.PENDING)
            .paymentReference("test-paymentReference")
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failureReason("test-failureReason")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PaymentScheduleResponseDto.ScheduledPaymentDto dto = PaymentScheduleResponseDto.ScheduledPaymentDto.builder()
                        .paymentId("test-paymentId")
            .scheduledDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .status(PaymentScheduleResponseDto.ScheduledPaymentDto.PaymentStatusDto.PENDING)
            .paymentReference("test-paymentReference")
            .processedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .failureReason("test-failureReason")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}