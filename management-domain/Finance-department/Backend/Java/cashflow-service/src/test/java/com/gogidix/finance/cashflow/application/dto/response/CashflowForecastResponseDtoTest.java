package com.gogidix.finance.cashflow.application.dto.response;

import com.gogidix.finance.cashflow.application.dto.response.CashflowForecastResponseDto;
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
class CashflowForecastResponseDtoTest {

        @Test
    void testBuilder() {
        CashflowForecastResponseDto dto = CashflowForecastResponseDto.builder()
                        .id("test-id")
            .forecastId("test-forecastId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .period(CashflowForecastResponseDto.ForecastPeriodDto.DAILY)
            .scenario(CashflowForecastResponseDto.ForecastScenarioDto.BASELINE)
            .status(CashflowForecastResponseDto.ForecastStatusDto.DRAFT)
            .generatedBy("test-generatedBy")
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .closingBalance(BigDecimal.TEN)
            .minimumBalance(BigDecimal.TEN)
            .maximumBalance(BigDecimal.TEN)
            .minimumBalanceDate(LocalDate.of(2025,1,15))
            .maximumBalanceDate(LocalDate.of(2025,1,15))
            .version(42)
            .parentForecastId("test-parentForecastId")
            .isBaseline(true)
            .periodData(Collections.emptyList())
            .variances(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .confidenceLevel(CashflowForecastResponseDto.ConfidenceLevelDto.LOW)
            .variancePercentage(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-forecastId", dto.getForecastId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals(CashflowForecastResponseDto.ForecastPeriodDto.DAILY, dto.getPeriod());
        assertEquals(CashflowForecastResponseDto.ForecastScenarioDto.BASELINE, dto.getScenario());
        assertEquals(CashflowForecastResponseDto.ForecastStatusDto.DRAFT, dto.getStatus());
        assertEquals("test-generatedBy", dto.getGeneratedBy());
        assertEquals(BigDecimal.TEN, dto.getTotalInflow());
        assertEquals(BigDecimal.TEN, dto.getTotalOutflow());
        assertEquals(BigDecimal.TEN, dto.getNetCashflow());
        assertEquals(BigDecimal.TEN, dto.getOpeningBalance());
        assertEquals(BigDecimal.TEN, dto.getClosingBalance());
        assertEquals(BigDecimal.TEN, dto.getMinimumBalance());
        assertEquals(BigDecimal.TEN, dto.getMaximumBalance());
        assertEquals(LocalDate.of(2025,1,15), dto.getMinimumBalanceDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getMaximumBalanceDate());
        assertEquals(42, dto.getVersion());
        assertEquals("test-parentForecastId", dto.getParentForecastId());
        assertTrue(dto.getIsBaseline());
        assertEquals("test-notes", dto.getNotes());
        assertEquals(CashflowForecastResponseDto.ConfidenceLevelDto.LOW, dto.getConfidenceLevel());
        assertEquals(BigDecimal.TEN, dto.getVariancePercentage());
    }

    @Test
    void testSettersAndGetters() {
        CashflowForecastResponseDto dto = new CashflowForecastResponseDto();
        dto.setId("val-id");
        dto.setForecastId("val-forecastId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPeriod(CashflowForecastResponseDto.ForecastPeriodDto.DAILY);
        dto.setScenario(CashflowForecastResponseDto.ForecastScenarioDto.BASELINE);
        dto.setStatus(CashflowForecastResponseDto.ForecastStatusDto.DRAFT);
        dto.setGeneratedBy("val-generatedBy");
        dto.setTotalInflow(BigDecimal.ONE);
        dto.setTotalOutflow(BigDecimal.ONE);
        dto.setNetCashflow(BigDecimal.ONE);
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setClosingBalance(BigDecimal.ONE);
        dto.setMinimumBalance(BigDecimal.ONE);
        dto.setMaximumBalance(BigDecimal.ONE);
        dto.setMinimumBalanceDate(LocalDate.of(2025,6,1));
        dto.setMaximumBalanceDate(LocalDate.of(2025,6,1));
        dto.setVersion(99);
        dto.setParentForecastId("val-parentForecastId");
        dto.setIsBaseline(true);
        dto.setNotes("val-notes");
        dto.setConfidenceLevel(CashflowForecastResponseDto.ConfidenceLevelDto.LOW);
        dto.setVariancePercentage(BigDecimal.ONE);
        assertEquals("val-id", dto.getId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(CashflowForecastResponseDto.ForecastPeriodDto.DAILY, dto.getPeriod());
        assertEquals(CashflowForecastResponseDto.ForecastScenarioDto.BASELINE, dto.getScenario());
        assertEquals(CashflowForecastResponseDto.ForecastStatusDto.DRAFT, dto.getStatus());
        assertEquals("val-generatedBy", dto.getGeneratedBy());
        assertEquals(BigDecimal.ONE, dto.getTotalInflow());
        assertEquals(BigDecimal.ONE, dto.getTotalOutflow());
        assertEquals(BigDecimal.ONE, dto.getNetCashflow());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals(BigDecimal.ONE, dto.getClosingBalance());
        assertEquals(BigDecimal.ONE, dto.getMinimumBalance());
        assertEquals(BigDecimal.ONE, dto.getMaximumBalance());
        assertEquals(LocalDate.of(2025,6,1), dto.getMinimumBalanceDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getMaximumBalanceDate());
        assertEquals(99, dto.getVersion());
        assertEquals("val-parentForecastId", dto.getParentForecastId());
        assertTrue(dto.getIsBaseline());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(CashflowForecastResponseDto.ConfidenceLevelDto.LOW, dto.getConfidenceLevel());
        assertEquals(BigDecimal.ONE, dto.getVariancePercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastResponseDto dto1 = CashflowForecastResponseDto.builder()
                        .id("test-id")
            .forecastId("test-forecastId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .period(CashflowForecastResponseDto.ForecastPeriodDto.DAILY)
            .scenario(CashflowForecastResponseDto.ForecastScenarioDto.BASELINE)
            .status(CashflowForecastResponseDto.ForecastStatusDto.DRAFT)
            .generatedBy("test-generatedBy")
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .closingBalance(BigDecimal.TEN)
            .minimumBalance(BigDecimal.TEN)
            .maximumBalance(BigDecimal.TEN)
            .minimumBalanceDate(LocalDate.of(2025,1,15))
            .maximumBalanceDate(LocalDate.of(2025,1,15))
            .version(42)
            .parentForecastId("test-parentForecastId")
            .isBaseline(true)
            .periodData(Collections.emptyList())
            .variances(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .confidenceLevel(CashflowForecastResponseDto.ConfidenceLevelDto.LOW)
            .variancePercentage(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CashflowForecastResponseDto dto2 = CashflowForecastResponseDto.builder()
                        .id("test-id")
            .forecastId("test-forecastId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .period(CashflowForecastResponseDto.ForecastPeriodDto.DAILY)
            .scenario(CashflowForecastResponseDto.ForecastScenarioDto.BASELINE)
            .status(CashflowForecastResponseDto.ForecastStatusDto.DRAFT)
            .generatedBy("test-generatedBy")
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .closingBalance(BigDecimal.TEN)
            .minimumBalance(BigDecimal.TEN)
            .maximumBalance(BigDecimal.TEN)
            .minimumBalanceDate(LocalDate.of(2025,1,15))
            .maximumBalanceDate(LocalDate.of(2025,1,15))
            .version(42)
            .parentForecastId("test-parentForecastId")
            .isBaseline(true)
            .periodData(Collections.emptyList())
            .variances(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .confidenceLevel(CashflowForecastResponseDto.ConfidenceLevelDto.LOW)
            .variancePercentage(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CashflowForecastResponseDto dto = CashflowForecastResponseDto.builder()
                        .id("test-id")
            .forecastId("test-forecastId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .period(CashflowForecastResponseDto.ForecastPeriodDto.DAILY)
            .scenario(CashflowForecastResponseDto.ForecastScenarioDto.BASELINE)
            .status(CashflowForecastResponseDto.ForecastStatusDto.DRAFT)
            .generatedBy("test-generatedBy")
            .generatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .totalInflow(BigDecimal.TEN)
            .totalOutflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .openingBalance(BigDecimal.TEN)
            .closingBalance(BigDecimal.TEN)
            .minimumBalance(BigDecimal.TEN)
            .maximumBalance(BigDecimal.TEN)
            .minimumBalanceDate(LocalDate.of(2025,1,15))
            .maximumBalanceDate(LocalDate.of(2025,1,15))
            .version(42)
            .parentForecastId("test-parentForecastId")
            .isBaseline(true)
            .periodData(Collections.emptyList())
            .variances(Collections.emptyList())
            .tags(Collections.emptyList())
            .notes("test-notes")
            .confidenceLevel(CashflowForecastResponseDto.ConfidenceLevelDto.LOW)
            .variancePercentage(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}