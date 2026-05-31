package com.gogidix.finance.revenuetracking.application.dto.response;

import com.gogidix.finance.revenuetracking.application.dto.response.RevenueRecognitionResponseDto;
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
class RevenueRecognitionResponseDtoTest {

        @Test
    void testBuilder() {
        RevenueRecognitionResponseDto dto = RevenueRecognitionResponseDto.builder()
                        .id("test-id")
            .recognitionId("test-recognitionId")
            .tenantId("test-tenantId")
            .revenueId("test-revenueId")
            .contractId("test-contractId")
            .scheduleType(RevenueRecognitionResponseDto.RecognitionScheduleTypeDto.STRAIGHT_LINE)
            .status(RevenueRecognitionResponseDto.RecognitionStatusDto.PENDING)
            .totalAmount(BigDecimal.TEN)
            .currency("test-currency")
            .recognizedAmount(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .totalPeriods(42)
            .completedPeriods(42)
            .periods(Collections.emptyList())
            .recognitionMethod(RevenueRecognitionResponseDto.RecognitionMethodDto.ASC_606)
            .description("test-description")
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .approvedDate(LocalDate.of(2025,1,15))
            .accountingStandard("test-accountingStandard")
            .performanceObligation("test-performanceObligation")
            .transactionPrice("test-transactionPrice")
            .allocationKeys(Collections.emptyList())
            .standaloneSellingPrice(true)
            .contractAssetId("test-contractAssetId")
            .contractLiabilityId("test-contractLiabilityId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-recognitionId", dto.getRecognitionId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-revenueId", dto.getRevenueId());
        assertEquals("test-contractId", dto.getContractId());
        assertEquals(RevenueRecognitionResponseDto.RecognitionScheduleTypeDto.STRAIGHT_LINE, dto.getScheduleType());
        assertEquals(RevenueRecognitionResponseDto.RecognitionStatusDto.PENDING, dto.getStatus());
        assertEquals(BigDecimal.TEN, dto.getTotalAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getRecognizedAmount());
        assertEquals(BigDecimal.TEN, dto.getRemainingAmount());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals(42, dto.getTotalPeriods());
        assertEquals(42, dto.getCompletedPeriods());
        assertEquals(RevenueRecognitionResponseDto.RecognitionMethodDto.ASC_606, dto.getRecognitionMethod());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals(LocalDate.of(2025,1,15), dto.getApprovedDate());
        assertEquals("test-accountingStandard", dto.getAccountingStandard());
        assertEquals("test-performanceObligation", dto.getPerformanceObligation());
        assertEquals("test-transactionPrice", dto.getTransactionPrice());
        assertTrue(dto.getStandaloneSellingPrice());
        assertEquals("test-contractAssetId", dto.getContractAssetId());
        assertEquals("test-contractLiabilityId", dto.getContractLiabilityId());
    }

    @Test
    void testSettersAndGetters() {
        RevenueRecognitionResponseDto dto = new RevenueRecognitionResponseDto();
        dto.setId("val-id");
        dto.setRecognitionId("val-recognitionId");
        dto.setTenantId("val-tenantId");
        dto.setRevenueId("val-revenueId");
        dto.setContractId("val-contractId");
        dto.setScheduleType(RevenueRecognitionResponseDto.RecognitionScheduleTypeDto.STRAIGHT_LINE);
        dto.setStatus(RevenueRecognitionResponseDto.RecognitionStatusDto.PENDING);
        dto.setTotalAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setRecognizedAmount(BigDecimal.ONE);
        dto.setRemainingAmount(BigDecimal.ONE);
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setTotalPeriods(99);
        dto.setCompletedPeriods(99);
        dto.setRecognitionMethod(RevenueRecognitionResponseDto.RecognitionMethodDto.ASC_606);
        dto.setDescription("val-description");
        dto.setCreatedBy("val-createdBy");
        dto.setApprovedBy("val-approvedBy");
        dto.setApprovedDate(LocalDate.of(2025,6,1));
        dto.setAccountingStandard("val-accountingStandard");
        dto.setPerformanceObligation("val-performanceObligation");
        dto.setTransactionPrice("val-transactionPrice");
        dto.setStandaloneSellingPrice(true);
        dto.setContractAssetId("val-contractAssetId");
        dto.setContractLiabilityId("val-contractLiabilityId");
        assertEquals("val-id", dto.getId());
        assertEquals("val-recognitionId", dto.getRecognitionId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-revenueId", dto.getRevenueId());
        assertEquals("val-contractId", dto.getContractId());
        assertEquals(RevenueRecognitionResponseDto.RecognitionScheduleTypeDto.STRAIGHT_LINE, dto.getScheduleType());
        assertEquals(RevenueRecognitionResponseDto.RecognitionStatusDto.PENDING, dto.getStatus());
        assertEquals(BigDecimal.ONE, dto.getTotalAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getRecognizedAmount());
        assertEquals(BigDecimal.ONE, dto.getRemainingAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(99, dto.getTotalPeriods());
        assertEquals(99, dto.getCompletedPeriods());
        assertEquals(RevenueRecognitionResponseDto.RecognitionMethodDto.ASC_606, dto.getRecognitionMethod());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getApprovedDate());
        assertEquals("val-accountingStandard", dto.getAccountingStandard());
        assertEquals("val-performanceObligation", dto.getPerformanceObligation());
        assertEquals("val-transactionPrice", dto.getTransactionPrice());
        assertTrue(dto.getStandaloneSellingPrice());
        assertEquals("val-contractAssetId", dto.getContractAssetId());
        assertEquals("val-contractLiabilityId", dto.getContractLiabilityId());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueRecognitionResponseDto dto1 = RevenueRecognitionResponseDto.builder()
                        .id("test-id")
            .recognitionId("test-recognitionId")
            .tenantId("test-tenantId")
            .revenueId("test-revenueId")
            .contractId("test-contractId")
            .scheduleType(RevenueRecognitionResponseDto.RecognitionScheduleTypeDto.STRAIGHT_LINE)
            .status(RevenueRecognitionResponseDto.RecognitionStatusDto.PENDING)
            .totalAmount(BigDecimal.TEN)
            .currency("test-currency")
            .recognizedAmount(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .totalPeriods(42)
            .completedPeriods(42)
            .periods(Collections.emptyList())
            .recognitionMethod(RevenueRecognitionResponseDto.RecognitionMethodDto.ASC_606)
            .description("test-description")
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .approvedDate(LocalDate.of(2025,1,15))
            .accountingStandard("test-accountingStandard")
            .performanceObligation("test-performanceObligation")
            .transactionPrice("test-transactionPrice")
            .allocationKeys(Collections.emptyList())
            .standaloneSellingPrice(true)
            .contractAssetId("test-contractAssetId")
            .contractLiabilityId("test-contractLiabilityId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        RevenueRecognitionResponseDto dto2 = RevenueRecognitionResponseDto.builder()
                        .id("test-id")
            .recognitionId("test-recognitionId")
            .tenantId("test-tenantId")
            .revenueId("test-revenueId")
            .contractId("test-contractId")
            .scheduleType(RevenueRecognitionResponseDto.RecognitionScheduleTypeDto.STRAIGHT_LINE)
            .status(RevenueRecognitionResponseDto.RecognitionStatusDto.PENDING)
            .totalAmount(BigDecimal.TEN)
            .currency("test-currency")
            .recognizedAmount(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .totalPeriods(42)
            .completedPeriods(42)
            .periods(Collections.emptyList())
            .recognitionMethod(RevenueRecognitionResponseDto.RecognitionMethodDto.ASC_606)
            .description("test-description")
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .approvedDate(LocalDate.of(2025,1,15))
            .accountingStandard("test-accountingStandard")
            .performanceObligation("test-performanceObligation")
            .transactionPrice("test-transactionPrice")
            .allocationKeys(Collections.emptyList())
            .standaloneSellingPrice(true)
            .contractAssetId("test-contractAssetId")
            .contractLiabilityId("test-contractLiabilityId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RevenueRecognitionResponseDto dto = RevenueRecognitionResponseDto.builder()
                        .id("test-id")
            .recognitionId("test-recognitionId")
            .tenantId("test-tenantId")
            .revenueId("test-revenueId")
            .contractId("test-contractId")
            .scheduleType(RevenueRecognitionResponseDto.RecognitionScheduleTypeDto.STRAIGHT_LINE)
            .status(RevenueRecognitionResponseDto.RecognitionStatusDto.PENDING)
            .totalAmount(BigDecimal.TEN)
            .currency("test-currency")
            .recognizedAmount(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .totalPeriods(42)
            .completedPeriods(42)
            .periods(Collections.emptyList())
            .recognitionMethod(RevenueRecognitionResponseDto.RecognitionMethodDto.ASC_606)
            .description("test-description")
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .approvedDate(LocalDate.of(2025,1,15))
            .accountingStandard("test-accountingStandard")
            .performanceObligation("test-performanceObligation")
            .transactionPrice("test-transactionPrice")
            .allocationKeys(Collections.emptyList())
            .standaloneSellingPrice(true)
            .contractAssetId("test-contractAssetId")
            .contractLiabilityId("test-contractLiabilityId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}