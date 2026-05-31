package com.gogidix.finance.forecasting.application.dto.response;

import com.gogidix.finance.forecasting.application.dto.response.ForecastResponseDto;
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
            .forecastType(ForecastResponseDto.ForecastTypeDto.REVENUE)
            .forecastHorizon(ForecastResponseDto.ForecastHorizonDto.MONTHLY)
            .name("test-name")
            .description("test-description")
            .startDate(Instant.parse("2025-01-15T10:00:00Z"))
            .endDate(Instant.parse("2025-01-15T10:00:00Z"))
            .status(ForecastResponseDto.ForecastStatusDto.DRAFT)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .currency("test-currency")
            .totalForecastAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .confidenceLevel(42)
            .dataSource("test-dataSource")
            .department("test-department")
            .category("test-category")
            .scenario("test-scenario")
            .metrics(Collections.emptyList())
            .notes("test-notes")
            .lastRegeneratedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .regenerationCount(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-forecastId", dto.getForecastId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals(ForecastResponseDto.ForecastTypeDto.REVENUE, dto.getForecastType());
        assertEquals(ForecastResponseDto.ForecastHorizonDto.MONTHLY, dto.getForecastHorizon());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(ForecastResponseDto.ForecastStatusDto.DRAFT, dto.getStatus());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals("test-rejectionReason", dto.getRejectionReason());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getTotalForecastAmount());
        assertEquals(BigDecimal.TEN, dto.getActualAmount());
        assertEquals(BigDecimal.TEN, dto.getVarianceAmount());
        assertEquals(BigDecimal.TEN, dto.getVariancePercentage());
        assertEquals(42, dto.getConfidenceLevel());
        assertEquals("test-dataSource", dto.getDataSource());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-scenario", dto.getScenario());
        assertEquals("test-notes", dto.getNotes());
        assertEquals(42, dto.getRegenerationCount());
    }

    @Test
    void testSettersAndGetters() {
        ForecastResponseDto dto = new ForecastResponseDto();
        dto.setId("val-id");
        dto.setForecastId("val-forecastId");
        dto.setTenantId("val-tenantId");
        dto.setForecastType(ForecastResponseDto.ForecastTypeDto.REVENUE);
        dto.setForecastHorizon(ForecastResponseDto.ForecastHorizonDto.MONTHLY);
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setStatus(ForecastResponseDto.ForecastStatusDto.DRAFT);
        dto.setCreatedBy("val-createdBy");
        dto.setApprovedBy("val-approvedBy");
        dto.setRejectionReason("val-rejectionReason");
        dto.setCurrency("val-currency");
        dto.setTotalForecastAmount(BigDecimal.ONE);
        dto.setActualAmount(BigDecimal.ONE);
        dto.setVarianceAmount(BigDecimal.ONE);
        dto.setVariancePercentage(BigDecimal.ONE);
        dto.setConfidenceLevel(99);
        dto.setDataSource("val-dataSource");
        dto.setDepartment("val-department");
        dto.setCategory("val-category");
        dto.setScenario("val-scenario");
        dto.setNotes("val-notes");
        dto.setRegenerationCount(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(ForecastResponseDto.ForecastTypeDto.REVENUE, dto.getForecastType());
        assertEquals(ForecastResponseDto.ForecastHorizonDto.MONTHLY, dto.getForecastHorizon());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(ForecastResponseDto.ForecastStatusDto.DRAFT, dto.getStatus());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals("val-rejectionReason", dto.getRejectionReason());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getTotalForecastAmount());
        assertEquals(BigDecimal.ONE, dto.getActualAmount());
        assertEquals(BigDecimal.ONE, dto.getVarianceAmount());
        assertEquals(BigDecimal.ONE, dto.getVariancePercentage());
        assertEquals(99, dto.getConfidenceLevel());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-scenario", dto.getScenario());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(99, dto.getRegenerationCount());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastResponseDto dto1 = ForecastResponseDto.builder()
                        .id("test-id")
            .forecastId("test-forecastId")
            .tenantId("test-tenantId")
            .forecastType(ForecastResponseDto.ForecastTypeDto.REVENUE)
            .forecastHorizon(ForecastResponseDto.ForecastHorizonDto.MONTHLY)
            .name("test-name")
            .description("test-description")
            .startDate(Instant.parse("2025-01-15T10:00:00Z"))
            .endDate(Instant.parse("2025-01-15T10:00:00Z"))
            .status(ForecastResponseDto.ForecastStatusDto.DRAFT)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .currency("test-currency")
            .totalForecastAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .confidenceLevel(42)
            .dataSource("test-dataSource")
            .department("test-department")
            .category("test-category")
            .scenario("test-scenario")
            .metrics(Collections.emptyList())
            .notes("test-notes")
            .lastRegeneratedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .regenerationCount(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ForecastResponseDto dto2 = ForecastResponseDto.builder()
                        .id("test-id")
            .forecastId("test-forecastId")
            .tenantId("test-tenantId")
            .forecastType(ForecastResponseDto.ForecastTypeDto.REVENUE)
            .forecastHorizon(ForecastResponseDto.ForecastHorizonDto.MONTHLY)
            .name("test-name")
            .description("test-description")
            .startDate(Instant.parse("2025-01-15T10:00:00Z"))
            .endDate(Instant.parse("2025-01-15T10:00:00Z"))
            .status(ForecastResponseDto.ForecastStatusDto.DRAFT)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .currency("test-currency")
            .totalForecastAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .confidenceLevel(42)
            .dataSource("test-dataSource")
            .department("test-department")
            .category("test-category")
            .scenario("test-scenario")
            .metrics(Collections.emptyList())
            .notes("test-notes")
            .lastRegeneratedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .regenerationCount(42)
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
            .forecastType(ForecastResponseDto.ForecastTypeDto.REVENUE)
            .forecastHorizon(ForecastResponseDto.ForecastHorizonDto.MONTHLY)
            .name("test-name")
            .description("test-description")
            .startDate(Instant.parse("2025-01-15T10:00:00Z"))
            .endDate(Instant.parse("2025-01-15T10:00:00Z"))
            .status(ForecastResponseDto.ForecastStatusDto.DRAFT)
            .createdBy("test-createdBy")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .currency("test-currency")
            .totalForecastAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .confidenceLevel(42)
            .dataSource("test-dataSource")
            .department("test-department")
            .category("test-category")
            .scenario("test-scenario")
            .metrics(Collections.emptyList())
            .notes("test-notes")
            .lastRegeneratedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .regenerationCount(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}