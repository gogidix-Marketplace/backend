package com.gogidix.sales.communication.application.dto.response;

import com.gogidix.sales.communication.application.dto.response.MessageTemplateResponseDto;
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
class MessageTemplateResponseDto_TemplateVariableDtoTest {

        @Test
    void testBuilder() {
        MessageTemplateResponseDto.TemplateVariableDto dto = MessageTemplateResponseDto.TemplateVariableDto.builder()
                        .name("test-name")
            .description("test-description")
            .type(null)
            .isRequired(true)
            .defaultValue("test-defaultValue")
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertTrue(dto.getIsRequired());
        assertEquals("test-defaultValue", dto.getDefaultValue());
    }

    @Test
    void testSettersAndGetters() {
        MessageTemplateResponseDto.TemplateVariableDto dto = new MessageTemplateResponseDto.TemplateVariableDto();
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setIsRequired(true);
        dto.setDefaultValue("val-defaultValue");
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertTrue(dto.getIsRequired());
        assertEquals("val-defaultValue", dto.getDefaultValue());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageTemplateResponseDto.TemplateVariableDto dto1 = MessageTemplateResponseDto.TemplateVariableDto.builder()
                        .name("test-name")
            .description("test-description")
            .type(null)
            .isRequired(true)
            .defaultValue("test-defaultValue")
            .build();
        MessageTemplateResponseDto.TemplateVariableDto dto2 = MessageTemplateResponseDto.TemplateVariableDto.builder()
                        .name("test-name")
            .description("test-description")
            .type(null)
            .isRequired(true)
            .defaultValue("test-defaultValue")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MessageTemplateResponseDto.TemplateVariableDto dto = MessageTemplateResponseDto.TemplateVariableDto.builder()
                        .name("test-name")
            .description("test-description")
            .type(null)
            .isRequired(true)
            .defaultValue("test-defaultValue")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}