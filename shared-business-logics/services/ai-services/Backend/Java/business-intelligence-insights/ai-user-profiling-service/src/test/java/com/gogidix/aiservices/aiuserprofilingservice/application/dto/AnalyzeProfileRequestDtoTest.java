package com.gogidix.aiservices.aiuserprofilingservice.application.dto;

import org.junit.jupiter.api.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class AnalyzeProfileRequestDtoTest {
    @Test
    void shouldCreateWithNullOptions() {
        var dto = new AnalyzeProfileRequestDto(null);
        assertThat(dto.analysisOptions()).isEmpty();
    }
    @Test
    void shouldCreateWithOptions() {
        var dto = new AnalyzeProfileRequestDto(Map.of("key", "value"));
        assertThat(dto.analysisOptions()).containsEntry("key", "value");
    }
    @Test
    void shouldCreateEmpty() {
        assertThat(AnalyzeProfileRequestDto.create().analysisOptions()).isEmpty();
    }
}
