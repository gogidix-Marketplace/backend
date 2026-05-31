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
class Forecast_AssumptionTest {

        @Test
    void testBuilder() {
        Forecast.Assumption dto = Forecast.Assumption.builder()
                        .name("test-name")
            .description("test-description")
            .value(BigDecimal.TEN)
            .valueType("test-valueType")
            .category("test-category")
            .sensitivity(BigDecimal.TEN)
            .impactLevel("test-impactLevel")
            .isCritical(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getValue());
        assertEquals("test-valueType", dto.getValueType());
        assertEquals("test-category", dto.getCategory());
        assertEquals(BigDecimal.TEN, dto.getSensitivity());
        assertEquals("test-impactLevel", dto.getImpactLevel());
        assertTrue(dto.getIsCritical());
    }

    @Test
    void testSettersAndGetters() {
        Forecast.Assumption dto = new Forecast.Assumption();
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setValue(BigDecimal.ONE);
        dto.setValueType("val-valueType");
        dto.setCategory("val-category");
        dto.setSensitivity(BigDecimal.ONE);
        dto.setImpactLevel("val-impactLevel");
        dto.setIsCritical(true);
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getValue());
        assertEquals("val-valueType", dto.getValueType());
        assertEquals("val-category", dto.getCategory());
        assertEquals(BigDecimal.ONE, dto.getSensitivity());
        assertEquals("val-impactLevel", dto.getImpactLevel());
        assertTrue(dto.getIsCritical());
    }

    @Test
    void testEqualsAndHashCode() {
        Forecast.Assumption dto1 = Forecast.Assumption.builder()
                        .name("test-name")
            .description("test-description")
            .value(BigDecimal.TEN)
            .valueType("test-valueType")
            .category("test-category")
            .sensitivity(BigDecimal.TEN)
            .impactLevel("test-impactLevel")
            .isCritical(true)
            .build();
        Forecast.Assumption dto2 = Forecast.Assumption.builder()
                        .name("test-name")
            .description("test-description")
            .value(BigDecimal.TEN)
            .valueType("test-valueType")
            .category("test-category")
            .sensitivity(BigDecimal.TEN)
            .impactLevel("test-impactLevel")
            .isCritical(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Forecast.Assumption dto = Forecast.Assumption.builder()
                        .name("test-name")
            .description("test-description")
            .value(BigDecimal.TEN)
            .valueType("test-valueType")
            .category("test-category")
            .sensitivity(BigDecimal.TEN)
            .impactLevel("test-impactLevel")
            .isCritical(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}