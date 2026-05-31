package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.CaseStudyDto;
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
class CaseStudyDto_MetricDtoTest {

        @Test
    void testBuilder() {
        CaseStudyDto.MetricDto dto = CaseStudyDto.MetricDto.builder()
                        .label("test-label")
            .value("test-value")
            .unit("test-unit")
            .description("test-description")
            .build();
        assertNotNull(dto);
        assertEquals("test-label", dto.getLabel());
        assertEquals("test-value", dto.getValue());
        assertEquals("test-unit", dto.getUnit());
        assertEquals("test-description", dto.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        CaseStudyDto.MetricDto dto = new CaseStudyDto.MetricDto();
        dto.setLabel("val-label");
        dto.setValue("val-value");
        dto.setUnit("val-unit");
        dto.setDescription("val-description");
        assertEquals("val-label", dto.getLabel());
        assertEquals("val-value", dto.getValue());
        assertEquals("val-unit", dto.getUnit());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        CaseStudyDto.MetricDto dto1 = CaseStudyDto.MetricDto.builder()
                        .label("test-label")
            .value("test-value")
            .unit("test-unit")
            .description("test-description")
            .build();
        CaseStudyDto.MetricDto dto2 = CaseStudyDto.MetricDto.builder()
                        .label("test-label")
            .value("test-value")
            .unit("test-unit")
            .description("test-description")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CaseStudyDto.MetricDto dto = CaseStudyDto.MetricDto.builder()
                        .label("test-label")
            .value("test-value")
            .unit("test-unit")
            .description("test-description")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}