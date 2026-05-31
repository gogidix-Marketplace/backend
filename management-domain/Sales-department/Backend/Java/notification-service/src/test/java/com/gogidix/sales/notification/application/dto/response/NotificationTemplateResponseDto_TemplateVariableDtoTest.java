package com.gogidix.sales.notification.application.dto.response;

import com.gogidix.sales.notification.application.dto.response.NotificationTemplateResponseDto;
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
class NotificationTemplateResponseDto_TemplateVariableDtoTest {

        @Test
    void testBuilder() {
        NotificationTemplateResponseDto.TemplateVariableDto dto = NotificationTemplateResponseDto.TemplateVariableDto.builder()
                        .name("test-name")
            .type("test-type")
            .description("test-description")
            .required(true)
            .defaultValue("test-defaultValue")
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-type", dto.getType());
        assertEquals("test-description", dto.getDescription());
        assertTrue(dto.getRequired());
        assertEquals("test-defaultValue", dto.getDefaultValue());
    }

    @Test
    void testSettersAndGetters() {
        NotificationTemplateResponseDto.TemplateVariableDto dto = new NotificationTemplateResponseDto.TemplateVariableDto();
        dto.setName("val-name");
        dto.setType("val-type");
        dto.setDescription("val-description");
        dto.setRequired(true);
        dto.setDefaultValue("val-defaultValue");
        assertEquals("val-name", dto.getName());
        assertEquals("val-type", dto.getType());
        assertEquals("val-description", dto.getDescription());
        assertTrue(dto.getRequired());
        assertEquals("val-defaultValue", dto.getDefaultValue());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationTemplateResponseDto.TemplateVariableDto dto1 = NotificationTemplateResponseDto.TemplateVariableDto.builder()
                        .name("test-name")
            .type("test-type")
            .description("test-description")
            .required(true)
            .defaultValue("test-defaultValue")
            .build();
        NotificationTemplateResponseDto.TemplateVariableDto dto2 = NotificationTemplateResponseDto.TemplateVariableDto.builder()
                        .name("test-name")
            .type("test-type")
            .description("test-description")
            .required(true)
            .defaultValue("test-defaultValue")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NotificationTemplateResponseDto.TemplateVariableDto dto = NotificationTemplateResponseDto.TemplateVariableDto.builder()
                        .name("test-name")
            .type("test-type")
            .description("test-description")
            .required(true)
            .defaultValue("test-defaultValue")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}