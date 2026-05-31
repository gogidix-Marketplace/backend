package com.gogidix.finance.revenuetracking.application.dto.response;

import com.gogidix.finance.revenuetracking.application.dto.response.RevenueResponseDto;
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
class RevenueResponseDtoTest {

        @Test
    void testBuilder() {
        RevenueResponseDto dto = RevenueResponseDto.builder()
                        .id("test-id")
            .revenueId("test-revenueId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .contractId("test-contractId")
            .projectId("test-projectId")
            .type(RevenueResponseDto.RevenueTypeDto.RECURRING)
            .totalAmount(BigDecimal.TEN)
            .currency("test-currency")
            .recognizedAmount(BigDecimal.TEN)
            .deferredAmount(BigDecimal.TEN)
            .status(RevenueResponseDto.RevenueStatusDto.PENDING)
            .recognitionMethod(RevenueResponseDto.RecognitionMethodDto.POINT_IN_TIME)
            .recognitionStartDate(LocalDate.of(2025,1,15))
            .recognitionEndDate(LocalDate.of(2025,1,15))
            .recognitionPeriods(42)
            .currentPeriod(42)
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .category("test-category")
            .productCode("test-productCode")
            .productSku("test-productSku")
            .department("test-department")
            .costCenter("test-costCenter")
            .salespersonId("test-salespersonId")
            .region("test-region")
            .territory("test-territory")
            .paymentTerms(RevenueResponseDto.PaymentTermsDto.NET_15)
            .invoiceDate(LocalDate.of(2025,1,15))
            .invoiceNumber("test-invoiceNumber")
            .dueDate(LocalDate.of(2025,1,15))
            .paidDate(LocalDate.of(2025,1,15))
            .fullyPaid(true)
            .milestones(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .isRecurring(true)
            .recurringSchedule("test-recurringSchedule")
            .nextRecognitionDate(LocalDate.of(2025,1,15))
            .recognizedThisPeriod(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-revenueId", dto.getRevenueId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-contractId", dto.getContractId());
        assertEquals("test-projectId", dto.getProjectId());
        assertEquals(RevenueResponseDto.RevenueTypeDto.RECURRING, dto.getType());
        assertEquals(BigDecimal.TEN, dto.getTotalAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getRecognizedAmount());
        assertEquals(BigDecimal.TEN, dto.getDeferredAmount());
        assertEquals(RevenueResponseDto.RevenueStatusDto.PENDING, dto.getStatus());
        assertEquals(RevenueResponseDto.RecognitionMethodDto.POINT_IN_TIME, dto.getRecognitionMethod());
        assertEquals(LocalDate.of(2025,1,15), dto.getRecognitionStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getRecognitionEndDate());
        assertEquals(42, dto.getRecognitionPeriods());
        assertEquals(42, dto.getCurrentPeriod());
        assertEquals(LocalDate.of(2025,1,15), dto.getTransactionDate());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-productCode", dto.getProductCode());
        assertEquals("test-productSku", dto.getProductSku());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-costCenter", dto.getCostCenter());
        assertEquals("test-salespersonId", dto.getSalespersonId());
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-territory", dto.getTerritory());
        assertEquals(RevenueResponseDto.PaymentTermsDto.NET_15, dto.getPaymentTerms());
        assertEquals(LocalDate.of(2025,1,15), dto.getInvoiceDate());
        assertEquals("test-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(LocalDate.of(2025,1,15), dto.getDueDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getPaidDate());
        assertTrue(dto.getFullyPaid());
        assertEquals("test-notes", dto.getNotes());
        assertTrue(dto.getIsRecurring());
        assertEquals("test-recurringSchedule", dto.getRecurringSchedule());
        assertEquals(LocalDate.of(2025,1,15), dto.getNextRecognitionDate());
        assertEquals(BigDecimal.TEN, dto.getRecognizedThisPeriod());
    }

    @Test
    void testSettersAndGetters() {
        RevenueResponseDto dto = new RevenueResponseDto();
        dto.setId("val-id");
        dto.setRevenueId("val-revenueId");
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setContractId("val-contractId");
        dto.setProjectId("val-projectId");
        dto.setType(RevenueResponseDto.RevenueTypeDto.RECURRING);
        dto.setTotalAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setRecognizedAmount(BigDecimal.ONE);
        dto.setDeferredAmount(BigDecimal.ONE);
        dto.setStatus(RevenueResponseDto.RevenueStatusDto.PENDING);
        dto.setRecognitionMethod(RevenueResponseDto.RecognitionMethodDto.POINT_IN_TIME);
        dto.setRecognitionStartDate(LocalDate.of(2025,6,1));
        dto.setRecognitionEndDate(LocalDate.of(2025,6,1));
        dto.setRecognitionPeriods(99);
        dto.setCurrentPeriod(99);
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setCategory("val-category");
        dto.setProductCode("val-productCode");
        dto.setProductSku("val-productSku");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setSalespersonId("val-salespersonId");
        dto.setRegion("val-region");
        dto.setTerritory("val-territory");
        dto.setPaymentTerms(RevenueResponseDto.PaymentTermsDto.NET_15);
        dto.setInvoiceDate(LocalDate.of(2025,6,1));
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setDueDate(LocalDate.of(2025,6,1));
        dto.setPaidDate(LocalDate.of(2025,6,1));
        dto.setFullyPaid(true);
        dto.setNotes("val-notes");
        dto.setIsRecurring(true);
        dto.setRecurringSchedule("val-recurringSchedule");
        dto.setNextRecognitionDate(LocalDate.of(2025,6,1));
        dto.setRecognizedThisPeriod(BigDecimal.ONE);
        assertEquals("val-id", dto.getId());
        assertEquals("val-revenueId", dto.getRevenueId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-contractId", dto.getContractId());
        assertEquals("val-projectId", dto.getProjectId());
        assertEquals(RevenueResponseDto.RevenueTypeDto.RECURRING, dto.getType());
        assertEquals(BigDecimal.ONE, dto.getTotalAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getRecognizedAmount());
        assertEquals(BigDecimal.ONE, dto.getDeferredAmount());
        assertEquals(RevenueResponseDto.RevenueStatusDto.PENDING, dto.getStatus());
        assertEquals(RevenueResponseDto.RecognitionMethodDto.POINT_IN_TIME, dto.getRecognitionMethod());
        assertEquals(LocalDate.of(2025,6,1), dto.getRecognitionStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getRecognitionEndDate());
        assertEquals(99, dto.getRecognitionPeriods());
        assertEquals(99, dto.getCurrentPeriod());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-productCode", dto.getProductCode());
        assertEquals("val-productSku", dto.getProductSku());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-salespersonId", dto.getSalespersonId());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-territory", dto.getTerritory());
        assertEquals(RevenueResponseDto.PaymentTermsDto.NET_15, dto.getPaymentTerms());
        assertEquals(LocalDate.of(2025,6,1), dto.getInvoiceDate());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getPaidDate());
        assertTrue(dto.getFullyPaid());
        assertEquals("val-notes", dto.getNotes());
        assertTrue(dto.getIsRecurring());
        assertEquals("val-recurringSchedule", dto.getRecurringSchedule());
        assertEquals(LocalDate.of(2025,6,1), dto.getNextRecognitionDate());
        assertEquals(BigDecimal.ONE, dto.getRecognizedThisPeriod());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueResponseDto dto1 = RevenueResponseDto.builder()
                        .id("test-id")
            .revenueId("test-revenueId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .contractId("test-contractId")
            .projectId("test-projectId")
            .type(RevenueResponseDto.RevenueTypeDto.RECURRING)
            .totalAmount(BigDecimal.TEN)
            .currency("test-currency")
            .recognizedAmount(BigDecimal.TEN)
            .deferredAmount(BigDecimal.TEN)
            .status(RevenueResponseDto.RevenueStatusDto.PENDING)
            .recognitionMethod(RevenueResponseDto.RecognitionMethodDto.POINT_IN_TIME)
            .recognitionStartDate(LocalDate.of(2025,1,15))
            .recognitionEndDate(LocalDate.of(2025,1,15))
            .recognitionPeriods(42)
            .currentPeriod(42)
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .category("test-category")
            .productCode("test-productCode")
            .productSku("test-productSku")
            .department("test-department")
            .costCenter("test-costCenter")
            .salespersonId("test-salespersonId")
            .region("test-region")
            .territory("test-territory")
            .paymentTerms(RevenueResponseDto.PaymentTermsDto.NET_15)
            .invoiceDate(LocalDate.of(2025,1,15))
            .invoiceNumber("test-invoiceNumber")
            .dueDate(LocalDate.of(2025,1,15))
            .paidDate(LocalDate.of(2025,1,15))
            .fullyPaid(true)
            .milestones(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .isRecurring(true)
            .recurringSchedule("test-recurringSchedule")
            .nextRecognitionDate(LocalDate.of(2025,1,15))
            .recognizedThisPeriod(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        RevenueResponseDto dto2 = RevenueResponseDto.builder()
                        .id("test-id")
            .revenueId("test-revenueId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .contractId("test-contractId")
            .projectId("test-projectId")
            .type(RevenueResponseDto.RevenueTypeDto.RECURRING)
            .totalAmount(BigDecimal.TEN)
            .currency("test-currency")
            .recognizedAmount(BigDecimal.TEN)
            .deferredAmount(BigDecimal.TEN)
            .status(RevenueResponseDto.RevenueStatusDto.PENDING)
            .recognitionMethod(RevenueResponseDto.RecognitionMethodDto.POINT_IN_TIME)
            .recognitionStartDate(LocalDate.of(2025,1,15))
            .recognitionEndDate(LocalDate.of(2025,1,15))
            .recognitionPeriods(42)
            .currentPeriod(42)
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .category("test-category")
            .productCode("test-productCode")
            .productSku("test-productSku")
            .department("test-department")
            .costCenter("test-costCenter")
            .salespersonId("test-salespersonId")
            .region("test-region")
            .territory("test-territory")
            .paymentTerms(RevenueResponseDto.PaymentTermsDto.NET_15)
            .invoiceDate(LocalDate.of(2025,1,15))
            .invoiceNumber("test-invoiceNumber")
            .dueDate(LocalDate.of(2025,1,15))
            .paidDate(LocalDate.of(2025,1,15))
            .fullyPaid(true)
            .milestones(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .isRecurring(true)
            .recurringSchedule("test-recurringSchedule")
            .nextRecognitionDate(LocalDate.of(2025,1,15))
            .recognizedThisPeriod(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RevenueResponseDto dto = RevenueResponseDto.builder()
                        .id("test-id")
            .revenueId("test-revenueId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .contractId("test-contractId")
            .projectId("test-projectId")
            .type(RevenueResponseDto.RevenueTypeDto.RECURRING)
            .totalAmount(BigDecimal.TEN)
            .currency("test-currency")
            .recognizedAmount(BigDecimal.TEN)
            .deferredAmount(BigDecimal.TEN)
            .status(RevenueResponseDto.RevenueStatusDto.PENDING)
            .recognitionMethod(RevenueResponseDto.RecognitionMethodDto.POINT_IN_TIME)
            .recognitionStartDate(LocalDate.of(2025,1,15))
            .recognitionEndDate(LocalDate.of(2025,1,15))
            .recognitionPeriods(42)
            .currentPeriod(42)
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .category("test-category")
            .productCode("test-productCode")
            .productSku("test-productSku")
            .department("test-department")
            .costCenter("test-costCenter")
            .salespersonId("test-salespersonId")
            .region("test-region")
            .territory("test-territory")
            .paymentTerms(RevenueResponseDto.PaymentTermsDto.NET_15)
            .invoiceDate(LocalDate.of(2025,1,15))
            .invoiceNumber("test-invoiceNumber")
            .dueDate(LocalDate.of(2025,1,15))
            .paidDate(LocalDate.of(2025,1,15))
            .fullyPaid(true)
            .milestones(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .isRecurring(true)
            .recurringSchedule("test-recurringSchedule")
            .nextRecognitionDate(LocalDate.of(2025,1,15))
            .recognizedThisPeriod(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}