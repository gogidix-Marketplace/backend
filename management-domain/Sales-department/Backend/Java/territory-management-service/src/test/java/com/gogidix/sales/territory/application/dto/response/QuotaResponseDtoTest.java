package com.gogidix.sales.territory.application.dto.response;

import com.gogidix.sales.territory.application.dto.response.QuotaResponseDto;
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
class QuotaResponseDtoTest {

        @Test
    void testBuilder() {
        QuotaResponseDto dto = QuotaResponseDto.builder()
                        .id("test-id")
            .quotaId("test-quotaId")
            .tenantId("test-tenantId")
            .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .salesRepresentativeId("test-salesRepresentativeId")
            .type(QuotaResponseDto.QuotaTypeDto.REVENUE)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .period(QuotaResponseDto.QuotaPeriodDto.DAILY)
            .year(42)
            .month(42)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .breakdown(Collections.emptyList())
            .status(QuotaResponseDto.QuotaStatusDto.DRAFT)
            .currentAchievement(BigDecimal.TEN)
            .attainmentPercentage(BigDecimal.TEN)
            .lastCalculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-quotaId", dto.getQuotaId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-territoryId", dto.getTerritoryId());
        assertEquals("test-territoryName", dto.getTerritoryName());
        assertEquals("test-salesRepresentativeId", dto.getSalesRepresentativeId());
        assertEquals(QuotaResponseDto.QuotaTypeDto.REVENUE, dto.getType());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(QuotaResponseDto.QuotaPeriodDto.DAILY, dto.getPeriod());
        assertEquals(42, dto.getYear());
        assertEquals(42, dto.getMonth());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals(QuotaResponseDto.QuotaStatusDto.DRAFT, dto.getStatus());
        assertEquals(BigDecimal.TEN, dto.getCurrentAchievement());
        assertEquals(BigDecimal.TEN, dto.getAttainmentPercentage());
        assertEquals("test-approvedBy", dto.getApprovedBy());
    }

    @Test
    void testSettersAndGetters() {
        QuotaResponseDto dto = new QuotaResponseDto();
        dto.setId("val-id");
        dto.setQuotaId("val-quotaId");
        dto.setTenantId("val-tenantId");
        dto.setTerritoryId("val-territoryId");
        dto.setTerritoryName("val-territoryName");
        dto.setSalesRepresentativeId("val-salesRepresentativeId");
        dto.setType(QuotaResponseDto.QuotaTypeDto.REVENUE);
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setPeriod(QuotaResponseDto.QuotaPeriodDto.DAILY);
        dto.setYear(99);
        dto.setMonth(99);
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setStatus(QuotaResponseDto.QuotaStatusDto.DRAFT);
        dto.setCurrentAchievement(BigDecimal.ONE);
        dto.setAttainmentPercentage(BigDecimal.ONE);
        dto.setApprovedBy("val-approvedBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-quotaId", dto.getQuotaId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-territoryId", dto.getTerritoryId());
        assertEquals("val-territoryName", dto.getTerritoryName());
        assertEquals("val-salesRepresentativeId", dto.getSalesRepresentativeId());
        assertEquals(QuotaResponseDto.QuotaTypeDto.REVENUE, dto.getType());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(QuotaResponseDto.QuotaPeriodDto.DAILY, dto.getPeriod());
        assertEquals(99, dto.getYear());
        assertEquals(99, dto.getMonth());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(QuotaResponseDto.QuotaStatusDto.DRAFT, dto.getStatus());
        assertEquals(BigDecimal.ONE, dto.getCurrentAchievement());
        assertEquals(BigDecimal.ONE, dto.getAttainmentPercentage());
        assertEquals("val-approvedBy", dto.getApprovedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        QuotaResponseDto dto1 = QuotaResponseDto.builder()
                        .id("test-id")
            .quotaId("test-quotaId")
            .tenantId("test-tenantId")
            .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .salesRepresentativeId("test-salesRepresentativeId")
            .type(QuotaResponseDto.QuotaTypeDto.REVENUE)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .period(QuotaResponseDto.QuotaPeriodDto.DAILY)
            .year(42)
            .month(42)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .breakdown(Collections.emptyList())
            .status(QuotaResponseDto.QuotaStatusDto.DRAFT)
            .currentAchievement(BigDecimal.TEN)
            .attainmentPercentage(BigDecimal.TEN)
            .lastCalculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        QuotaResponseDto dto2 = QuotaResponseDto.builder()
                        .id("test-id")
            .quotaId("test-quotaId")
            .tenantId("test-tenantId")
            .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .salesRepresentativeId("test-salesRepresentativeId")
            .type(QuotaResponseDto.QuotaTypeDto.REVENUE)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .period(QuotaResponseDto.QuotaPeriodDto.DAILY)
            .year(42)
            .month(42)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .breakdown(Collections.emptyList())
            .status(QuotaResponseDto.QuotaStatusDto.DRAFT)
            .currentAchievement(BigDecimal.TEN)
            .attainmentPercentage(BigDecimal.TEN)
            .lastCalculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        QuotaResponseDto dto = QuotaResponseDto.builder()
                        .id("test-id")
            .quotaId("test-quotaId")
            .tenantId("test-tenantId")
            .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .salesRepresentativeId("test-salesRepresentativeId")
            .type(QuotaResponseDto.QuotaTypeDto.REVENUE)
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .period(QuotaResponseDto.QuotaPeriodDto.DAILY)
            .year(42)
            .month(42)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .breakdown(Collections.emptyList())
            .status(QuotaResponseDto.QuotaStatusDto.DRAFT)
            .currentAchievement(BigDecimal.TEN)
            .attainmentPercentage(BigDecimal.TEN)
            .lastCalculatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}