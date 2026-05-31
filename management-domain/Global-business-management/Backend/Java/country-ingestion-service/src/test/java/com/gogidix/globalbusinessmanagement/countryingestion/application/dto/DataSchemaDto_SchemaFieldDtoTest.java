package com.gogidix.globalbusinessmanagement.countryingestion.application.dto;

import com.gogidix.globalbusinessmanagement.countryingestion.application.dto.DataSchemaDto;
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
class DataSchemaDto_SchemaFieldDtoTest {

        @Test
    void testBuilder() {
        DataSchemaDto.SchemaFieldDto dto = DataSchemaDto.SchemaFieldDto.builder()
                        .name("test-name")
            .displayName("test-displayName")
            .type(null)
            .required(true)
            .unique(true)
            .minLength(42)
            .maxLength(42)
            .pattern("test-pattern")
            .minValue(null)
            .maxValue(null)
            .defaultValue("test-defaultValue")
            .allowedValues(Collections.emptyList())
            .description("test-description")
            .order(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-displayName", dto.getDisplayName());
        assertTrue(dto.isRequired());
        assertTrue(dto.isUnique());
        assertEquals(42, dto.getMinLength());
        assertEquals(42, dto.getMaxLength());
        assertEquals("test-pattern", dto.getPattern());
        assertEquals("test-defaultValue", dto.getDefaultValue());
        assertEquals("test-description", dto.getDescription());
        assertEquals(42, dto.getOrder());
    }

    @Test
    void testSettersAndGetters() {
        DataSchemaDto.SchemaFieldDto dto = new DataSchemaDto.SchemaFieldDto();
        dto.setName("val-name");
        dto.setDisplayName("val-displayName");
        dto.setRequired(true);
        dto.setUnique(true);
        dto.setMinLength(99);
        dto.setMaxLength(99);
        dto.setPattern("val-pattern");
        dto.setDefaultValue("val-defaultValue");
        dto.setDescription("val-description");
        dto.setOrder(99);
        assertEquals("val-name", dto.getName());
        assertEquals("val-displayName", dto.getDisplayName());
        assertTrue(dto.isRequired());
        assertTrue(dto.isUnique());
        assertEquals(99, dto.getMinLength());
        assertEquals(99, dto.getMaxLength());
        assertEquals("val-pattern", dto.getPattern());
        assertEquals("val-defaultValue", dto.getDefaultValue());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getOrder());
    }

    @Test
    void testEqualsAndHashCode() {
        DataSchemaDto.SchemaFieldDto dto1 = DataSchemaDto.SchemaFieldDto.builder()
                        .name("test-name")
            .displayName("test-displayName")
            .type(null)
            .required(true)
            .unique(true)
            .minLength(42)
            .maxLength(42)
            .pattern("test-pattern")
            .minValue(null)
            .maxValue(null)
            .defaultValue("test-defaultValue")
            .allowedValues(Collections.emptyList())
            .description("test-description")
            .order(42)
            .build();
        DataSchemaDto.SchemaFieldDto dto2 = DataSchemaDto.SchemaFieldDto.builder()
                        .name("test-name")
            .displayName("test-displayName")
            .type(null)
            .required(true)
            .unique(true)
            .minLength(42)
            .maxLength(42)
            .pattern("test-pattern")
            .minValue(null)
            .maxValue(null)
            .defaultValue("test-defaultValue")
            .allowedValues(Collections.emptyList())
            .description("test-description")
            .order(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DataSchemaDto.SchemaFieldDto dto = DataSchemaDto.SchemaFieldDto.builder()
                        .name("test-name")
            .displayName("test-displayName")
            .type(null)
            .required(true)
            .unique(true)
            .minLength(42)
            .maxLength(42)
            .pattern("test-pattern")
            .minValue(null)
            .maxValue(null)
            .defaultValue("test-defaultValue")
            .allowedValues(Collections.emptyList())
            .description("test-description")
            .order(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}