package com.gogidix.shared.infrastructure.services.communication.email.application.mapper;

import com.gogidix.shared.infrastructure.services.communication.email.application.dto.request.CreateEmailMessageRequestDto;
import com.gogidix.shared.infrastructure.services.communication.email.application.dto.response.EmailMessageResponseDto;
import com.gogidix.shared.infrastructure.services.communication.email.domain.model.EmailMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EmailMessageMapper.
 */
@DisplayName("Email Message Mapper Tests")
class EmailMessageMapperTest {

    private EmailMessageMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EmailMessageMapper();
    }

    @Test
    @DisplayName("Should map DTO to entity")
    void shouldMapDtoToEntity() {
        CreateEmailMessageRequestDto dto = new CreateEmailMessageRequestDto(
                "test@example.com",
                "cc@example.com",
                "bcc@example.com",
                "Test Subject",
                "Test Body",
                "welcome-template",
                "SMTP",
                "campaign123"
        );

        EmailMessage entity = mapper.toEntity(dto, "tenant123");

        assertNotNull(entity);
        assertEquals("test@example.com", entity.getTo());
        assertEquals("cc@example.com", entity.getCc());
        assertEquals("bcc@example.com", entity.getBcc());
        assertEquals("Test Subject", entity.getSubject());
        assertEquals("Test Body", entity.getBody());
        assertEquals("welcome-template", entity.getTemplateName());
        assertEquals("SMTP", entity.getProvider());
        assertEquals("campaign123", entity.getCampaignId());
        assertEquals("tenant123", entity.getTenantId().getValue());
    }

    @Test
    @DisplayName("Should use default provider when not specified")
    void shouldUseDefaultProviderWhenNotSpecified() {
        CreateEmailMessageRequestDto dto = new CreateEmailMessageRequestDto(
                "test@example.com",
                null,
                null,
                "Subject",
                "Body",
                null,
                null,
                null
        );

        EmailMessage entity = mapper.toEntity(dto, "tenant123");

        assertEquals("SMTP", entity.getProvider());
    }

    @Test
    @DisplayName("Should map entity to response DTO")
    void shouldMapEntityToResponseDto() {
        EmailMessage entity = new EmailMessage();
        entity.setId("email123");
        entity.setTenantId(new com.gogidix.shared.infrastructure.core.tenancy.model.TenantId("tenant123"));
        entity.setTo("test@example.com");
        entity.setCc("cc@example.com");
        entity.setBcc("bcc@example.com");
        entity.setSubject("Test Subject");
        entity.setBody("Test Body");
        entity.setTemplateName("template");
        entity.setStatus("SENT");
        entity.setRetryCount(2);
        entity.setMaxRetries(5);
        entity.setErrorMessage("Error");
        entity.setProvider("SMTP");
        entity.setCampaignId("campaign123");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setSentAt(LocalDateTime.now());

        EmailMessageResponseDto dto = mapper.toResponseDto(entity);

        assertNotNull(dto);
        assertEquals("email123", dto.id());
        assertEquals("tenant123", dto.tenantId());
        assertEquals("test@example.com", dto.to());
        assertEquals("cc@example.com", dto.cc());
        assertEquals("bcc@example.com", dto.bcc());
        assertEquals("Test Subject", dto.subject());
        assertEquals("Test Body", dto.body());
        assertEquals("template", dto.templateName());
        assertEquals("SENT", dto.status());
        assertEquals(2, dto.retryCount());
        assertEquals(5, dto.maxRetries());
        assertEquals("Error", dto.errorMessage());
        assertEquals("SMTP", dto.provider());
        assertEquals("campaign123", dto.campaignId());
        assertNotNull(dto.createdAt());
        assertNotNull(dto.sentAt());
    }

    @Test
    @DisplayName("Should handle null tenant ID in response DTO")
    void shouldHandleNullTenantIdInResponseDto() {
        EmailMessage entity = new EmailMessage();
        entity.setId("email123");
        entity.setTenantId(null);
        entity.setTo("test@example.com");
        entity.setSubject("Subject");
        entity.setBody("Body");

        EmailMessageResponseDto dto = mapper.toResponseDto(entity);

        assertNull(dto.tenantId());
    }

    @Test
    @DisplayName("Should handle null optional fields in DTO")
    void shouldHandleNullOptionalFieldsInDto() {
        CreateEmailMessageRequestDto dto = new CreateEmailMessageRequestDto(
                "test@example.com",
                null,
                null,
                "Subject",
                "Body",
                null,
                null,
                null
        );

        EmailMessage entity = mapper.toEntity(dto, "tenant123");

        assertNull(entity.getCc());
        assertNull(entity.getBcc());
        assertNull(entity.getTemplateName());
        assertNull(entity.getCampaignId());
    }

    @Test
    @DisplayName("Should handle all status values")
    void shouldHandleAllStatusValues() {
        String[] statuses = {"PENDING", "SENT", "FAILED", "RETRYING"};

        for (String status : statuses) {
            EmailMessage entity = new EmailMessage();
            entity.setId("email123");
            entity.setTenantId(new com.gogidix.shared.infrastructure.core.tenancy.model.TenantId("tenant123"));
            entity.setTo("test@example.com");
            entity.setSubject("Subject");
            entity.setBody("Body");
            entity.setStatus(status);

            EmailMessageResponseDto dto = mapper.toResponseDto(entity);

            assertEquals(status, dto.status());
        }
    }
}
