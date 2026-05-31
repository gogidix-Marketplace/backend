package com.gogidix.finance.accountsreceivable.application.dto.response;

import com.gogidix.finance.accountsreceivable.application.dto.response.PaymentScheduleResponseDto;
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
class PaymentScheduleResponseDtoTest {

        @Test
    void testBuilder() {
        PaymentScheduleResponseDto dto = PaymentScheduleResponseDto.builder()
                        .id("test-id")
            .scheduleId("test-scheduleId")
            .tenantId("test-tenantId")
            .scheduleNumber("test-scheduleNumber")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .totalAmount(BigDecimal.TEN)
            .amountPaid(BigDecimal.TEN)
            .balanceRemaining(BigDecimal.TEN)
            .currency("test-currency")
            .status(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING)
            .scheduleType(PaymentScheduleResponseDto.ScheduleTypeDto.FIXED_INSTALLMENTS)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .installmentCount(42)
            .frequency("test-frequency")
            .paymentMethod("test-paymentMethod")
            .autoCharge(true)
            .description("test-description")
            .notes("test-notes")
            .scheduledPayments(Collections.emptyList())
            .createdBy("test-createdBy")
            .lastPaymentDate(Instant.parse("2025-01-15T10:00:00Z"))
            .nextPaymentDate("test-nextPaymentDate")
            .totalInstallments(42)
            .completedInstallments(42)
            .installmentAmount(BigDecimal.TEN)
            .templateId("test-templateId")
            .originalScheduleId("test-originalScheduleId")
            .isRescheduled(true)
            .rescheduleReason("test-rescheduleReason")
            .rescheduleDate(LocalDate.of(2025,1,15))
            .tags(Collections.emptyList())
            .projectId("test-projectId")
            .departmentId("test-departmentId")
            .bankAccountId("test-bankAccountId")
            .paymentGatewayCustomerId("test-paymentGatewayCustomerId")
            .paymentGatewaySubscriptionId("test-paymentGatewaySubscriptionId")
            .sendReminder(true)
            .reminderDaysBefore(42)
            .prorateFirstInstallment(true)
            .prorationAmount(BigDecimal.TEN)
            .agreedBy("test-agreedBy")
            .agreedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .agreementReference("test-agreementReference")
            .contractId("test-contractId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-scheduleId", dto.getScheduleId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-scheduleNumber", dto.getScheduleNumber());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-invoiceId", dto.getInvoiceId());
        assertEquals("test-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(BigDecimal.TEN, dto.getTotalAmount());
        assertEquals(BigDecimal.TEN, dto.getAmountPaid());
        assertEquals(BigDecimal.TEN, dto.getBalanceRemaining());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING, dto.getStatus());
        assertEquals(PaymentScheduleResponseDto.ScheduleTypeDto.FIXED_INSTALLMENTS, dto.getScheduleType());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals(42, dto.getInstallmentCount());
        assertEquals("test-frequency", dto.getFrequency());
        assertEquals("test-paymentMethod", dto.getPaymentMethod());
        assertTrue(dto.getAutoCharge());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-nextPaymentDate", dto.getNextPaymentDate());
        assertEquals(42, dto.getTotalInstallments());
        assertEquals(42, dto.getCompletedInstallments());
        assertEquals(BigDecimal.TEN, dto.getInstallmentAmount());
        assertEquals("test-templateId", dto.getTemplateId());
        assertEquals("test-originalScheduleId", dto.getOriginalScheduleId());
        assertTrue(dto.getIsRescheduled());
        assertEquals("test-rescheduleReason", dto.getRescheduleReason());
        assertEquals(LocalDate.of(2025,1,15), dto.getRescheduleDate());
        assertEquals("test-projectId", dto.getProjectId());
        assertEquals("test-departmentId", dto.getDepartmentId());
        assertEquals("test-bankAccountId", dto.getBankAccountId());
        assertEquals("test-paymentGatewayCustomerId", dto.getPaymentGatewayCustomerId());
        assertEquals("test-paymentGatewaySubscriptionId", dto.getPaymentGatewaySubscriptionId());
        assertTrue(dto.getSendReminder());
        assertEquals(42, dto.getReminderDaysBefore());
        assertTrue(dto.getProrateFirstInstallment());
        assertEquals(BigDecimal.TEN, dto.getProrationAmount());
        assertEquals("test-agreedBy", dto.getAgreedBy());
        assertEquals("test-agreementReference", dto.getAgreementReference());
        assertEquals("test-contractId", dto.getContractId());
    }

