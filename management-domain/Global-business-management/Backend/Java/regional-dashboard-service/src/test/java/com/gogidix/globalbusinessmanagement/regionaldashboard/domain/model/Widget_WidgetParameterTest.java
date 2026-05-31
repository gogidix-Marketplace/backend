package com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model;

import com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model.Widget;
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
class Widget_WidgetParameterTest {

        @Test
    void testBuilder() {
        Widget.WidgetParameter dto = Widget.WidgetParameter.builder()
                        .name("test-name")
            .displayName("test-displayName")
            .type("test-type")
            .defaultValue("test-defaultValue")
            .isRequired(true)
            .options(Collections.emptyList())
            .validation("test-validation")
            .description("test-description")
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-displayName", dto.getDisplayName());
        assertEquals("test-type", dto.getType());
        assertEquals("test-defaultValue", dto.getDefaultValue());
        assertTrue(dto.getIsRequired());
        assertEquals("test-validation", dto.getValidation());
        assertEquals("test-description", dto.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        Widget.WidgetParameter dto = new Widget.WidgetParameter();
        dto.setName("val-name");
        dto.setDisplayName("val-displayName");
        dto.setType("val-type");
        dto.setDefaultValue("val-defaultValue");
        dto.setIsRequired(true);
        dto.setValidation("val-validation");
        dto.setDescription("val-description");
        assertEquals("val-name", dto.getName());
        assertEquals("val-displayName", dto.getDisplayName());
        assertEquals("val-type", dto.getType());
        assertEquals("val-defaultValue", dto.getDefaultValue());
        assertTrue(dto.getIsRequired());
        assertEquals("val-validation", dto.getValidation());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        Widget.WidgetParameter dto1 = Widget.WidgetParameter.builder()
                        .name("test-name")
            .displayName("test-displayName")
            .type("test-type")
            .defaultValue("test-defaultValue")
            .isRequired(true)
            .options(Collections.emptyList())
            .validation("test-validation")
            .description("test-description")
            .build();
        Widget.WidgetParameter dto2 = Widget.WidgetParameter.builder()
                        .name("test-name")
            .displayName("test-displayName")
            .type("test-type")
            .defaultValue("test-defaultValue")
            .isRequired(true)
            .options(Collections.emptyList())
            .validation("test-validation")
            .description("test-description")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Widget.WidgetParameter dto = Widget.WidgetParameter.builder()
                        .name("test-name")
            .displayName("test-displayName")
            .type("test-type")
            .defaultValue("test-defaultValue")
            .isRequired(true)
            .options(Collections.emptyList())
            .validation("test-validation")
            .description("test-description")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}