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
class NotificationTemplate_LocalizedContentTest {

        @Test
    void testSettersAndGetters() {
        NotificationTemplate.LocalizedContent dto = new NotificationTemplate.LocalizedContent();
        dto.setLanguage("val-language");
        dto.setSubject("val-subject");
        dto.setBody("val-body");
        dto.setHtmlBody("val-htmlBody");
        assertEquals("val-language", dto.getLanguage());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-body", dto.getBody());
        assertEquals("val-htmlBody", dto.getHtmlBody());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationTemplate.LocalizedContent dto1 = new NotificationTemplate.LocalizedContent();
        NotificationTemplate.LocalizedContent dto2 = new NotificationTemplate.LocalizedContent();
        dto1.setLanguage("test");
        dto1.setSubject("test");
        dto1.setBody("test");
        dto1.setHtmlBody("test");
        dto2.setLanguage("test");
        dto2.setSubject("test");
        dto2.setBody("test");
        dto2.setHtmlBody("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setLanguage(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        NotificationTemplate.LocalizedContent dto = new NotificationTemplate.LocalizedContent();
        dto.setLanguage("test");
        dto.setSubject("test");
        dto.setBody("test");
        dto.setHtmlBody("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        NotificationTemplate.LocalizedContent dto = new NotificationTemplate.LocalizedContent();
        dto.setLanguage("test");
        dto.setSubject("test");
        dto.setBody("test");
        dto.setHtmlBody("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}