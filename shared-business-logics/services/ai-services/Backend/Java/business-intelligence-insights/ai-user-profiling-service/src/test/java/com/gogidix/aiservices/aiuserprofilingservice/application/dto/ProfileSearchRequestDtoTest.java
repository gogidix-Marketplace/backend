package com.gogidix.aiservices.aiuserprofilingservice.application.dto;

import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileType;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ProfileSearchRequestDtoTest {
    @Test
    void shouldApplyDefaults() {
        var dto = new ProfileSearchRequestDto(null, null, null, null, null, null);
        assertThat(dto.page()).isEqualTo(0);
        assertThat(dto.size()).isEqualTo(20);
    }
    @Test
    void shouldUseProvidedValues() {
        var dto = new ProfileSearchRequestDto(ProfileType.BEHAVIORAL, true, 2, 50, "name", "ASC");
        assertThat(dto.segmentType()).isEqualTo(ProfileType.BEHAVIORAL);
    }
    @Test
    void shouldCreateStatic() {
        assertThat(ProfileSearchRequestDto.create().page()).isEqualTo(0);
    }
    @Test
    void shouldHandleBlankSort() {
        var dto = new ProfileSearchRequestDto(null, null, 0, 20, "  ", "  ");
        assertThat(dto.sortBy()).isEqualTo("createdAt");
    }
}
