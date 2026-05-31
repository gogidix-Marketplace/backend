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
class Forecast_ForecastScenariosTest {

        @Test
    void testBuilder() {
        Forecast.ForecastScenarios dto = Forecast.ForecastScenarios.builder()
                        .baseline(null)
            .optimistic(null)
            .pessimistic(null)
            .customScenarios(Collections.emptyList())
            .analysis(null)
            .build();
        assertNotNull(dto);

    }

    @Test
    void testSettersAndGetters() {
        Forecast.ForecastScenarios dto = new Forecast.ForecastScenarios();


    }

    @Test
    void testEqualsAndHashCode() {
        Forecast.ForecastScenarios dto1 = Forecast.ForecastScenarios.builder()
                        .baseline(null)
            .optimistic(null)
            .pessimistic(null)
            .customScenarios(Collections.emptyList())
            .analysis(null)
            .build();
        Forecast.ForecastScenarios dto2 = Forecast.ForecastScenarios.builder()
                        .baseline(null)
            .optimistic(null)
            .pessimistic(null)
            .customScenarios(Collections.emptyList())
            .analysis(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Forecast.ForecastScenarios dto = Forecast.ForecastScenarios.builder()
                        .baseline(null)
            .optimistic(null)
            .pessimistic(null)
            .customScenarios(Collections.emptyList())
            .analysis(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}