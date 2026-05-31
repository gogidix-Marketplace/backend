package com.gogidix.sales.communication.domain.model;

import com.gogidix.sales.communication.domain.model.MessageTemplate;
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
class MessageTemplate_TemplateVariableTest {

        @Test
    void testBuilder() {
        MessageTemplate.TemplateVariable dto = MessageTemplate.TemplateVariable.builder()
                        .name("test-name")
            .description("test-description")
            .type(MessageTemplate.TemplateVariable.VariableType.STRING)
            .isRequired(true)
            .defaultValue("test-defaultValue")
            .allowedValues(Collections.emptyList())
            .regexPattern("test-regexPattern")
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(MessageTemplate.TemplateVariable.VariableType.STRING, dto.getType());
        assertTrue(dto.getIsRequired());
        assertEquals("test-defaultValue", dto.getDefaultValue());
        assertEquals("test-regexPattern", dto.getRegexPattern());
    }

    @Test
    void testSettersAndGetters() {
        MessageTemplate.TemplateVariable dto = new MessageTemplate.TemplateVariable();
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setType(MessageTemplate.TemplateVariable.VariableType.STRING);
        dto.setIsRequired(true);
        dto.setDefaultValue("val-defaultValue");
        dto.setRegexPattern("val-regexPattern");
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(MessageTemplate.TemplateVariable.VariableType.STRING, dto.getType());
        assertTrue(dto.getIsRequired());
        assertEquals("val-defaultValue", dto.getDefaultValue());
        assertEquals("val-regexPattern", dto.getRegexPattern());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageTemplate.TemplateVariable dto1 = MessageTemplate.TemplateVariable.builder()
                        .name("test-name")
            .description("test-description")
            .type(MessageTemplate.TemplateVariable.VariableType.STRING)
            .isRequired(true)
            .defaultValue("test-defaultValue")
            .allowedValues(Collections.emptyList())
            .regexPattern("test-regexPattern")
            .build();
        MessageTemplate.TemplateVariable dto2 = MessageTemplate.TemplateVariable.builder()
                        .name("test-name")
            .description("test-description")
            .type(MessageTemplate.TemplateVariable.VariableType.STRING)
            .isRequired(true)
            .defaultValue("test-defaultValue")
            .allowedValues(Collections.emptyList())
            .regexPattern("test-regexPattern")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MessageTemplate.TemplateVariable dto = MessageTemplate.TemplateVariable.builder()
                        .name("test-name")
            .description("test-description")
            .type(MessageTemplate.TemplateVariable.VariableType.STRING)
            .isRequired(true)
            .defaultValue("test-defaultValue")
            .allowedValues(Collections.emptyList())
            .regexPattern("test-regexPattern")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}