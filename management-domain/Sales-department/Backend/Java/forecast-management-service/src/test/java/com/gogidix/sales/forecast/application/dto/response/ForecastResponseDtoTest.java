package com.gogidix.sales.forecast.application.dto.response;

import com.gogidix.sales.forecast.application.dto.response.ForecastResponseDto;
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
class ForecastResponseDtoTest {

        @Test
    void testBuilder() {
        ForecastResponseDto dto = ForecastResponseDto.builder()
                        .id("test-id")
            .forecastId("test-forecastId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .period(ForecastResponseDto.ForecastPeriodDto.MONTHLY)
            .startDate(null)
            .endDate(null)
            .status(ForecastResponseDto.ForecastStatusDto.DRAFT)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .version(42)
            .parentForecastId("test-parentForecastId")
            .totalBestCase(BigDecimal.TEN)
            .totalLikely(BigDecimal.TEN)
            .totalWorstCase(BigDecimal.TEN)
            .currency("test-currency")
            .region("test-region")
            .territory("test-territory")
            .businessUnit("test-businessUnit")
            .lineItems(Collections.emptyList())
            .currentApprovalLevel(ForecastResponseDto.ApprovalLevelDto.NONE)
            .rejectionReason("test-rejectionReason")
            .locked(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-forecastId", dto.getForecastId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(ForecastResponseDto.ForecastPeriodDto.MONTHLY, dto.getPeriod());
        assertEquals(ForecastResponseDto.ForecastStatusDto.DRAFT, dto.getStatus());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals(42, dto.getVersion());
        assertEquals("test-parentForecastId", dto.getParentForecastId());
        assertEquals(BigDecimal.TEN, dto.getTotalBestCase());
        assertEquals(BigDecimal.TEN, dto.getTotalLikely());
        assertEquals(BigDecimal.TEN, dto.getTotalWorstCase());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-territory", dto.getTerritory());
        assertEquals("test-businessUnit", dto.getBusinessUnit());
        assertEquals(ForecastResponseDto.ApprovalLevelDto.NONE, dto.getCurrentApprovalLevel());
        assertEquals("test-rejectionReason", dto.getRejectionReason());
        assertTrue(dto.getLocked());
    }

    @Test
    void testSettersAndGetters() {
        ForecastResponseDto dto = new ForecastResponseDto();
        dto.setId("val-id");
        dto.setForecastId("val-forecastId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setPeriod(ForecastResponseDto.ForecastPeriodDto.MONTHLY);
        dto.setStatus(ForecastResponseDto.ForecastStatusDto.DRAFT);
        dto.setCreatedBy("val-createdBy");
        dto.setApprovedBy("val-approvedBy");
        dto.setVersion(99);
        dto.setParentForecastId("val-parentForecastId");
        dto.setTotalBestCase(BigDecimal.ONE);
        dto.setTotalLikely(BigDecimal.ONE);
        dto.setTotalWorstCase(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setRegion("val-region");
        dto.setTerritory("val-territory");
        dto.setBusinessUnit("val-businessUnit");
        dto.setCurrentApprovalLevel(ForecastResponseDto.ApprovalLevelDto.NONE);
        dto.setRejectionReason("val-rejectionReason");
        dto.setLocked(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(ForecastResponseDto.ForecastPeriodDto.MONTHLY, dto.getPeriod());
        assertEquals(ForecastResponseDto.ForecastStatusDto.DRAFT, dto.getStatus());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals(99, dto.getVersion());
        assertEquals("val-parentForecastId", dto.getParentForecastId());
        assertEquals(BigDecimal.ONE, dto.getTotalBestCase());
        assertEquals(BigDecimal.ONE, dto.getTotalLikely());
        assertEquals(BigDecimal.ONE, dto.getTotalWorstCase());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-territory", dto.getTerritory());
        assertEquals("val-businessUnit", dto.getBusinessUnit());
        assertEquals(ForecastResponseDto.ApprovalLevelDto.NONE, dto.getCurrentApprovalLevel());
        assertEquals("val-rejectionReason", dto.getRejectionReason());
        assertTrue(dto.getLocked());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastResponseDto dto1 = ForecastResponseDto.builder()
                        .id("test-id")
            .forecastId("test-forecastId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .period(ForecastResponseDto.ForecastPeriodDto.MONTHLY)
            .startDate(null)
            .endDate(null)
            .status(ForecastResponseDto.ForecastStatusDto.DRAFT)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .version(42)
            .parentForecastId("test-parentForecastId")
            .totalBestCase(BigDecimal.TEN)
            .totalLikely(BigDecimal.TEN)
            .totalWorstCase(BigDecimal.TEN)
            .currency("test-currency")
            .region("test-region")
            .territory("test-territory")
            .businessUnit("test-businessUnit")
            .lineItems(Collections.emptyList())
            .currentApprovalLevel(ForecastResponseDto.ApprovalLevelDto.NONE)
            .rejectionReason("test-rejectionReason")
            .locked(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ForecastResponseDto dto2 = ForecastResponseDto.builder()
                        .id("test-id")
            .forecastId("test-forecastId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .period(ForecastResponseDto.ForecastPeriodDto.MONTHLY)
            .startDate(null)
            .endDate(null)
            .status(ForecastResponseDto.ForecastStatusDto.DRAFT)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .version(42)
            .parentForecastId("test-parentForecastId")
            .totalBestCase(BigDecimal.TEN)
            .totalLikely(BigDecimal.TEN)
            .totalWorstCase(BigDecimal.TEN)
            .currency("test-currency")
            .region("test-region")
            .territory("test-territory")
            .businessUnit("test-businessUnit")
            .lineItems(Collections.emptyList())
            .currentApprovalLevel(ForecastResponseDto.ApprovalLevelDto.NONE)
            .rejectionReason("test-rejectionReason")
            .locked(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ForecastResponseDto dto = ForecastResponseDto.builder()
                        .id("test-id")
            .forecastId("test-forecastId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .period(ForecastResponseDto.ForecastPeriodDto.MONTHLY)
            .startDate(null)
            .endDate(null)
            .status(ForecastResponseDto.ForecastStatusDto.DRAFT)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .version(42)
            .parentForecastId("test-parentForecastId")
            .totalBestCase(BigDecimal.TEN)
            .totalLikely(BigDecimal.TEN)
            .totalWorstCase(BigDecimal.TEN)
            .currency("test-currency")
            .region("test-region")
            .territory("test-territory")
            .businessUnit("test-businessUnit")
            .lineItems(Collections.emptyList())
            .currentApprovalLevel(ForecastResponseDto.ApprovalLevelDto.NONE)
            .rejectionReason("test-rejectionReason")
            .locked(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}