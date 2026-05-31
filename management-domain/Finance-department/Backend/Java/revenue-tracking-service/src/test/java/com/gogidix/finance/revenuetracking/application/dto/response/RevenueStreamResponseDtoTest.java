package com.gogidix.finance.revenuetracking.application.dto.response;

import com.gogidix.finance.revenuetracking.application.dto.response.RevenueStreamResponseDto;
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
class RevenueStreamResponseDtoTest {

        @Test
    void testBuilder() {
        RevenueStreamResponseDto dto = RevenueStreamResponseDto.builder()
                        .id("test-id")
            .streamId("test-streamId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .streamName("test-streamName")
            .description("test-description")
            .type(RevenueStreamResponseDto.RevenueStreamTypeDto.SUBSCRIPTION)
            .status(RevenueStreamResponseDto.StreamStatusDto.ACTIVE)
            .recurringAmount(BigDecimal.TEN)
            .currency("test-currency")
            .billingCycle(RevenueStreamResponseDto.BillingCycleDto.WEEKLY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .contractTermMonths(42)
            .nextBillingDate(LocalDate.of(2025,1,15))
            .lastBillingDate(LocalDate.of(2025,1,15))
            .billingCount(42)
            .totalBillingCycles(42)
            .totalValue(BigDecimal.TEN)
            .recognizedValue(BigDecimal.TEN)
            .paymentTerms(RevenueStreamResponseDto.PaymentTermsDto.NET_15)
            .autoRenewal(RevenueStreamResponseDto.AutoRenewalDto.ENABLED)
            .renewalReminderDays(42)
            .productId("test-productId")
            .productSku("test-productSku")
            .salespersonId("test-salespersonId")
            .department("test-department")
            .costCenter("test-costCenter")
            .region("test-region")
            .contractUrl("test-contractUrl")
            .tiers(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .mrr(BigDecimal.TEN)
            .arr(BigDecimal.TEN)
            .isTrial(true)
            .trialDays(42)
            .trialEndDate(LocalDate.of(2025,1,15))
            .cancellationReason("test-cancellationReason")
            .cancellationDate(LocalDate.of(2025,1,15))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-streamId", dto.getStreamId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-streamName", dto.getStreamName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(RevenueStreamResponseDto.RevenueStreamTypeDto.SUBSCRIPTION, dto.getType());
        assertEquals(RevenueStreamResponseDto.StreamStatusDto.ACTIVE, dto.getStatus());
        assertEquals(BigDecimal.TEN, dto.getRecurringAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(RevenueStreamResponseDto.BillingCycleDto.WEEKLY, dto.getBillingCycle());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals(42, dto.getContractTermMonths());
        assertEquals(LocalDate.of(2025,1,15), dto.getNextBillingDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getLastBillingDate());
        assertEquals(42, dto.getBillingCount());
        assertEquals(42, dto.getTotalBillingCycles());
        assertEquals(BigDecimal.TEN, dto.getTotalValue());
        assertEquals(BigDecimal.TEN, dto.getRecognizedValue());
        assertEquals(RevenueStreamResponseDto.PaymentTermsDto.NET_15, dto.getPaymentTerms());
        assertEquals(RevenueStreamResponseDto.AutoRenewalDto.ENABLED, dto.getAutoRenewal());
        assertEquals(42, dto.getRenewalReminderDays());
        assertEquals("test-productId", dto.getProductId());
        assertEquals("test-productSku", dto.getProductSku());
        assertEquals("test-salespersonId", dto.getSalespersonId());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-costCenter", dto.getCostCenter());
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-contractUrl", dto.getContractUrl());
        assertEquals("test-notes", dto.getNotes());
        assertEquals(BigDecimal.TEN, dto.getMrr());
        assertEquals(BigDecimal.TEN, dto.getArr());
        assertTrue(dto.getIsTrial());
        assertEquals(42, dto.getTrialDays());
        assertEquals(LocalDate.of(2025,1,15), dto.getTrialEndDate());
        assertEquals("test-cancellationReason", dto.getCancellationReason());
        assertEquals(LocalDate.of(2025,1,15), dto.getCancellationDate());
    }

    @Test
    void testSettersAndGetters() {
        RevenueStreamResponseDto dto = new RevenueStreamResponseDto();
        dto.setId("val-id");
        dto.setStreamId("val-streamId");
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setStreamName("val-streamName");
        dto.setDescription("val-description");
        dto.setType(RevenueStreamResponseDto.RevenueStreamTypeDto.SUBSCRIPTION);
        dto.setStatus(RevenueStreamResponseDto.StreamStatusDto.ACTIVE);
        dto.setRecurringAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setBillingCycle(RevenueStreamResponseDto.BillingCycleDto.WEEKLY);
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setContractTermMonths(99);
        dto.setNextBillingDate(LocalDate.of(2025,6,1));
        dto.setLastBillingDate(LocalDate.of(2025,6,1));
        dto.setBillingCount(99);
        dto.setTotalBillingCycles(99);
        dto.setTotalValue(BigDecimal.ONE);
        dto.setRecognizedValue(BigDecimal.ONE);
        dto.setPaymentTerms(RevenueStreamResponseDto.PaymentTermsDto.NET_15);
        dto.setAutoRenewal(RevenueStreamResponseDto.AutoRenewalDto.ENABLED);
        dto.setRenewalReminderDays(99);
        dto.setProductId("val-productId");
        dto.setProductSku("val-productSku");
        dto.setSalespersonId("val-salespersonId");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setRegion("val-region");
        dto.setContractUrl("val-contractUrl");
        dto.setNotes("val-notes");
        dto.setMrr(BigDecimal.ONE);
        dto.setArr(BigDecimal.ONE);
        dto.setIsTrial(true);
        dto.setTrialDays(99);
        dto.setTrialEndDate(LocalDate.of(2025,6,1));
        dto.setCancellationReason("val-cancellationReason");
        dto.setCancellationDate(LocalDate.of(2025,6,1));
        assertEquals("val-id", dto.getId());
        assertEquals("val-streamId", dto.getStreamId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-streamName", dto.getStreamName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(RevenueStreamResponseDto.RevenueStreamTypeDto.SUBSCRIPTION, dto.getType());
        assertEquals(RevenueStreamResponseDto.StreamStatusDto.ACTIVE, dto.getStatus());
        assertEquals(BigDecimal.ONE, dto.getRecurringAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(RevenueStreamResponseDto.BillingCycleDto.WEEKLY, dto.getBillingCycle());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(99, dto.getContractTermMonths());
        assertEquals(LocalDate.of(2025,6,1), dto.getNextBillingDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getLastBillingDate());
        assertEquals(99, dto.getBillingCount());
        assertEquals(99, dto.getTotalBillingCycles());
        assertEquals(BigDecimal.ONE, dto.getTotalValue());
        assertEquals(BigDecimal.ONE, dto.getRecognizedValue());
        assertEquals(RevenueStreamResponseDto.PaymentTermsDto.NET_15, dto.getPaymentTerms());
        assertEquals(RevenueStreamResponseDto.AutoRenewalDto.ENABLED, dto.getAutoRenewal());
        assertEquals(99, dto.getRenewalReminderDays());
        assertEquals("val-productId", dto.getProductId());
        assertEquals("val-productSku", dto.getProductSku());
        assertEquals("val-salespersonId", dto.getSalespersonId());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-contractUrl", dto.getContractUrl());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(BigDecimal.ONE, dto.getMrr());
        assertEquals(BigDecimal.ONE, dto.getArr());
        assertTrue(dto.getIsTrial());
        assertEquals(99, dto.getTrialDays());
        assertEquals(LocalDate.of(2025,6,1), dto.getTrialEndDate());
        assertEquals("val-cancellationReason", dto.getCancellationReason());
        assertEquals(LocalDate.of(2025,6,1), dto.getCancellationDate());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueStreamResponseDto dto1 = RevenueStreamResponseDto.builder()
                        .id("test-id")
            .streamId("test-streamId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .streamName("test-streamName")
            .description("test-description")
            .type(RevenueStreamResponseDto.RevenueStreamTypeDto.SUBSCRIPTION)
            .status(RevenueStreamResponseDto.StreamStatusDto.ACTIVE)
            .recurringAmount(BigDecimal.TEN)
            .currency("test-currency")
            .billingCycle(RevenueStreamResponseDto.BillingCycleDto.WEEKLY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .contractTermMonths(42)
            .nextBillingDate(LocalDate.of(2025,1,15))
            .lastBillingDate(LocalDate.of(2025,1,15))
            .billingCount(42)
            .totalBillingCycles(42)
            .totalValue(BigDecimal.TEN)
            .recognizedValue(BigDecimal.TEN)
            .paymentTerms(RevenueStreamResponseDto.PaymentTermsDto.NET_15)
            .autoRenewal(RevenueStreamResponseDto.AutoRenewalDto.ENABLED)
            .renewalReminderDays(42)
            .productId("test-productId")
            .productSku("test-productSku")
            .salespersonId("test-salespersonId")
            .department("test-department")
            .costCenter("test-costCenter")
            .region("test-region")
            .contractUrl("test-contractUrl")
            .tiers(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .mrr(BigDecimal.TEN)
            .arr(BigDecimal.TEN)
            .isTrial(true)
            .trialDays(42)
            .trialEndDate(LocalDate.of(2025,1,15))
            .cancellationReason("test-cancellationReason")
            .cancellationDate(LocalDate.of(2025,1,15))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        RevenueStreamResponseDto dto2 = RevenueStreamResponseDto.builder()
                        .id("test-id")
            .streamId("test-streamId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .streamName("test-streamName")
            .description("test-description")
            .type(RevenueStreamResponseDto.RevenueStreamTypeDto.SUBSCRIPTION)
            .status(RevenueStreamResponseDto.StreamStatusDto.ACTIVE)
            .recurringAmount(BigDecimal.TEN)
            .currency("test-currency")
            .billingCycle(RevenueStreamResponseDto.BillingCycleDto.WEEKLY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .contractTermMonths(42)
            .nextBillingDate(LocalDate.of(2025,1,15))
            .lastBillingDate(LocalDate.of(2025,1,15))
            .billingCount(42)
            .totalBillingCycles(42)
            .totalValue(BigDecimal.TEN)
            .recognizedValue(BigDecimal.TEN)
            .paymentTerms(RevenueStreamResponseDto.PaymentTermsDto.NET_15)
            .autoRenewal(RevenueStreamResponseDto.AutoRenewalDto.ENABLED)
            .renewalReminderDays(42)
            .productId("test-productId")
            .productSku("test-productSku")
            .salespersonId("test-salespersonId")
            .department("test-department")
            .costCenter("test-costCenter")
            .region("test-region")
            .contractUrl("test-contractUrl")
            .tiers(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .mrr(BigDecimal.TEN)
            .arr(BigDecimal.TEN)
            .isTrial(true)
            .trialDays(42)
            .trialEndDate(LocalDate.of(2025,1,15))
            .cancellationReason("test-cancellationReason")
            .cancellationDate(LocalDate.of(2025,1,15))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RevenueStreamResponseDto dto = RevenueStreamResponseDto.builder()
                        .id("test-id")
            .streamId("test-streamId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .streamName("test-streamName")
            .description("test-description")
            .type(RevenueStreamResponseDto.RevenueStreamTypeDto.SUBSCRIPTION)
            .status(RevenueStreamResponseDto.StreamStatusDto.ACTIVE)
            .recurringAmount(BigDecimal.TEN)
            .currency("test-currency")
            .billingCycle(RevenueStreamResponseDto.BillingCycleDto.WEEKLY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .contractTermMonths(42)
            .nextBillingDate(LocalDate.of(2025,1,15))
            .lastBillingDate(LocalDate.of(2025,1,15))
            .billingCount(42)
            .totalBillingCycles(42)
            .totalValue(BigDecimal.TEN)
            .recognizedValue(BigDecimal.TEN)
            .paymentTerms(RevenueStreamResponseDto.PaymentTermsDto.NET_15)
            .autoRenewal(RevenueStreamResponseDto.AutoRenewalDto.ENABLED)
            .renewalReminderDays(42)
            .productId("test-productId")
            .productSku("test-productSku")
            .salespersonId("test-salespersonId")
            .department("test-department")
            .costCenter("test-costCenter")
            .region("test-region")
            .contractUrl("test-contractUrl")
            .tiers(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .mrr(BigDecimal.TEN)
            .arr(BigDecimal.TEN)
            .isTrial(true)
            .trialDays(42)
            .trialEndDate(LocalDate.of(2025,1,15))
            .cancellationReason("test-cancellationReason")
            .cancellationDate(LocalDate.of(2025,1,15))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}