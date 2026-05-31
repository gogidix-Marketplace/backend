package com.gogidix.sales.notification.domain.port.in;

import com.gogidix.sales.notification.domain.port.in.NotificationTemplateCommand;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
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
class NotificationTemplateCommand_CreateTemplateCommandTest {

        @Test
    void testSettersAndGetters() {
        NotificationTemplateCommand.CreateTemplateCommand dto = new NotificationTemplateCommand.CreateTemplateCommand();
        dto.setTenantId("val-tenantId");
        dto.setCode("val-code");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setSubjectTemplate("val-subjectTemplate");
        dto.setContentTemplate("val-contentTemplate");
        dto.setHtmlContentTemplate("val-htmlContentTemplate");
        dto.setLocale("val-locale");
        dto.setTags("val-tags");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-code", dto.getCode());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-subjectTemplate", dto.getSubjectTemplate());
        assertEquals("val-contentTemplate", dto.getContentTemplate());
        assertEquals("val-htmlContentTemplate", dto.getHtmlContentTemplate());
        assertEquals("val-locale", dto.getLocale());
        assertEquals("val-tags", dto.getTags());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationTemplateCommand.CreateTemplateCommand dto1 = new NotificationTemplateCommand.CreateTemplateCommand();
        NotificationTemplateCommand.CreateTemplateCommand dto2 = new NotificationTemplateCommand.CreateTemplateCommand();
        dto1.setTenantId("test");
        dto1.setCode("test");
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setChannel(NotificationChannel.EMAIL);
        dto1.setSubjectTemplate("test");
        dto1.setContentTemplate("test");
        dto1.setHtmlContentTemplate("test");
        dto1.setVariables(Collections.emptyMap());
        dto1.setLocale("test");
        dto1.setValidFrom(null);
        dto1.setValidUntil(null);
        dto1.setTags("test");
        dto2.setTenantId("test");
        dto2.setCode("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setChannel(NotificationChannel.EMAIL);
        dto2.setSubjectTemplate("test");
        dto2.setContentTemplate("test");
        dto2.setHtmlContentTemplate("test");
        dto2.setVariables(Collections.emptyMap());
        dto2.setLocale("test");
        dto2.setValidFrom(null);
        dto2.setValidUntil(null);
        dto2.setTags("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        NotificationTemplateCommand.CreateTemplateCommand dto = new NotificationTemplateCommand.CreateTemplateCommand();
        dto.setTenantId("test");
        dto.setCode("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setChannel(NotificationChannel.EMAIL);
        dto.setSubjectTemplate("test");
        dto.setContentTemplate("test");
        dto.setHtmlContentTemplate("test");
        dto.setVariables(Collections.emptyMap());
        dto.setLocale("test");
        dto.setValidFrom(null);
        dto.setValidUntil(null);
        dto.setTags("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        NotificationTemplateCommand.CreateTemplateCommand dto = new NotificationTemplateCommand.CreateTemplateCommand();
        dto.setTenantId("test");
        dto.setCode("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setChannel(NotificationChannel.EMAIL);
        dto.setSubjectTemplate("test");
        dto.setContentTemplate("test");
        dto.setHtmlContentTemplate("test");
        dto.setVariables(Collections.emptyMap());
        dto.setLocale("test");
        dto.setValidFrom(null);
        dto.setValidUntil(null);
        dto.setTags("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}