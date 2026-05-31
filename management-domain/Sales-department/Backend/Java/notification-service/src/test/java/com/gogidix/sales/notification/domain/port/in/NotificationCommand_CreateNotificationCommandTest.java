package com.gogidix.sales.notification.domain.port.in;

import com.gogidix.sales.notification.domain.port.in.NotificationCommand;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationPriority;
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
class NotificationCommand_CreateNotificationCommandTest {

        @Test
    void testSettersAndGetters() {
        NotificationCommand.CreateNotificationCommand dto = new NotificationCommand.CreateNotificationCommand();
        dto.setTenantId("val-tenantId");
        dto.setUserId("val-userId");
        dto.setSubject("val-subject");
        dto.setContent("val-content");
        dto.setHtmlContent("val-htmlContent");
        dto.setTemplateId("val-templateId");
        dto.setCategory("val-category");
        dto.setActionType("val-actionType");
        dto.setActionUrl("val-actionUrl");
        dto.setGroupId("val-groupId");
        dto.setIsBatched(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-userId", dto.getUserId());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-content", dto.getContent());
        assertEquals("val-htmlContent", dto.getHtmlContent());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-actionType", dto.getActionType());
        assertEquals("val-actionUrl", dto.getActionUrl());
        assertEquals("val-groupId", dto.getGroupId());
        assertTrue(dto.getIsBatched());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationCommand.CreateNotificationCommand dto1 = new NotificationCommand.CreateNotificationCommand();
        NotificationCommand.CreateNotificationCommand dto2 = new NotificationCommand.CreateNotificationCommand();
        dto1.setTenantId("test");
        dto1.setUserId("test");
        dto1.setRecipients(Collections.emptyList());
        dto1.setChannel(NotificationChannel.EMAIL);
        dto1.setSubject("test");
        dto1.setContent("test");
        dto1.setHtmlContent("test");
        dto1.setTemplateId("test");
        dto1.setTemplateVariables(Collections.emptyMap());
        dto1.setPriority(NotificationPriority.LOW);
        dto1.setCategory("test");
        dto1.setActionType("test");
        dto1.setActionUrl("test");
        dto1.setScheduledAt(null);
        dto1.setExpiresAt(null);
        dto1.setGroupId("test");
        dto1.setIsBatched(true);
        dto1.setMetadata(Collections.emptyMap());
        dto2.setTenantId("test");
        dto2.setUserId("test");
        dto2.setRecipients(Collections.emptyList());
        dto2.setChannel(NotificationChannel.EMAIL);
        dto2.setSubject("test");
        dto2.setContent("test");
        dto2.setHtmlContent("test");
        dto2.setTemplateId("test");
        dto2.setTemplateVariables(Collections.emptyMap());
        dto2.setPriority(NotificationPriority.LOW);
        dto2.setCategory("test");
        dto2.setActionType("test");
        dto2.setActionUrl("test");
        dto2.setScheduledAt(null);
        dto2.setExpiresAt(null);
        dto2.setGroupId("test");
        dto2.setIsBatched(true);
        dto2.setMetadata(Collections.emptyMap());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        NotificationCommand.CreateNotificationCommand dto = new NotificationCommand.CreateNotificationCommand();
        dto.setTenantId("test");
        dto.setUserId("test");
        dto.setRecipients(Collections.emptyList());
        dto.setChannel(NotificationChannel.EMAIL);
        dto.setSubject("test");
        dto.setContent("test");
        dto.setHtmlContent("test");
        dto.setTemplateId("test");
        dto.setTemplateVariables(Collections.emptyMap());
        dto.setPriority(NotificationPriority.LOW);
        dto.setCategory("test");
        dto.setActionType("test");
        dto.setActionUrl("test");
        dto.setScheduledAt(null);
        dto.setExpiresAt(null);
        dto.setGroupId("test");
        dto.setIsBatched(true);
        dto.setMetadata(Collections.emptyMap());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        NotificationCommand.CreateNotificationCommand dto = new NotificationCommand.CreateNotificationCommand();
        dto.setTenantId("test");
        dto.setUserId("test");
        dto.setRecipients(Collections.emptyList());
        dto.setChannel(NotificationChannel.EMAIL);
        dto.setSubject("test");
        dto.setContent("test");
        dto.setHtmlContent("test");
        dto.setTemplateId("test");
        dto.setTemplateVariables(Collections.emptyMap());
        dto.setPriority(NotificationPriority.LOW);
        dto.setCategory("test");
        dto.setActionType("test");
        dto.setActionUrl("test");
        dto.setScheduledAt(null);
        dto.setExpiresAt(null);
        dto.setGroupId("test");
        dto.setIsBatched(true);
        dto.setMetadata(Collections.emptyMap());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}