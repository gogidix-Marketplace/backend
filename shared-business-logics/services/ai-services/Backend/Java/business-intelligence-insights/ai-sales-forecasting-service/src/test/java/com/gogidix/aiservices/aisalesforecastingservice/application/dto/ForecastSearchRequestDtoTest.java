package com.gogidix.aiservices.aisalesforecastingservice.application.dto;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ForecastSearchRequestDtoTest {

    @Test
    void shouldApplyDefaults() {
        var dto = new ForecastSearchRequestDto(null, null, null, null, null, null);
        assertThat(dto.page()).isEqualTo(0);
        assertThat(dto.size()).isEqualTo(20);
        assertThat(dto.sortBy()).isEqualTo("createdAt");
        assertThat(dto.sortDirection()).isEqualTo("DESC");
    }

    @Test
    void shouldUseProvidedValues() {
        var dto = new ForecastSearchRequestDto(ForecastType.DEMOGRAPHIC, true, 2, 50, "name", "ASC");
        assertThat(dto.segmentType()).isEqualTo(ForecastType.DEMOGRAPHIC);
        assertThat(dto.active()).isTrue();
        assertThat(dto.page()).isEqualTo(2);
        assertThat(dto.size()).isEqualTo(50);
        assertThat(dto.sortBy()).isEqualTo("name");
        assertThat(dto.sortDirection()).isEqualTo("ASC");
    }

    @Test
    void shouldCreateStatic() {
        var dto = ForecastSearchRequestDto.create();
        assertThat(dto.page()).isEqualTo(0);
    }

    @Test
    void shouldHandleBlankSort() {
        var dto = new ForecastSearchRequestDto(null, null, 0, 20, "  ", "  ");
        assertThat(dto.sortBy()).isEqualTo("createdAt");
        assertThat(dto.sortDirection()).isEqualTo("DESC");
    }
}
