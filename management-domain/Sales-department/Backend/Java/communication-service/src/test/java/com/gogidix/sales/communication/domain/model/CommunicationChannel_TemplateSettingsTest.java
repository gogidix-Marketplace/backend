package com.gogidix.sales.communication.domain.model;

import com.gogidix.sales.communication.domain.model.CommunicationChannel;
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
class CommunicationChannel_TemplateSettingsTest {

        @Test
    void testBuilder() {
        CommunicationChannel.TemplateSettings dto = CommunicationChannel.TemplateSettings.builder()
                        .isEnabled(true)
            .templateEngine("test-templateEngine")
            .templateLocation("test-templateLocation")
            .defaultTemplates(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertTrue(dto.getIsEnabled());
        assertEquals("test-templateEngine", dto.getTemplateEngine());
        assertEquals("test-templateLocation", dto.getTemplateLocation());
    }

    @Test
    void testSettersAndGetters() {
        CommunicationChannel.TemplateSettings dto = new CommunicationChannel.TemplateSettings();
        dto.setIsEnabled(true);
        dto.setTemplateEngine("val-templateEngine");
        dto.setTemplateLocation("val-templateLocation");
        assertTrue(dto.getIsEnabled());
        assertEquals("val-templateEngine", dto.getTemplateEngine());
        assertEquals("val-templateLocation", dto.getTemplateLocation());
    }

    @Test
    void testEqualsAndHashCode() {
        CommunicationChannel.TemplateSettings dto1 = CommunicationChannel.TemplateSettings.builder()
                        .isEnabled(true)
            .templateEngine("test-templateEngine")
            .templateLocation("test-templateLocation")
            .defaultTemplates(Collections.emptyMap())
            .build();
        CommunicationChannel.TemplateSettings dto2 = CommunicationChannel.TemplateSettings.builder()
                        .isEnabled(true)
            .templateEngine("test-templateEngine")
            .templateLocation("test-templateLocation")
            .defaultTemplates(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CommunicationChannel.TemplateSettings dto = CommunicationChannel.TemplateSettings.builder()
                        .isEnabled(true)
            .templateEngine("test-templateEngine")
            .templateLocation("test-templateLocation")
            .defaultTemplates(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}