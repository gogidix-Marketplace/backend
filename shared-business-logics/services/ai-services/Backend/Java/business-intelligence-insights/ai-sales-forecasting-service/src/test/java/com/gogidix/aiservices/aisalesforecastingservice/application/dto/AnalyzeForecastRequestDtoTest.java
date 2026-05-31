package com.gogidix.aiservices.aisalesforecastingservice.application.dto;

import org.junit.jupiter.api.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class AnalyzeForecastRequestDtoTest {

    @Test
    void shouldCreateWithNullOptions() {
        var dto = new AnalyzeForecastRequestDto(null);
        assertThat(dto.analysisOptions()).isEmpty();
    }

    @Test
    void shouldCreateWithOptions() {
        var dto = new AnalyzeForecastRequestDto(Map.of("key", "value"));
        assertThat(dto.analysisOptions()).containsEntry("key", "value");
    }

    @Test
    void shouldCreateEmpty() {
        var dto = AnalyzeForecastRequestDto.create();
        assertThat(dto.analysisOptions()).isEmpty();
    }
}
