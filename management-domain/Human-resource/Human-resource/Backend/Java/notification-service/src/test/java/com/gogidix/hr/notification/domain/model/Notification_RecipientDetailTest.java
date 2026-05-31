package com.gogidix.hr.notification.domain.model;

import com.gogidix.hr.notification.domain.model.Notification;
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
class Notification_RecipientDetailTest {

        @Test
    void testSettersAndGetters() {
        Notification.RecipientDetail dto = new Notification.RecipientDetail();
        dto.setRecipientId("val-recipientId");
        dto.setRecipientName("val-recipientName");
        dto.setRecipientEmail("val-recipientEmail");
        dto.setRecipientPhone("val-recipientPhone");
        dto.setDeliveryStatus("val-deliveryStatus");
        dto.setRead(true);
        assertEquals("val-recipientId", dto.getRecipientId());
        assertEquals("val-recipientName", dto.getRecipientName());
        assertEquals("val-recipientEmail", dto.getRecipientEmail());
        assertEquals("val-recipientPhone", dto.getRecipientPhone());
        assertEquals("val-deliveryStatus", dto.getDeliveryStatus());
        assertTrue(dto.getRead());
    }

    @Test
    void testEqualsAndHashCode() {
        Notification.RecipientDetail dto1 = new Notification.RecipientDetail();
        Notification.RecipientDetail dto2 = new Notification.RecipientDetail();
        dto1.setRecipientId("test");
        dto1.setRecipientName("test");
        dto1.setRecipientEmail("test");
        dto1.setRecipientPhone("test");
        dto1.setDeliveryStatus("test");
        dto1.setDeliveryTime(LocalDateTime.of(2025,1,1,10,0));
        dto1.setRead(true);
        dto1.setReadTime(LocalDateTime.of(2025,1,1,10,0));
        dto2.setRecipientId("test");
        dto2.setRecipientName("test");
        dto2.setRecipientEmail("test");
        dto2.setRecipientPhone("test");
        dto2.setDeliveryStatus("test");
        dto2.setDeliveryTime(LocalDateTime.of(2025,1,1,10,0));
        dto2.setRead(true);
        dto2.setReadTime(LocalDateTime.of(2025,1,1,10,0));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setRecipientId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        Notification.RecipientDetail dto = new Notification.RecipientDetail();
        dto.setRecipientId("test");
        dto.setRecipientName("test");
        dto.setRecipientEmail("test");
        dto.setRecipientPhone("test");
        dto.setDeliveryStatus("test");
        dto.setDeliveryTime(LocalDateTime.of(2025,1,1,10,0));
        dto.setRead(true);
        dto.setReadTime(LocalDateTime.of(2025,1,1,10,0));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        Notification.RecipientDetail dto = new Notification.RecipientDetail();
        dto.setRecipientId("test");
        dto.setRecipientName("test");
        dto.setRecipientEmail("test");
        dto.setRecipientPhone("test");
        dto.setDeliveryStatus("test");
        dto.setDeliveryTime(LocalDateTime.of(2025,1,1,10,0));
        dto.setRead(true);
        dto.setReadTime(LocalDateTime.of(2025,1,1,10,0));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}