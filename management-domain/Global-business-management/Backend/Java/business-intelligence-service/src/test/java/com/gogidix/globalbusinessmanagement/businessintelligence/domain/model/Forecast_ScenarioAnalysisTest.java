package com.gogidix.globalbusinessmanagement.businessintelligence.domain.model;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.Forecast;
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
class Forecast_ScenarioAnalysisTest {

        @Test
    void testBuilder() {
        Forecast.ScenarioAnalysis dto = Forecast.ScenarioAnalysis.builder()
                        .bestCaseValue(BigDecimal.TEN)
            .worstCaseValue(BigDecimal.TEN)
            .expectedValue(BigDecimal.TEN)
            .range(BigDecimal.TEN)
            .variability(BigDecimal.TEN)
            .riskLevel("test-riskLevel")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getBestCaseValue());
        assertEquals(BigDecimal.TEN, dto.getWorstCaseValue());
        assertEquals(BigDecimal.TEN, dto.getExpectedValue());
        assertEquals(BigDecimal.TEN, dto.getRange());
        assertEquals(BigDecimal.TEN, dto.getVariability());
        assertEquals("test-riskLevel", dto.getRiskLevel());
    }

    @Test
    void testSettersAndGetters() {
        Forecast.ScenarioAnalysis dto = new Forecast.ScenarioAnalysis();
        dto.setBestCaseValue(BigDecimal.ONE);
        dto.setWorstCaseValue(BigDecimal.ONE);
        dto.setExpectedValue(BigDecimal.ONE);
        dto.setRange(BigDecimal.ONE);
        dto.setVariability(BigDecimal.ONE);
        dto.setRiskLevel("val-riskLevel");
        assertEquals(BigDecimal.ONE, dto.getBestCaseValue());
        assertEquals(BigDecimal.ONE, dto.getWorstCaseValue());
        assertEquals(BigDecimal.ONE, dto.getExpectedValue());
        assertEquals(BigDecimal.ONE, dto.getRange());
        assertEquals(BigDecimal.ONE, dto.getVariability());
        assertEquals("val-riskLevel", dto.getRiskLevel());
    }

    @Test
    void testEqualsAndHashCode() {
        Forecast.ScenarioAnalysis dto1 = Forecast.ScenarioAnalysis.builder()
                        .bestCaseValue(BigDecimal.TEN)
            .worstCaseValue(BigDecimal.TEN)
            .expectedValue(BigDecimal.TEN)
            .range(BigDecimal.TEN)
            .variability(BigDecimal.TEN)
            .riskLevel("test-riskLevel")
            .build();
        Forecast.ScenarioAnalysis dto2 = Forecast.ScenarioAnalysis.builder()
                        .bestCaseValue(BigDecimal.TEN)
            .worstCaseValue(BigDecimal.TEN)
            .expectedValue(BigDecimal.TEN)
            .range(BigDecimal.TEN)
            .variability(BigDecimal.TEN)
            .riskLevel("test-riskLevel")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Forecast.ScenarioAnalysis dto = Forecast.ScenarioAnalysis.builder()
                        .bestCaseValue(BigDecimal.TEN)
            .worstCaseValue(BigDecimal.TEN)
            .expectedValue(BigDecimal.TEN)
            .range(BigDecimal.TEN)
            .variability(BigDecimal.TEN)
            .riskLevel("test-riskLevel")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}