    @Test
    void testSettersAndGetters() {
        PaymentScheduleResponseDto dto = new PaymentScheduleResponseDto();
        dto.setId("val-id");
        dto.setScheduleId("val-scheduleId");
        dto.setTenantId("val-tenantId");
        dto.setScheduleNumber("val-scheduleNumber");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setInvoiceId("val-invoiceId");
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setTotalAmount(BigDecimal.ONE);
        dto.setAmountPaid(BigDecimal.ONE);
        dto.setBalanceRemaining(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setStatus(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING);
        dto.setScheduleType(PaymentScheduleResponseDto.ScheduleTypeDto.FIXED_INSTALLMENTS);
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setInstallmentCount(99);
        dto.setFrequency("val-frequency");
        dto.setPaymentMethod("val-paymentMethod");
        dto.setAutoCharge(true);
        dto.setDescription("val-description");
        dto.setNotes("val-notes");
        dto.setCreatedBy("val-createdBy");
        dto.setNextPaymentDate("val-nextPaymentDate");
        dto.setTotalInstallments(99);
        dto.setCompletedInstallments(99);
        dto.setInstallmentAmount(BigDecimal.ONE);
        dto.setTemplateId("val-templateId");
        dto.setOriginalScheduleId("val-originalScheduleId");
        dto.setIsRescheduled(true);
        dto.setRescheduleReason("val-rescheduleReason");
        dto.setRescheduleDate(LocalDate.of(2025,6,1));
        dto.setProjectId("val-projectId");
        dto.setDepartmentId("val-departmentId");
        dto.setBankAccountId("val-bankAccountId");
        dto.setPaymentGatewayCustomerId("val-paymentGatewayCustomerId");
        dto.setPaymentGatewaySubscriptionId("val-paymentGatewaySubscriptionId");
        dto.setSendReminder(true);
        dto.setReminderDaysBefore(99);
        dto.setProrateFirstInstallment(true);
        dto.setProrationAmount(BigDecimal.ONE);
        dto.setAgreedBy("val-agreedBy");
        dto.setAgreementReference("val-agreementReference");
        dto.setContractId("val-contractId");
        assertEquals("val-id", dto.getId());
        assertEquals("val-scheduleId", dto.getScheduleId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-scheduleNumber", dto.getScheduleNumber());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(BigDecimal.ONE, dto.getTotalAmount());
        assertEquals(BigDecimal.ONE, dto.getAmountPaid());
        assertEquals(BigDecimal.ONE, dto.getBalanceRemaining());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING, dto.getStatus());
        assertEquals(PaymentScheduleResponseDto.ScheduleTypeDto.FIXED_INSTALLMENTS, dto.getScheduleType());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(99, dto.getInstallmentCount());
        assertEquals("val-frequency", dto.getFrequency());
        assertEquals("val-paymentMethod", dto.getPaymentMethod());
        assertTrue(dto.getAutoCharge());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-nextPaymentDate", dto.getNextPaymentDate());
        assertEquals(99, dto.getTotalInstallments());
        assertEquals(99, dto.getCompletedInstallments());
        assertEquals(BigDecimal.ONE, dto.getInstallmentAmount());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-originalScheduleId", dto.getOriginalScheduleId());
        assertTrue(dto.getIsRescheduled());
        assertEquals("val-rescheduleReason", dto.getRescheduleReason());
        assertEquals(LocalDate.of(2025,6,1), dto.getRescheduleDate());
        assertEquals("val-projectId", dto.getProjectId());
        assertEquals("val-departmentId", dto.getDepartmentId());
        assertEquals("val-bankAccountId", dto.getBankAccountId());
        assertEquals("val-paymentGatewayCustomerId", dto.getPaymentGatewayCustomerId());
        assertEquals("val-paymentGatewaySubscriptionId", dto.getPaymentGatewaySubscriptionId());
        assertTrue(dto.getSendReminder());
        assertEquals(99, dto.getReminderDaysBefore());
        assertTrue(dto.getProrateFirstInstallment());
        assertEquals(BigDecimal.ONE, dto.getProrationAmount());
        assertEquals("val-agreedBy", dto.getAgreedBy());
        assertEquals("val-agreementReference", dto.getAgreementReference());
        assertEquals("val-contractId", dto.getContractId());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentScheduleResponseDto dto1 = PaymentScheduleResponseDto.builder()
                        .id("test-id")
            .scheduleId("test-scheduleId")
            .tenantId("test-tenantId")
            .scheduleNumber("test-scheduleNumber")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .totalAmount(BigDecimal.TEN)
            .amountPaid(BigDecimal.TEN)
            .balanceRemaining(BigDecimal.TEN)
            .currency("test-currency")
            .status(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING)
            .scheduleType(PaymentScheduleResponseDto.ScheduleTypeDto.FIXED_INSTALLMENTS)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .installmentCount(42)
            .frequency("test-frequency")
            .paymentMethod("test-paymentMethod")
            .autoCharge(true)
            .description("test-description")
            .notes("test-notes")
            .scheduledPayments(Collections.emptyList())
            .createdBy("test-createdBy")
            .lastPaymentDate(Instant.parse("2025-01-15T10:00:00Z"))
            .nextPaymentDate("test-nextPaymentDate")
            .totalInstallments(42)
            .completedInstallments(42)
            .installmentAmount(BigDecimal.TEN)
            .templateId("test-templateId")
            .originalScheduleId("test-originalScheduleId")
            .isRescheduled(true)
            .rescheduleReason("test-rescheduleReason")
            .rescheduleDate(LocalDate.of(2025,1,15))
            .tags(Collections.emptyList())
            .projectId("test-projectId")
            .departmentId("test-departmentId")
            .bankAccountId("test-bankAccountId")
            .paymentGatewayCustomerId("test-paymentGatewayCustomerId")
            .paymentGatewaySubscriptionId("test-paymentGatewaySubscriptionId")
            .sendReminder(true)
            .reminderDaysBefore(42)
            .prorateFirstInstallment(true)
            .prorationAmount(BigDecimal.TEN)
            .agreedBy("test-agreedBy")
            .agreedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .agreementReference("test-agreementReference")
            .contractId("test-contractId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        PaymentScheduleResponseDto dto2 = PaymentScheduleResponseDto.builder()
                        .id("test-id")
            .scheduleId("test-scheduleId")
            .tenantId("test-tenantId")
            .scheduleNumber("test-scheduleNumber")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .totalAmount(BigDecimal.TEN)
            .amountPaid(BigDecimal.TEN)
            .balanceRemaining(BigDecimal.TEN)
            .currency("test-currency")
            .status(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING)
            .scheduleType(PaymentScheduleResponseDto.ScheduleTypeDto.FIXED_INSTALLMENTS)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .installmentCount(42)
            .frequency("test-frequency")
            .paymentMethod("test-paymentMethod")
            .autoCharge(true)
            .description("test-description")
            .notes("test-notes")
            .scheduledPayments(Collections.emptyList())
            .createdBy("test-createdBy")
            .lastPaymentDate(Instant.parse("2025-01-15T10:00:00Z"))
            .nextPaymentDate("test-nextPaymentDate")
            .totalInstallments(42)
            .completedInstallments(42)
            .installmentAmount(BigDecimal.TEN)
            .templateId("test-templateId")
            .originalScheduleId("test-originalScheduleId")
            .isRescheduled(true)
            .rescheduleReason("test-rescheduleReason")
            .rescheduleDate(LocalDate.of(2025,1,15))
            .tags(Collections.emptyList())
            .projectId("test-projectId")
            .departmentId("test-departmentId")
            .bankAccountId("test-bankAccountId")
            .paymentGatewayCustomerId("test-paymentGatewayCustomerId")
            .paymentGatewaySubscriptionId("test-paymentGatewaySubscriptionId")
            .sendReminder(true)
            .reminderDaysBefore(42)
            .prorateFirstInstallment(true)
            .prorationAmount(BigDecimal.TEN)
            .agreedBy("test-agreedBy")
            .agreedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .agreementReference("test-agreementReference")
            .contractId("test-contractId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PaymentScheduleResponseDto dto = PaymentScheduleResponseDto.builder()
                        .id("test-id")
            .scheduleId("test-scheduleId")
            .tenantId("test-tenantId")
            .scheduleNumber("test-scheduleNumber")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .totalAmount(BigDecimal.TEN)
            .amountPaid(BigDecimal.TEN)
            .balanceRemaining(BigDecimal.TEN)
            .currency("test-currency")
            .status(PaymentScheduleResponseDto.ScheduleStatusDto.PENDING)
            .scheduleType(PaymentScheduleResponseDto.ScheduleTypeDto.FIXED_INSTALLMENTS)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .installmentCount(42)
            .frequency("test-frequency")
            .paymentMethod("test-paymentMethod")
            .autoCharge(true)
            .description("test-description")
            .notes("test-notes")
            .scheduledPayments(Collections.emptyList())
            .createdBy("test-createdBy")
            .lastPaymentDate(Instant.parse("2025-01-15T10:00:00Z"))
            .nextPaymentDate("test-nextPaymentDate")
            .totalInstallments(42)
            .completedInstallments(42)
            .installmentAmount(BigDecimal.TEN)
            .templateId("test-templateId")
            .originalScheduleId("test-originalScheduleId")
            .isRescheduled(true)
            .rescheduleReason("test-rescheduleReason")
            .rescheduleDate(LocalDate.of(2025,1,15))
            .tags(Collections.emptyList())
            .projectId("test-projectId")
            .departmentId("test-departmentId")
            .bankAccountId("test-bankAccountId")
            .paymentGatewayCustomerId("test-paymentGatewayCustomerId")
            .paymentGatewaySubscriptionId("test-paymentGatewaySubscriptionId")
            .sendReminder(true)
            .reminderDaysBefore(42)
            .prorateFirstInstallment(true)
            .prorationAmount(BigDecimal.TEN)
            .agreedBy("test-agreedBy")
            .agreedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .agreementReference("test-agreementReference")
            .contractId("test-contractId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}