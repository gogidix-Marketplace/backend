package com.gogidix.sales.notification.domain.model;

import com.gogidix.sales.notification.domain.model.NotificationTemplate;
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
class NotificationTemplate_TemplateVariableTest {

        @Test
    void testBuilder() {
        NotificationTemplate.TemplateVariable dto = NotificationTemplate.TemplateVariable.builder()
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
        NotificationTemplate.TemplateVariable dto = new NotificationTemplate.TemplateVariable();
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
        NotificationTemplate.TemplateVariable dto1 = NotificationTemplate.TemplateVariable.builder()
                        .name("test-name")
            .type("test-type")
            .description("test-description")
            .required(true)
            .defaultValue("test-defaultValue")
            .build();
        NotificationTemplate.TemplateVariable dto2 = NotificationTemplate.TemplateVariable.builder()
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
        NotificationTemplate.TemplateVariable dto = NotificationTemplate.TemplateVariable.builder()
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