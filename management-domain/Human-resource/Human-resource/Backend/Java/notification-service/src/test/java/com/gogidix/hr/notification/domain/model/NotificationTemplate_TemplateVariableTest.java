package com.gogidix.hr.notification.domain.model;

import com.gogidix.hr.notification.domain.model.NotificationTemplate;
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
    void testSettersAndGetters() {
        NotificationTemplate.TemplateVariable dto = new NotificationTemplate.TemplateVariable();
        dto.setName("val-name");
        dto.setType("val-type");
        dto.setDescription("val-description");
        dto.setIsRequired(true);
        dto.setDefaultValue("val-defaultValue");
        assertEquals("val-name", dto.getName());
        assertEquals("val-type", dto.getType());
        assertEquals("val-description", dto.getDescription());
        assertTrue(dto.getIsRequired());
        assertEquals("val-defaultValue", dto.getDefaultValue());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationTemplate.TemplateVariable dto1 = new NotificationTemplate.TemplateVariable();
        NotificationTemplate.TemplateVariable dto2 = new NotificationTemplate.TemplateVariable();
        dto1.setName("test");
        dto1.setType("test");
        dto1.setDescription("test");
        dto1.setIsRequired(true);
        dto1.setDefaultValue("test");
        dto2.setName("test");
        dto2.setType("test");
        dto2.setDescription("test");
        dto2.setIsRequired(true);
        dto2.setDefaultValue("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        NotificationTemplate.TemplateVariable dto = new NotificationTemplate.TemplateVariable();
        dto.setName("test");
        dto.setType("test");
        dto.setDescription("test");
        dto.setIsRequired(true);
        dto.setDefaultValue("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        NotificationTemplate.TemplateVariable dto = new NotificationTemplate.TemplateVariable();
        dto.setName("test");
        dto.setType("test");
        dto.setDescription("test");
        dto.setIsRequired(true);
        dto.setDefaultValue("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}