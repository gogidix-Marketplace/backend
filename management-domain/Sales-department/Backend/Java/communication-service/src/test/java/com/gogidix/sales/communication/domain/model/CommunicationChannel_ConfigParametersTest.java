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
class CommunicationChannel_ConfigParametersTest {

        @Test
    void testBuilder() {
        CommunicationChannel.ConfigParameters dto = CommunicationChannel.ConfigParameters.builder()
                        .smtpHost("test-smtpHost")
            .smtpPort(42)
            .smtpUsername("test-smtpUsername")
            .smtpPassword("test-smtpPassword")
            .useTls(true)
            .fromEmail("test-fromEmail")
            .fromName("test-fromName")
            .replyToEmail("test-replyToEmail")
            .provider("test-provider")
            .apiKey("test-apiKey")
            .apiSecret("test-apiSecret")
            .senderId("test-senderId")
            .shortCode("test-shortCode")
            .businessAccountId("test-businessAccountId")
            .phoneNumberId("test-phoneNumberId")
            .accessToken("test-accessToken")
            .templateNamespace("test-templateNamespace")
            .firebaseServerKey("test-firebaseServerKey")
            .apnsCertificate("test-apnsCertificate")
            .apnsPrivateKey("test-apnsPrivateKey")
            .webhookUrl("test-webhookUrl")
            .authenticationMethod("test-authenticationMethod")
            .authUsername("test-authUsername")
            .authPassword("test-authPassword")
            .webhookApiKey("test-webhookApiKey")
            .headers(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-smtpHost", dto.getSmtpHost());
        assertEquals(42, dto.getSmtpPort());
        assertEquals("test-smtpUsername", dto.getSmtpUsername());
        assertEquals("test-smtpPassword", dto.getSmtpPassword());
        assertTrue(dto.getUseTls());
        assertEquals("test-fromEmail", dto.getFromEmail());
        assertEquals("test-fromName", dto.getFromName());
        assertEquals("test-replyToEmail", dto.getReplyToEmail());
        assertEquals("test-provider", dto.getProvider());
        assertEquals("test-apiKey", dto.getApiKey());
        assertEquals("test-apiSecret", dto.getApiSecret());
        assertEquals("test-senderId", dto.getSenderId());
        assertEquals("test-shortCode", dto.getShortCode());
        assertEquals("test-businessAccountId", dto.getBusinessAccountId());
        assertEquals("test-phoneNumberId", dto.getPhoneNumberId());
        assertEquals("test-accessToken", dto.getAccessToken());
        assertEquals("test-templateNamespace", dto.getTemplateNamespace());
        assertEquals("test-firebaseServerKey", dto.getFirebaseServerKey());
        assertEquals("test-apnsCertificate", dto.getApnsCertificate());
        assertEquals("test-apnsPrivateKey", dto.getApnsPrivateKey());
        assertEquals("test-webhookUrl", dto.getWebhookUrl());
        assertEquals("test-authenticationMethod", dto.getAuthenticationMethod());
        assertEquals("test-authUsername", dto.getAuthUsername());
        assertEquals("test-authPassword", dto.getAuthPassword());
        assertEquals("test-webhookApiKey", dto.getWebhookApiKey());
    }

    @Test
    void testSettersAndGetters() {
        CommunicationChannel.ConfigParameters dto = new CommunicationChannel.ConfigParameters();
        dto.setSmtpHost("val-smtpHost");
        dto.setSmtpPort(99);
        dto.setSmtpUsername("val-smtpUsername");
        dto.setSmtpPassword("val-smtpPassword");
        dto.setUseTls(true);
        dto.setFromEmail("val-fromEmail");
        dto.setFromName("val-fromName");
        dto.setReplyToEmail("val-replyToEmail");
        dto.setProvider("val-provider");
        dto.setApiKey("val-apiKey");
        dto.setApiSecret("val-apiSecret");
        dto.setSenderId("val-senderId");
        dto.setShortCode("val-shortCode");
        dto.setBusinessAccountId("val-businessAccountId");
        dto.setPhoneNumberId("val-phoneNumberId");
        dto.setAccessToken("val-accessToken");
        dto.setTemplateNamespace("val-templateNamespace");
        dto.setFirebaseServerKey("val-firebaseServerKey");
        dto.setApnsCertificate("val-apnsCertificate");
        dto.setApnsPrivateKey("val-apnsPrivateKey");
        dto.setWebhookUrl("val-webhookUrl");
        dto.setAuthenticationMethod("val-authenticationMethod");
        dto.setAuthUsername("val-authUsername");
        dto.setAuthPassword("val-authPassword");
        dto.setWebhookApiKey("val-webhookApiKey");
        assertEquals("val-smtpHost", dto.getSmtpHost());
        assertEquals(99, dto.getSmtpPort());
        assertEquals("val-smtpUsername", dto.getSmtpUsername());
        assertEquals("val-smtpPassword", dto.getSmtpPassword());
        assertTrue(dto.getUseTls());
        assertEquals("val-fromEmail", dto.getFromEmail());
        assertEquals("val-fromName", dto.getFromName());
        assertEquals("val-replyToEmail", dto.getReplyToEmail());
        assertEquals("val-provider", dto.getProvider());
        assertEquals("val-apiKey", dto.getApiKey());
        assertEquals("val-apiSecret", dto.getApiSecret());
        assertEquals("val-senderId", dto.getSenderId());
        assertEquals("val-shortCode", dto.getShortCode());
        assertEquals("val-businessAccountId", dto.getBusinessAccountId());
        assertEquals("val-phoneNumberId", dto.getPhoneNumberId());
        assertEquals("val-accessToken", dto.getAccessToken());
        assertEquals("val-templateNamespace", dto.getTemplateNamespace());
        assertEquals("val-firebaseServerKey", dto.getFirebaseServerKey());
        assertEquals("val-apnsCertificate", dto.getApnsCertificate());
        assertEquals("val-apnsPrivateKey", dto.getApnsPrivateKey());
        assertEquals("val-webhookUrl", dto.getWebhookUrl());
        assertEquals("val-authenticationMethod", dto.getAuthenticationMethod());
        assertEquals("val-authUsername", dto.getAuthUsername());
        assertEquals("val-authPassword", dto.getAuthPassword());
        assertEquals("val-webhookApiKey", dto.getWebhookApiKey());
    }

    @Test
    void testEqualsAndHashCode() {
        CommunicationChannel.ConfigParameters dto1 = CommunicationChannel.ConfigParameters.builder()
                        .smtpHost("test-smtpHost")
            .smtpPort(42)
            .smtpUsername("test-smtpUsername")
            .smtpPassword("test-smtpPassword")
            .useTls(true)
            .fromEmail("test-fromEmail")
            .fromName("test-fromName")
            .replyToEmail("test-replyToEmail")
            .provider("test-provider")
            .apiKey("test-apiKey")
            .apiSecret("test-apiSecret")
            .senderId("test-senderId")
            .shortCode("test-shortCode")
            .businessAccountId("test-businessAccountId")
            .phoneNumberId("test-phoneNumberId")
            .accessToken("test-accessToken")
            .templateNamespace("test-templateNamespace")
            .firebaseServerKey("test-firebaseServerKey")
            .apnsCertificate("test-apnsCertificate")
            .apnsPrivateKey("test-apnsPrivateKey")
            .webhookUrl("test-webhookUrl")
            .authenticationMethod("test-authenticationMethod")
            .authUsername("test-authUsername")
            .authPassword("test-authPassword")
            .webhookApiKey("test-webhookApiKey")
            .headers(Collections.emptyList())
            .build();
        CommunicationChannel.ConfigParameters dto2 = CommunicationChannel.ConfigParameters.builder()
                        .smtpHost("test-smtpHost")
            .smtpPort(42)
            .smtpUsername("test-smtpUsername")
            .smtpPassword("test-smtpPassword")
            .useTls(true)
            .fromEmail("test-fromEmail")
            .fromName("test-fromName")
            .replyToEmail("test-replyToEmail")
            .provider("test-provider")
            .apiKey("test-apiKey")
            .apiSecret("test-apiSecret")
            .senderId("test-senderId")
            .shortCode("test-shortCode")
            .businessAccountId("test-businessAccountId")
            .phoneNumberId("test-phoneNumberId")
            .accessToken("test-accessToken")
            .templateNamespace("test-templateNamespace")
            .firebaseServerKey("test-firebaseServerKey")
            .apnsCertificate("test-apnsCertificate")
            .apnsPrivateKey("test-apnsPrivateKey")
            .webhookUrl("test-webhookUrl")
            .authenticationMethod("test-authenticationMethod")
            .authUsername("test-authUsername")
            .authPassword("test-authPassword")
            .webhookApiKey("test-webhookApiKey")
            .headers(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CommunicationChannel.ConfigParameters dto = CommunicationChannel.ConfigParameters.builder()
                        .smtpHost("test-smtpHost")
            .smtpPort(42)
            .smtpUsername("test-smtpUsername")
            .smtpPassword("test-smtpPassword")
            .useTls(true)
            .fromEmail("test-fromEmail")
            .fromName("test-fromName")
            .replyToEmail("test-replyToEmail")
            .provider("test-provider")
            .apiKey("test-apiKey")
            .apiSecret("test-apiSecret")
            .senderId("test-senderId")
            .shortCode("test-shortCode")
            .businessAccountId("test-businessAccountId")
            .phoneNumberId("test-phoneNumberId")
            .accessToken("test-accessToken")
            .templateNamespace("test-templateNamespace")
            .firebaseServerKey("test-firebaseServerKey")
            .apnsCertificate("test-apnsCertificate")
            .apnsPrivateKey("test-apnsPrivateKey")
            .webhookUrl("test-webhookUrl")
            .authenticationMethod("test-authenticationMethod")
            .authUsername("test-authUsername")
            .authPassword("test-authPassword")
            .webhookApiKey("test-webhookApiKey")
            .headers(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}