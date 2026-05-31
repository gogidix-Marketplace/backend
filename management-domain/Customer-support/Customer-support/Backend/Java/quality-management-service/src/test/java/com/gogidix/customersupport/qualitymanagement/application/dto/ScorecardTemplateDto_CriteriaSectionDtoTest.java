package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.gogidix.customersupport.qualitymanagement.application.dto.ScorecardTemplateDto;
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
class ScorecardTemplateDto_CriteriaSectionDtoTest {

        @Test
    void testBuilder() {
        ScorecardTemplateDto.CriteriaSectionDto dto = ScorecardTemplateDto.CriteriaSectionDto.builder()
                        .sectionId("test-sectionId")
            .sectionName("test-sectionName")
            .description("test-description")
            .order(42)
            .weight(null)
            .maxScore(null)
            .criteria(Collections.emptyList())
            .isRequired(true)
            .instructions("test-instructions")
            .build();
        assertNotNull(dto);
        assertEquals("test-sectionId", dto.getSectionId());
        assertEquals("test-sectionName", dto.getSectionName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(42, dto.getOrder());
        assertTrue(dto.getIsRequired());
        assertEquals("test-instructions", dto.getInstructions());
    }

    @Test
    void testSettersAndGetters() {
        ScorecardTemplateDto.CriteriaSectionDto dto = new ScorecardTemplateDto.CriteriaSectionDto();
        dto.setSectionId("val-sectionId");
        dto.setSectionName("val-sectionName");
        dto.setDescription("val-description");
        dto.setOrder(99);
        dto.setIsRequired(true);
        dto.setInstructions("val-instructions");
        assertEquals("val-sectionId", dto.getSectionId());
        assertEquals("val-sectionName", dto.getSectionName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getOrder());
        assertTrue(dto.getIsRequired());
        assertEquals("val-instructions", dto.getInstructions());
    }

    @Test
    void testEqualsAndHashCode() {
        ScorecardTemplateDto.CriteriaSectionDto dto1 = ScorecardTemplateDto.CriteriaSectionDto.builder()
                        .sectionId("test-sectionId")
            .sectionName("test-sectionName")
            .description("test-description")
            .order(42)
            .weight(null)
            .maxScore(null)
            .criteria(Collections.emptyList())
            .isRequired(true)
            .instructions("test-instructions")
            .build();
        ScorecardTemplateDto.CriteriaSectionDto dto2 = ScorecardTemplateDto.CriteriaSectionDto.builder()
                        .sectionId("test-sectionId")
            .sectionName("test-sectionName")
            .description("test-description")
            .order(42)
            .weight(null)
            .maxScore(null)
            .criteria(Collections.emptyList())
            .isRequired(true)
            .instructions("test-instructions")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ScorecardTemplateDto.CriteriaSectionDto dto = ScorecardTemplateDto.CriteriaSectionDto.builder()
                        .sectionId("test-sectionId")
            .sectionName("test-sectionName")
            .description("test-description")
            .order(42)
            .weight(null)
            .maxScore(null)
            .criteria(Collections.emptyList())
            .isRequired(true)
            .instructions("test-instructions")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}