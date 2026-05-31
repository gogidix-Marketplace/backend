package com.gogidix.sales.notification.domain.port.in;

import com.gogidix.sales.notification.domain.port.in.NotificationTemplateCommand;
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
class NotificationTemplateCommand_UpdateTemplateCommandTest {

        @Test
    void testSettersAndGetters() {
        NotificationTemplateCommand.UpdateTemplateCommand dto = new NotificationTemplateCommand.UpdateTemplateCommand();
        dto.setTenantId("val-tenantId");
        dto.setTemplateId("val-templateId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setSubjectTemplate("val-subjectTemplate");
        dto.setContentTemplate("val-contentTemplate");
        dto.setHtmlContentTemplate("val-htmlContentTemplate");
        dto.setLocale("val-locale");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-subjectTemplate", dto.getSubjectTemplate());
        assertEquals("val-contentTemplate", dto.getContentTemplate());
        assertEquals("val-htmlContentTemplate", dto.getHtmlContentTemplate());
        assertEquals("val-locale", dto.getLocale());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationTemplateCommand.UpdateTemplateCommand dto1 = new NotificationTemplateCommand.UpdateTemplateCommand();
        NotificationTemplateCommand.UpdateTemplateCommand dto2 = new NotificationTemplateCommand.UpdateTemplateCommand();
        dto1.setTenantId("test");
        dto1.setTemplateId("test");
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setSubjectTemplate("test");
        dto1.setContentTemplate("test");
        dto1.setHtmlContentTemplate("test");
        dto1.setVariables(Collections.emptyMap());
        dto1.setLocale("test");
        dto2.setTenantId("test");
        dto2.setTemplateId("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setSubjectTemplate("test");
        dto2.setContentTemplate("test");
        dto2.setHtmlContentTemplate("test");
        dto2.setVariables(Collections.emptyMap());
        dto2.setLocale("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        NotificationTemplateCommand.UpdateTemplateCommand dto = new NotificationTemplateCommand.UpdateTemplateCommand();
        dto.setTenantId("test");
        dto.setTemplateId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setSubjectTemplate("test");
        dto.setContentTemplate("test");
        dto.setHtmlContentTemplate("test");
        dto.setVariables(Collections.emptyMap());
        dto.setLocale("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        NotificationTemplateCommand.UpdateTemplateCommand dto = new NotificationTemplateCommand.UpdateTemplateCommand();
        dto.setTenantId("test");
        dto.setTemplateId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setSubjectTemplate("test");
        dto.setContentTemplate("test");
        dto.setHtmlContentTemplate("test");
        dto.setVariables(Collections.emptyMap());
        dto.setLocale("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}