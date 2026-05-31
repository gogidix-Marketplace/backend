package com.gogidix.shared.infrastructure.services.communication.sms.infrastructure.sms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SmsGatewayService interface contract.
 */
@DisplayName("SMS Gateway Service Contract Tests")
class SmsGatewayServiceTest {

    @Test
    @DisplayName("Should verify all delivery statuses")
    void shouldVerifyAllDeliveryStatuses() {
        SmsGatewayService.DeliveryStatus[] statuses = SmsGatewayService.DeliveryStatus.values();

        assertEquals(8, statuses.length);
        assertEquals(SmsGatewayService.DeliveryStatus.PENDING, SmsGatewayService.DeliveryStatus.valueOf("PENDING"));
        assertEquals(SmsGatewayService.DeliveryStatus.SENT, SmsGatewayService.DeliveryStatus.valueOf("SENT"));
        assertEquals(SmsGatewayService.DeliveryStatus.DELIVERED, SmsGatewayService.DeliveryStatus.valueOf("DELIVERED"));
        assertEquals(SmsGatewayService.DeliveryStatus.FAILED, SmsGatewayService.DeliveryStatus.valueOf("FAILED"));
        assertEquals(SmsGatewayService.DeliveryStatus.UNDELIVERED, SmsGatewayService.DeliveryStatus.valueOf("UNDELIVERED"));
        assertEquals(SmsGatewayService.DeliveryStatus.EXPIRED, SmsGatewayService.DeliveryStatus.valueOf("EXPIRED"));
        assertEquals(SmsGatewayService.DeliveryStatus.REJECTED, SmsGatewayService.DeliveryStatus.valueOf("REJECTED"));
        assertEquals(SmsGatewayService.DeliveryStatus.UNKNOWN, SmsGatewayService.DeliveryStatus.valueOf("UNKNOWN"));
    }

    @Test
    @DisplayName("Should verify SmsSendingException")
    void shouldVerifySmsSendingException() {
        SmsGatewayService.SmsSendingException exception1 =
                new SmsGatewayService.SmsSendingException("Failed to send", "+1234567890");

        assertEquals("Failed to send", exception1.getMessage());
        assertEquals("+1234567890", exception1.getRecipient());
        assertNull(exception1.getErrorCode());

        SmsGatewayService.SmsSendingException exception2 =
                new SmsGatewayService.SmsSendingException("Failed to send", "+1234567890", "ERR_001");

        assertEquals("ERR_001", exception2.getErrorCode());

        SmsGatewayService.SmsSendingException exception3 =
                new SmsGatewayService.SmsSendingException("Failed to send", "+1234567890", new RuntimeException("Cause"));

        assertNotNull(exception3.getCause());
    }

    @Test
    @DisplayName("Should verify delivery status enum values")
    void shouldVerifyDeliveryStatusEnumValues() {
        assertArrayEquals(
                new SmsGatewayService.DeliveryStatus[]{
                        SmsGatewayService.DeliveryStatus.PENDING,
                        SmsGatewayService.DeliveryStatus.SENT,
                        SmsGatewayService.DeliveryStatus.DELIVERED,
                        SmsGatewayService.DeliveryStatus.FAILED,
                        SmsGatewayService.DeliveryStatus.UNDELIVERED,
                        SmsGatewayService.DeliveryStatus.EXPIRED,
                        SmsGatewayService.DeliveryStatus.REJECTED,
                        SmsGatewayService.DeliveryStatus.UNKNOWN
                },
                SmsGatewayService.DeliveryStatus.values()
        );
    }
}
